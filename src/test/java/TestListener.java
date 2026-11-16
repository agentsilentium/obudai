import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestListener extends TestBase implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getDescription();
        if (testName == null || testName.isBlank()) {
            testName = result.getMethod().getMethodName();
        }
        test = extent.createTest(testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test passed:" + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.log(Status.FAIL, "Test failed: " + result.getName() + " : " + result.getThrowable().getMessage());

        // Take screenshot
        Object currentClass = result.getInstance();
        WebDriver driver = ((TestBase) currentClass).driver;

        if (driver != null) {
            String path = captureScreenshot(driver, result.getMethod().getMethodName());
            try {
                test.fail("Screenshot on failure:",
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (Exception e) {
                test.warning("Could not attach screenshot: " + e.getMessage());
            }
        }
    }

    private String captureScreenshot(WebDriver driver, String name) {
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String dest = System.getProperty("user.dir") + "/test-output/screenshots/" + name + ".png";
        try {
            Files.createDirectories(new File(dest).getParentFile().toPath());
            Files.copy(src.toPath(), new File(dest).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }

    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) { extent.flush(); }
}
