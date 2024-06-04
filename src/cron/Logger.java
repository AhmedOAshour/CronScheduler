package cron;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Semaphore;

public class Logger {
    private final Semaphore semaphore;
    private FileWriter writer;
    DateTimeFormatter formatter;

    public Logger() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        semaphore = new Semaphore(1);
        try {
            writer = new FileWriter("scheduler-log.txt", true);
        }
        catch (IOException e) {
            System.err.println("Could not open file for logging");
            System.err.println(e.getMessage());
        }
    }

    public String formatMillis(long millis) {
        Duration duration = Duration.ofMillis(millis);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        int millisPart = duration.toMillisPart();
        return String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, millisPart);
    }

    public void log(String jobID, long runTime, String message) {
        String output = String.format("JobId: %s --- DateTime: %s --- Runtime: %ss --- Output: %s\n",
                jobID, formatter.format(LocalDateTime.now()), formatMillis(runTime), message);
        try {
            semaphore.acquire();
            writer.write(output);
        }
        catch (InterruptedException e) {
            System.err.println("Interrupted while writing to file");
        }
        catch (IOException e) {
            System.err.println("Could not write to logs to file");
            System.err.println(e.getMessage());
        }
        catch (Exception e) {
            System.err.println(output);
        }
        finally {
            semaphore.release();
        }
    }

    public void close() throws IOException {
        writer.close();
    }
}
