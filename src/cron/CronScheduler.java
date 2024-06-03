package cron;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;

public class CronScheduler {
    private boolean graceFlag;
    private boolean sleepFlag;
    private final int gracePeriod = 60000;
    private final Thread schedulerThread;
    private final Map<CronJob, Thread> activeThreads;
    private final PriorityQueue<CronJob> jobs;

    public CronScheduler() {
        graceFlag = false;
        sleepFlag = false;
        this.schedulerThread = new Thread(this::schedule,"SchedulerThread");
//        this.threadPool = new ThreadPool();
        this.jobs = new PriorityQueue<>(Comparator.comparing(CronJob::getScheduledTime));
        activeThreads = new HashMap<>();
//        this.nextJob = null;
    }

    private void schedule() {
        //todo shutdown flag?
        while (true) {
            if (jobs.isEmpty()) {
                try {
                    graceFlag = true;
//                    System.err.println("No tasks in queue, waiting for job");
                    Thread.sleep(gracePeriod);
                    System.err.println("No tasks scheduled during grace period.");
                    System.err.println("Terminating program.");
                    //todo is system exit necessary?
                    System.exit(0);
                } catch (InterruptedException e) {
//                    System.err.println("Task scheduled before grace period is up");
                    //todo reset interrupt flag?
                    Thread.interrupted(); // clear interruption flag
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
            Thread thread = new Thread(()->{
                try {
                    long startTime = System.currentTimeMillis();
                    Object jobOutput = job.getTask().call();
                    System.out.println(job.getJobId()+" Output: "+jobOutput);
                    long runTime = System.currentTimeMillis() - startTime;
                    //todo log output and metrics
                } catch (Exception e) {
                    System.err.println("Task failed to run, removing it from schedule");
                    System.out.println(e.getMessage());
                    return;
                }
                finally {
                    activeThreads.remove(job);
                }
                requeueJob(job);
            },
                    job.getJobId());
            activeThreads.put(job, thread);
            thread.start();
        }
    }


    public void queueJob(String jobId, String runInterval, Callable<Object> task) {
        try {
            CronJob job = new CronJob(jobId, runInterval, task);
            this.jobs.add(job);
        }
        catch (Exception e) {
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
    }

    private void requeueJob(CronJob job) {
        job.setScheduledTime();
        this.jobs.add(job);
        if (graceFlag) {
            graceFlag = false;
            schedulerThread.interrupt();
        }
    }

}
