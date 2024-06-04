# Concurrent Job Scheduler
## Overview

This Java project implements a custom concurrent job scheduler that allows multiple jobs to be executed concurrently at specified intervals. Jobs are dynamically loaded and compiled from external Java files and can be scheduled via a command-line interface.

## Reasoning Behind Technical Decisions

- Custom Concurrent Scheduler: Instead of using a pre-built library, we implemented our own scheduler to have full control over job execution and management, ensuring it meets our specific requirements.


- Dynamic Class Loading: The ClassReader allows dynamic loading and compilation of Java classes from specified file paths, enabling flexible job definitions.


- Concurrency Handling: The scheduler leverages a combination of threads, semaphores, and priority queues to manage job execution efficiently while maintaining thread safety.


- Logging: The Logger class logs job execution details, providing transparency and traceability of job metrics and outputs.


- Daemon Jobs: Scheduled jobs run on daemon threads, as not to keep the program running after the scheduler has been stopped. 

## Trade-offs

- Complexity: Implementing a custom scheduler adds complexity and requires more extensive testing compared to using a pre-built library.


- Resource Management: Managing threads manually can lead to resource management issues if not handled properly, especially with long-running or high-frequency jobs.


- Error Handling: Custom error handling mechanisms are necessary to ensure robustness, which increases the codebase size and complexity.


- Runaway Threads: Dynamically loaded custom jobs must be interruptible, otherwise runaway threads cannot be stopped. 

## Possible Future Improvements

- Job Update and Removal: Implement functionality to update and unschedule jobs dynamically.


- Enhanced Error Handling: Improve error handling mechanisms, especially around job execution failures and class loading issues.


- Extended Logging: Include more detailed logs, such as job start and end times, and handle log rotation.


- GUI : Develop a GUI for easier job management and monitoring.


- Configuration Management: Introduce a configuration file for managing scheduler settings.


- Unit Testing and Bug Fixes: Implement more comprehensive tests, and fix any bugs that might arise while testing.

## Examples

### [Main.java](src%2Fmain%2FMain.java) /  [CronScheduler.jar](out%2Fartifacts%2FCronSchedulerRunnable_jar%2FCronScheduler.jar)
### Example 1:
User adds a job.\
User adds an infinite loop, interruptible job.\
Job gets interrupted when it outlives its max run time.
User exits program
    
### Console Output


    Enter command: 
    To schedule new job enter 1
    To exit program enter 0
    
    1
    Enter file path, Ex: <C:\folder\>
    File must be outside of project directory.
    D:\VSCode Projects\
    Enter file name, Ex: <Class.java>
    SampleJobs.java
    Enter methodName name, Ex: <methodName>
    sampleB
    Enter JobId
    JobB
    Enter job scheduling frequency, Ex: <5s> or <1m> or <1h>
    5s
    Enter max time to run job, Ex: <5s> or <1m> or <1h>
    5s
    JobB: Job scheduled
    
    
    Enter command:
    To schedule new job enter 1
    To exit program enter 0
    
    1
    Enter file path, Ex: <C:\folder\>
    File must be outside of project directory.
    D:\VSCode Projects\
    Enter file name, Ex: <Class.java>
    SampleJobs.java
    Enter methodName name, Ex: <methodName>
    sampleA
    Enter JobId
    jobA
    Enter job scheduling frequency, Ex: <5s> or <1m> or <1h>
    5s
    Enter max time to run job, Ex: <5s> or <1m> or <1h>
    2s
    jobA: Job scheduled
    
    
    Enter command:
    To schedule new job enter 1
    To exit program enter 0
    
    jobA exceeded max runTime, interrupting thread
    Task failed to run, removing it from schedule
    jobA exceeded max runTime, interrupting thread
    Message: null
    0 
    Could not write to logs to file
    Stream closed


### Log Output

    JobId: JobB --- DateTime: 2024-06-05 01:25:21 --- Runtime: Invalid Time --- Output: Sample B
    JobId: JobB --- DateTime: 2024-06-05 01:25:26 --- Runtime: Invalid Time --- Output: Sample B
    JobId: JobB --- DateTime: 2024-06-05 01:25:31 --- Runtime: Invalid Time --- Output: Sample B
    JobId: JobB --- DateTime: 2024-06-05 01:25:36 --- Runtime: Invalid Time --- Output: Sample B
    JobId: JobB --- DateTime: 2024-06-05 01:25:41 --- Runtime: Invalid Time --- Output: Sample B

### Example 2:

User adds a job.
User adds another job.
User exits program.

### Console Output

    Enter command:
    To schedule new job enter 1
    To exit program enter 0
    
    1
    Enter file path, Ex: <C:\folder\>
    File must be outside of project directory.
    D:\VSCode Projects\
    Enter file name, Ex: <Class.java>
    SampleJobs.java
    Enter methodName name, Ex: <methodName>
    sampleB
    Enter JobId
    JobB
    Enter job scheduling frequency, Ex: <5s> or <1m> or <1h>
    5s
    Enter max time to run job, Ex: <5s> or <1m> or <1h>
    5s
    JobB: Job scheduled
    
    
    Enter command:
    To schedule new job enter 1
    To exit program enter 0
    
    1
    Enter file path, Ex: <C:\folder\>
    File must be outside of project directory.
    D:\VSCode Projects\
    Enter file name, Ex: <Class.java>
    SampleJobs.java
    Enter methodName name, Ex: <methodName>
    sampleC
    Enter JobId
    JobC
    Enter job scheduling frequency, Ex: <5s> or <1m> or <1h>
    2s
    Enter max time to run job, Ex: <5s> or <1m> or <1h>
    5s
    JobC: Job scheduled
    
    
    Enter command:
    To schedule new job enter 1
    To exit program enter 0
    
    0


### Log Output

    JobId: JobB --- DateTime: 2024-06-05 01:36:47 --- Runtime: 00:00:00:00 --- Output: Sample B
    JobId: JobB --- DateTime: 2024-06-05 01:36:52 --- Runtime: 00:00:00:00 --- Output: Sample B
    JobId: JobC --- DateTime: 2024-06-05 01:36:52 --- Runtime: 00:00:00:03 --- Output: true
    JobId: JobC --- DateTime: 2024-06-05 01:36:57 --- Runtime: 00:00:00:00 --- Output: true
    JobId: JobB --- DateTime: 2024-06-05 01:36:57 --- Runtime: 00:00:00:00 --- Output: Sample B
    JobId: JobC --- DateTime: 2024-06-05 01:36:59 --- Runtime: 00:00:00:00 --- Output: true
    JobId: JobC --- DateTime: 2024-06-05 01:37:02 --- Runtime: 00:00:00:00 --- Output: true


### [MainTest.java](src%2Ftest%2FMainTest.java) / [CronScheduler.jar](out%2Fartifacts%2FCronScheduleTestRunnable_jar%2FCronScheduler.jar)

### Example 3
TestClass run.


    formatMillis_sendValid_ReturnTime
    Test Passed
    
    
    formatMillis_sendInvalid_ReturnNull
    Test Passed
    
    
    log_validInput_WritesToFile
    Test Passed
    
    
    testCronJobInitialization_InvalidInput_ThrowsIllegalArgumentException
    Test Passed
    
    
    testParseTime_ValidInput_ReturnsLong
    Test Passed
    
    
    testParseTime_SendInvalid_ThrowsIllegalArgumentException
    Test Passed
    
    
    testCompareTo
    Test Passed
    
    
    testSetScheduledTime
    Test Passed
    
    
    testReadFunctionValid
    Sample A
    Test Passed: 200
    
    
    testScheduleJob
    job1: Job scheduled
    Could not write to logs to file
    Stream closed
    Test Passed
    
    Process finished with exit code 0


### Scheduler Test Log Output
    JobId: job1 --- DateTime: 2024-06-05 01:33:34 --- Runtime: Invalid Time --- Output: testScheduleJobResult

### Logger Test Log Output
    JobId: 123 --- DateTime: 2024-06-05 01:33:33 --- Runtime: 00:00:08:00 --- Output: Test Message
