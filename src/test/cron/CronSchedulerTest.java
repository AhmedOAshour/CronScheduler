package test.cron;

import main.cron.CronScheduler;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class CronSchedulerTest {
    public static final String loggerFileName = "scheduler-test-log.txt";

    public static void testScheduleJob() {
        System.out.println("\n\ntestScheduleJob");
        CronScheduler scheduler = new CronScheduler(loggerFileName);

        Callable<Object> task = () -> "testScheduleJobResult";

        scheduler.queueJob("job1", "1s", "5s", task);

        try {
            Thread.sleep(1000); // wait for job to execute
        } catch (InterruptedException e) {
            System.out.println("Test Failed: Test interrupted");
        }
        scheduler.interruptActiveThreads();
        scheduler.stopScheduler();

        try {
            File file = new File(loggerFileName);
            Scanner scanner = new Scanner(file);
            String res = "";
            while (scanner.hasNextLine()) {
                res = scanner.nextLine();
            }
            res = res.trim();
            scanner.close();
            if (res.contains("testScheduleJobResult")) {
                System.out.println("Test Passed");
            } else {
                System.out.println("Test Failed");
            }
        } catch (IOException e) {
            System.out.println("Test Failed: " + e.getMessage());
        }
    }
}