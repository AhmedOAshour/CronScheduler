package test.cron;

import main.cron.ClassReader;

import java.util.concurrent.Callable;

public class ClassReaderTest {
    public static void testReadFunctionValid() {
        System.out.println("\n\ntestReadFunctionValid");
        ClassReader classReader = new ClassReader();

        // TODO Ensure that the test class and method exist before running this test
        //  fails when calling class within project directory
        String filePath = "D:\\";
        String fileName = "SampleJobs.java";
        String methodName = "sampleA";

        Callable<Object> callable = classReader.readFunction(filePath, fileName, methodName);

        if (callable != null) {
            try {
                Object result = callable.call();
                System.out.println("Test Passed: " + result);
            } catch (Exception e) {
                System.out.println("Test Failed: Exception while invoking method");
            }
        } else {
            System.out.println("Test Failed: Callable is null");
        }
    }
}
