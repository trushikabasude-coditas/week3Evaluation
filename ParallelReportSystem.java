import java.util.concurrent.*;
import java.util.Random;

public class ParallelReportSystem {

    public static void main(String[] args) throws InterruptedException {

        // Total reports
        int totalReports = 8;

        // Fixed Thread Pool of size 2
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // CountDownLatch initialized with 8
        CountDownLatch latch = new CountDownLatch(totalReports);

        System.out.println("Starting Report Generation...\n");

        // Creating 8 report tasks
        for (int i = 1; i <= totalReports; i++) {
            String reportId = "Report-" + i;
            executor.submit(new ReportTask(reportId, latch));
        }

        // Main thread waiting
        System.out.println("Main thread is waiting for all reports to complete...\n");
        latch.await();  // Main thread blocked here

        // Final message
        System.out.println("\nAll reports generated. Dashboard is ready.");

        executor.shutdown();
    }
}

// Report Task Class
class ReportTask implements Runnable {

    private String reportId;
    private CountDownLatch latch;
    private Random random = new Random();

    public ReportTask(String reportId, CountDownLatch latch) {
        this.reportId = reportId;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            System.out.println(reportId + " started by " + Thread.currentThread().getName());

            // Simulate processing delay (2–4 seconds)
            int processingTime = 2000 + random.nextInt(2000);
            Thread.sleep(processingTime);

            System.out.println(reportId + " completed by " + Thread.currentThread().getName());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();  // Decrease latch count
        }
    }
}