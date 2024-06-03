/*
    TODO Implementation
    implement own concurrent scheduler instead of pre-made
        DONE Multiple jobs can be running at the same time.
        single run expected interval: interrupt after interval
            Interrupter thread?
        Done handle throws
    Done track and log task metrics/outputs
    testing
 */

/*
    Cron Expression min(0-59) hour(0-23) DayofMonth(1-31) Month(1-12) DayofWeek(0-6, 0 and 7 are Sunday)
    * matches every value in range
    , separates multiple values
    - defines range
    / defines interval

    function on own thread input timer call's job, recursive on itself
*/

import cron.CronScheduler;

public class Main {

    public static void main(String[] args) {
        CronScheduler scheduler = new CronScheduler();
        scheduler.queueJob("JobA", "3s", SampleJobs::sampleA);
        scheduler.queueJob("JobB", "1s", SampleJobs::sampleB);
        scheduler.queueJob("JobC", "5s", SampleJobs::sampleC);
    }
}