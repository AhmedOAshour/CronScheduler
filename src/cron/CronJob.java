package cron;

public class CronJob implements Runnable {

    private final String jobId;
    private final long time;
    private final boolean singleRun;
    private final Runnable task;

    public CronJob(String jobId, long time, boolean singleRun, Runnable task) {
        this.jobId = jobId;
        this.time = time;
        this.singleRun = singleRun;
        this.task = task;
    }

    @Override
    public void run() {
        task.run();
    }

    @Override
    public String toString() {
        return "cron.CronJob{" +
                "jobId='" + jobId + '\'' +
                ", time=" + time +
                ", singleRun=" + singleRun +
                ", task=" + task +
                '}';
    }
}
