import java.util.concurrent.*;
import java.util.Random;

public class ParallelReportSystem {

    public static void main(String[] args) throws InterruptedException {
        int totalReports = 8;
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(totalReports);

        System.out.println("Starting Report Generation");
        for (int i = 1; i <= totalReports; i++) {
            String reportId ="Report-"+ i;
            executor.submit(new ReportTask(reportId, latch));
        }
        System.out.println("Main thread is waiting for all reports to complete");
        latch.await();// Main thread blocked here
        System.out.println("All reports generated. Dashboard is ready.");
        executor.shutdown();
    }
}
class ReportTask implements Runnable {

    private String reportId;
    private CountDownLatch latch;
    private Random random= new Random();
    public ReportTask(String reportId,CountDownLatch latch) {
        this.reportId =reportId;
        this.latch = latch;
    }
    @Override
    public void run() {
        try {
            System.out.println(reportId + " started by " + Thread.currentThread().getName());
            int processingTime = 2000 + random.nextInt(2000);
            Thread.sleep(processingTime);
            System.out.println(reportId + " completed by" + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();  // Decrease latch count
        }
    }
}
