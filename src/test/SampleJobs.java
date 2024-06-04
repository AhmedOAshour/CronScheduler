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
        return "Sample B";
    }

   public static boolean sampleC(){
       return true;
   }
}
