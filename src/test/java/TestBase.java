import com.aventstack.extentreports.*;

import obudai.utils.ExtentManager;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.*;

public abstract class TestBase {

    protected WebDriver driver;
    protected static ExtentReports extent;
    protected static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    protected ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        extent = ExtentManager.getInstance();
    }

    @BeforeMethod
    public void setupDriver() {
        driver = new EdgeDriver();
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDownDriver() {
        if (driver != null) driver.quit();
    }

    @AfterSuite
    public void tearDownReport() {
        extent.flush();
    }
}
