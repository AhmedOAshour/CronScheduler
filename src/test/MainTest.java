package test;

import test.cron.CronJobTest;
import test.cron.CronSchedulerTest;
import test.cron.LoggerTest;
import test.cron.ClassReaderTest;

import java.io.File;
import java.io.IOException;

// Unit Tests
public class MainTest {
    public static void main(String[] args) throws IOException {
        LoggerTest.formatMillis_sendValid_ReturnTime();
        LoggerTest.formatMillis_sendInvalid_ReturnString();
        LoggerTest.log_validInput_WritesToFile();


        CronJobTest.testCronJobInitialization_InvalidInput_ThrowsIllegalArgumentException();
        CronJobTest.testParseTime_ValidInput_ReturnsLong();
        CronJobTest.testParseTime_SendInvalid_ThrowsIllegalArgumentException();
        CronJobTest.testCompareTo();
        CronJobTest.testSetScheduledTime();

        ClassReaderTest.testReadFunctionValid();

        CronSchedulerTest.testScheduleJob();
    }
}
