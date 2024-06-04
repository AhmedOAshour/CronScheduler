package test.cron;

import main.cron.CronJob;

public class CronJobTest {

    public static void testCronJobInitialization_InvalidInput_ThrowsIllegalArgumentException() {
        System.out.println("\n\ntestCronJobInitialization_InvalidInput_ThrowsIllegalArgumentException");

        try {
            CronJob cronJob = new CronJob("job1", "10s", "5s", null);
        } catch (IllegalArgumentException e) {
            System.out.println("Test Passed");
            return;
        }
        System.out.println("Test Failed");
    }

    public static void testParseTime_ValidInput_ReturnsLong() {
        System.out.println("\n\ntestParseTime_ValidInput_ReturnsLong");
        CronJob cronJob = new CronJob("job1", "10s", "5s", () -> null);

        if (cronJob.parseTime("10s") != 10000) {
            System.out.println("Test Failed: Parse Time for seconds does not match");
        } else if (cronJob.parseTime("10m") != 600000) {
            System.out.println("Test Failed: Parse Time for minutes does not match");
        } else if (cronJob.parseTime("1h") != 3600000) {
            System.out.println("Test Failed: Parse Time for hours does not match");
        } else {
            System.out.println("Test Passed");
        }
    }

    public static void testParseTime_SendInvalid_ThrowsIllegalArgumentException() {
        System.out.println("\n\ntestParseTime_SendInvalid_ThrowsIllegalArgumentException");
        CronJob cronJob = new CronJob("job1", "10s", "5s", () -> null);

        try {
            cronJob.parseTime("10x");
            System.out.println("Test Failed: Invalid time format did not throw exception");
        } catch (IllegalArgumentException e) {
            System.out.println("Test Passed");
        }
    }

    public static void testCompareTo() {
        System.out.println("\n\ntestCompareTo");
        CronJob cronJob1 = new CronJob("job1", "10s", "5s", () -> null);
        CronJob cronJob2 = new CronJob("job2", "5s", "5s", () -> null);

        if (cronJob1.compareTo(cronJob2) <= 0) {
            System.out.println("Test Failed: CompareTo does not work as expected");
        } else {
            System.out.println("Test Passed");
        }
    }

    public static void testSetScheduledTime() {
        System.out.println("\n\ntestSetScheduledTime");
        CronJob cronJob = new CronJob("job1", "10s", "5s", () -> null);
        long previousScheduledTime = cronJob.getScheduledTime();

        cronJob.setScheduledTime();
        long newScheduledTime = cronJob.getScheduledTime();

        if (newScheduledTime < previousScheduledTime) {
            System.out.println("Test Failed: SetScheduledTime does not work as expected");
        } else {
            System.out.println("Test Passed");
        }
    }
}
