/*
    TODO Implementation
    DONE implement own concurrent scheduler instead of pre-made
        DONE Multiple jobs can be running at the same time.
        DONE single run expected interval: interrupt after interval
        DONE handle throws
    read jobs from file
    DONE track and log task metrics/outputs
    testing
 */

import cron.CronScheduler;

public class Main {

    public static void main(String[] args) {
        CronScheduler scheduler = new CronScheduler();
        scheduler.queueJob("JobA", "2s", "1s", SampleJobs::sampleA);
        scheduler.queueJob("JobB", "3s", "5s", SampleJobs::sampleB);
        scheduler.queueJob("JobC", "3s", "5s", SampleJobs::sampleC);

    }
}