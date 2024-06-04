package main.cron;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Semaphore;

public class CronScheduler {
    private boolean graceFlag;
    private boolean sleepFlag;
    private boolean stopFlag;
    private final int gracePeriod = 60000;
    private long maxTimeToLive;
    private final Thread schedulerThread;
    private final Semaphore semaphore;
    private final Map<CronJob, Thread> activeThreads;
    private final PriorityQueue<CronJob> jobs;
    private final Logger logger;

    public CronScheduler() {
        this.graceFlag = false;
        this.sleepFlag = false;
        this.stopFlag = false;
        this.schedulerThread = new Thread(this::schedule, "SchedulerThread");
        this.semaphore = new Semaphore(1);
        this.jobs = new PriorityQueue<>(Comparator.comparing(CronJob::getScheduledTime));
        this.activeThreads = new HashMap<>();
        this.logger = new Logger("scheduler-log.txt");
        this.maxTimeToLive = 0;
    }

    private void schedule() {
        while (!stopFlag) {
            try {
                semaphore.acquire();
                if (!activeThreads.isEmpty()) {
                    long currentTime = System.currentTimeMillis();
                    for (CronJob job : activeThreads.keySet()) {
                        if (job.getScheduledTime() + job.getMaxRunTime() < currentTime) {
                            System.err.println(job.getJobId() + " exceeded max runTime, interrupting thread");
                            activeThreads.get(job).interrupt(); // Job needs to be interruptible, can't kill running thread
                        }
                    }
                }
            } catch (InterruptedException e) {
                continue;
            } finally {
                semaphore.release();
            }

            if (jobs.isEmpty()) {
                try {
                    graceFlag = true;
                    Thread.sleep(Math.max(gracePeriod, maxTimeToLive));
                    System.err.println("No tasks re-queued during grace period."); // runaway threads
                    System.err.println("Terminating program.");
                    System.exit(1); // status 1 indicates some error
                } catch (InterruptedException e) {
                    Thread.interrupted(); // clear interruption flag
                    continue;
                }
            }

            try {
                long sleepTime = jobs.peek().getScheduledTime() - System.currentTimeMillis();
                if (sleepTime > 0) {
                    sleepFlag = true;
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                System.err.println("Scheduled earlier job");
                Thread.interrupted();
                continue;
            }
            CronJob job = jobs.poll();
            Thread thread = createJobThread(job);
            semaphore.acquireUninterruptibly();
            activeThreads.put(job, thread);
            semaphore.release();
            thread.start();
        }
    }

    private Thread createJobThread(CronJob job) {
        Thread thread = new Thread(() -> {
            try {
                long startTime = System.currentTimeMillis();
                Object jobOutput = job.getTask().call();
//                System.out.println(job.getJobId()+" Output: "+jobOutput);
                long runTime = System.currentTimeMillis() - startTime;
                logger.log(job.getJobId(), runTime, jobOutput.toString());
            } catch (Exception e) {
                System.err.println("Task failed to run, removing it from schedule");
                System.out.println(e.getMessage());
                return;
            } finally {
                semaphore.acquireUninterruptibly();
                activeThreads.remove(job);
                semaphore.release();
            }
            requeueJob(job);
        }, job.getJobId());
        thread.setDaemon(true);
        return thread;
    }

    public void queueJob(String jobId, String runInterval, String maxRunTime, Callable<Object> task) {
        try {
            CronJob job = new CronJob(jobId, runInterval, maxRunTime, task);
            jobs.add(job);
            if (job.getMaxRunTime() > maxTimeToLive) {
                maxTimeToLive = job.getMaxRunTime();
            }
        } catch (Exception e) {
            System.out.println("Could not create job");
            return;
        }

        if (!schedulerThread.isAlive()) {
            schedulerThread.start();
        }
        if (jobs.peek().getJobId().equals(jobId) && sleepFlag) {
            sleepFlag = false;
            schedulerThread.interrupt();
        }
        if (graceFlag) {
            graceFlag = false;
            schedulerThread.interrupt();
        }
        System.out.println(jobId + ": Job scheduled");
    }

    private void requeueJob(CronJob job) {
        job.setScheduledTime();
        jobs.add(job);
        if (graceFlag) {
            graceFlag = false;
            schedulerThread.interrupt();
        }
    }

    public void stopScheduler() {
        stopFlag = true;
        schedulerThread.interrupt();
        try {
            logger.close();
        } catch (IOException e) {
            System.err.println("Failed to close logger");
        }
    }

}
