/*
    TODO Revisit
    do I need to parse Cron Expressions or just time(hours minutes etc) for scheduling frequency
    single run expected interval meaning? run once after delay? interrupt after interval?

    TODO Implementation
    implement own concurrent scheduler instead of pre-made
        single run expected interval (revisit)
    track and log task metrics/outputs
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
        scheduler.schedule("JobA", "3s", SampleJobs::sampleA);
        scheduler.schedule("JobB", "1s", SampleJobs::sampleB);
        scheduler.schedule("JobC", "5s", SampleJobs::sampleC);
    }
}