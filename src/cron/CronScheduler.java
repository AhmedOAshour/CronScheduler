package cron;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CronScheduler {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public static final Map<String, Future<?>> jobs = new HashMap<>();

    private static long parseInterval(String interval) {
        if (interval.length()>2)
            throw new IllegalArgumentException("Invalid interval format");

        long value = Long.parseLong(interval.substring(0, interval.length() - 1));
        return switch (interval.charAt(interval.length() - 1)) {
            case 's' -> value * 1000;
            case 'm' -> value * 60 * 1000;
            case 'h' -> value * 60 * 60 * 1000;
            default -> throw new IllegalArgumentException("Invalid interval format");
        };
    }

    public static void schedule(String jobId, String time, boolean singleRun, Runnable task) {
        long timeValue = parseInterval(time);
        CronJob job = new CronJob(jobId, timeValue, singleRun, () -> {
            task.run();
            if(singleRun)
                cancel(jobId);
        });
        Future<?> scheduledJob;

        if (singleRun){
            scheduledJob = scheduler.schedule(job, timeValue, TimeUnit.MILLISECONDS);
        }
        else {
            scheduledJob = scheduler.scheduleAtFixedRate(job, 0, timeValue, TimeUnit.MILLISECONDS);
        }
        jobs.put(jobId, scheduledJob);
    }


    public static void cancel(String jobId) {
        Future<?> removedJob = jobs.remove(jobId);
        if (removedJob != null) {
            removedJob.cancel(true);
        }
        if (jobs.isEmpty()) {
            scheduler.shutdownNow();
        }
    }

}
