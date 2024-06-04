package test.cron;

import main.cron.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class LoggerTest {
    public static final String loggerFileName = "logger-test-log.txt";

    public static void formatMillis_sendValid_ReturnTime() {
        System.out.println("\n\nformatMillis_sendValid_ReturnTime");
        Logger logger = new Logger(loggerFileName);
        String expected = "00:00:08:00";

        String res = logger.formatMillis(8000);

        if (!res.equals(expected)) {
            System.out.println("Test Failed");
            System.out.println("Expected: " + expected + "Got: " + res);
        } else {
            System.out.println("Test Passed");
        }
    }

    public static void formatMillis_sendInvalid_ReturnString() {
        System.out.println("\n\nformatMillis_sendInvalid_ReturnNull");
        Logger logger = new Logger(loggerFileName);

        String res = logger.formatMillis(-1);

        if (res.equals("Invalid Time")) {
            System.out.println("Test Passed");
        } else {
            System.out.println("Test Failed");
        }
    }

    public static void log_validInput_WritesToFile() {
        System.out.println("\n\nlog_validInput_WritesToFile");
        Logger logger = new Logger(loggerFileName);
        String jobID = "123";
        long runTime = 8000;
        String message = "Test Message";

        logger.log(jobID, runTime, message);
        try {
            logger.close();
            File file = new File(loggerFileName);
            Scanner scanner = new Scanner(file);
            String res = "";
            while (scanner.hasNextLine()) {
                res = scanner.nextLine();
            }
            res = res.trim();
            scanner.close();
//            file.delete(); // doesn't delete
            if (res.contains(jobID)) {
                System.out.println("Test Passed");
            } else {
                System.out.println("Test Failed");
            }
        } catch (IOException e) {
            System.out.println("Test Failed: " + e.getMessage());
        }
    }
}
