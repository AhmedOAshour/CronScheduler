/*
    TODO Revisit
    do I need to parse Cron Expressions or just time(hours minutes etc) for scheduling frequency
    single run expected interval meaning? run once after delay?
    keep scheduler static?

    TODO Implementation
    implement own concurrent scheduler instead of pre-made
    track and log task metrics/outputs
    testing
 */

/*
    Cron Expression min(0-59) hour(0-23) DayofMonth(1-31) Month(1-12) DayofWeek(0-6, 0 and 7 are Sunday)
    * matches every value in range
    , separates multiple values
    - defines range
    / defines interval
*/

import cron.CronScheduler;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        CronScheduler.schedule( "jobA","1s", true, SampleJobs::sampleA);
        CronScheduler.schedule( "jobB","3s", false, SampleJobs::sampleB);
//        cron.CronScheduler.schedule( "jobC","5s", false, SampleJobs::sampleC);

        System.out.println(CronScheduler.jobs.keySet());
        System.out.println(CronScheduler.jobs.values());



        Thread.sleep(5000);
        cron.CronScheduler.cancel("jobB");

        System.out.println(cron.CronScheduler.jobs.keySet());
        System.out.println(cron.CronScheduler.jobs.values());
    }
}