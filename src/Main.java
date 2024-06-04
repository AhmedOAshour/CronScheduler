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

import cron.ClassReader;
import cron.CronScheduler;

import java.util.concurrent.Callable;

public class Main {

    public static void main(String[] args) {
        CronScheduler scheduler = new CronScheduler();
        ClassReader classReader = new ClassReader();
        Callable<Object> callable = classReader.readFunction("D:\\VSCode Projects\\", "SampleJobs.java", "sampleA");
        Callable<Object> callable1 = classReader.readFunction("D:\\VSCode Projects\\", "SampleJobs.java", "sampleB");
        Callable<Object> callable2 = classReader.readFunction("D:\\VSCode Projects\\", "SampleJobs.java", "sampleC");
        scheduler.queueJob("JobA", "1s", "1s", callable);
        scheduler.queueJob("JobB", "3s", "5s", callable1);
        scheduler.queueJob("JobC", "3s", "5s", callable2);

    }
}