package cron;

import java.util.Comparator;
import java.util.PriorityQueue;

public class CronScheduler {
    private boolean isAlive = false;
    private Thread activeThread = null;
    private final PriorityQueue<CronJob> jobs = new PriorityQueue<>(Comparator.comparing(CronJob::getScheduledTime));

    public void createActiveThread() {
        this.activeThread = new Thread(() -> {
            try {
                // check if scheduled time has passed, sleep till then otherwise
                long currentTime = System.currentTimeMillis();
                if (jobs.peek().getScheduledTime() > currentTime)
                    Thread.sleep(jobs.peek().getScheduledTime()-currentTime);
                CronJob job = jobs.poll();
                isAlive = true;
//                System.out.println("pre run"+System.currentTimeMillis());
                job.getTask().run();
//                System.out.println("post run " + System.currentTimeMillis());
                isAlive=false;
                reschedule(job);
            }
            catch (InterruptedException e) {
                System.out.println("Interrupted to schedule an earlier job");
            }
        }
        ,"ActiveThread");

        this.activeThread.start();
    }


    public void schedule(String jobId, String runInterval, Runnable task) {

        CronJob job;
        try {
            job = new CronJob(jobId, runInterval, task);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
        this.jobs.add(job);

        if (activeThread==null) { // create initial thread
            createActiveThread();
        }
        else if (!isAlive && jobs.peek().getJobId().equals(jobId)){ //schedule an earlier job
            activeThread.interrupt();
            createActiveThread();
        }
    }

    private void reschedule(CronJob job) {
        job.setScheduledTime();
        this.jobs.add(job);
        createActiveThread();
    }

}
