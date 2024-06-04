package test;

public class SampleJobs {
   public static int sampleA() throws InterruptedException {
       int count = 0;
       while (!Thread.currentThread().isInterrupted()) {
           count += 1;
           Thread.sleep(1000);
       }
       return count;
   }

    public static String sampleB(){
        System.out.println("Sample B");
        return "Sample B";
    }

   public static boolean sampleC(){
       System.out.println("Sample C");
       return true;
   }
}
