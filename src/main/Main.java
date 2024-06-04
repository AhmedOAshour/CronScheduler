package main;
/*
    TODO Implementation
    DONE implement own concurrent scheduler instead of pre-made
        DONE Multiple jobs can be running at the same time.
        DONE single run expected interval: interrupt after interval
        DONE handle throws
        update/replace job
        unschedule job
    DONE read jobs from file
    DONE track and log task metrics/outputs
    DONE main method (cli)
    testing
    README
 */

import main.cron.ClassReader;
import main.cron.CronScheduler;

import java.util.Scanner;
import java.util.concurrent.Callable;

public class Main {

    public static void main(String[] args) {
        CronScheduler scheduler = new CronScheduler();
        ClassReader classReader = new ClassReader();
//        Callable<Object> callable = classReader.readFunction("D:\\VSCode Projects\\", "SampleJobs.java", "sampleA");
//        Callable<Object> callable1 = classReader.readFunction("D:\\VSCode Projects\\", "SampleJobs.java", "sampleB");
//        Callable<Object> callable2 = classReader.readFunction("D:\\VSCode Projects\\", "SampleJobs.java", "sampleC");
//        scheduler.queueJob("JobA", "1s", "1s", callable);
//        scheduler.queueJob("JobB", "3s", "5s", callable1);
//        scheduler.queueJob("JobC", "3s", "5s", callable2);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n\nEnter command: ");
            System.out.println("To schedule new job enter 1");
            System.out.println("To exit program enter 0\n");
            String command = sc.nextLine().trim();
            if (command.equals("1")) {
                System.out.println("Enter file path, Ex: <C:\\folder\\>");
                String filePath = sc.nextLine().trim();
                if (filePath.length()>1 && filePath.charAt(filePath.length() - 1) != '\\') {
                    filePath += "\\";
                }
                else {
                    System.err.println("Invalid file path");
                    continue;
                }
                System.out.println("Enter file name, Ex: <Class.java>");
                String fileName = sc.nextLine().trim();
                System.out.println("Enter methodName name, Ex: <methodName>");
                String methodName = sc.nextLine().trim();
                System.out.println("Enter JobId");
                String jobId = sc.nextLine();
                System.out.println("Enter job scheduling frequency, Ex: <5s> or <1m> or <1h>");
                String frequency = sc.nextLine().trim();
                System.out.println("Enter max time to run job, Ex: <5s> or <1m> or <1h>");
                String maxTime = sc.nextLine().trim();
                Callable<Object> callable = classReader.readFunction(filePath, fileName, methodName);
                if (callable == null) {
                    System.out.println("Couldn't create job, try again");
                    continue;
                }
                scheduler.queueJob(jobId, frequency, maxTime, callable);
            }
            if (command.equals("0")) {
                scheduler.stopScheduler();
                break;
            }
        }

    }
}