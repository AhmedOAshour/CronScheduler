package cron;

import java.util.concurrent.Callable;

public class CronJob implements Comparable<CronJob>  {

    private final String jobId;
    private final long runInterval;
    private long scheduledTime;
    private final Callable<Object> task;

    public CronJob(String jobId, String runInterval, Callable<Object> task) {
        if (jobId == null || jobId.isBlank() || task == null) {
            throw new IllegalArgumentException("All fields are required");
        }
        this.jobId = jobId;
        this.runInterval = parseTime(runInterval);
        this.task = task;
        this.setScheduledTime();
    }

    public int compareTo(CronJob o) {
        return Long.compare(this.scheduledTime, o.scheduledTime);
    }

    public String getJobId() {
        return jobId;
    }

    public long getRunInterval() {
        return runInterval;
    }

    public long getScheduledTime() {
        return scheduledTime;
    }

    public Callable<Object> getTask() {
        return task;
    }

    public void setScheduledTime() {
        this.scheduledTime = System.currentTimeMillis() + runInterval;
    }

    private long parseTime(String time) {
        if (time.length()<2 || !Character.isDigit(time.charAt(0)))
            throw new IllegalArgumentException("Invalid interval format");

        long value = Long.parseUnsignedLong(time.substring(0, time.length() - 1));
        return switch (time.charAt(time.length() - 1)) {
            case 's' -> value * 1000;
            case 'm' -> value * 60 * 1000;
            case 'h' -> value * 60 * 60 * 1000;
            default -> throw new IllegalArgumentException("Invalid time format");
        };
    }

    @Override
    public String toString() {
        return "CronJob{" +
                "jobId='" + jobId + '\'' +
                ", runInterval=" + runInterval +
                ", scheduledTime=" + scheduledTime +
                ", task=" + task +
                '}';
    }
}
