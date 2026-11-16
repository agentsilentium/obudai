package obudai.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setReportName("SauceDemo Automation Report");
            spark.config().setDocumentTitle("SauceDemo Test Results");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Tester", "Obudai Selenium Class");
        }
        return extent;
    }
}
