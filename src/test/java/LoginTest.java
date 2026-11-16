import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.*;
import obudai.pages.LoginPage;
import obudai.pages.InventoryPage;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;

    @BeforeMethod
    public void startBrowser() {
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
    }

    @Test
    public void validUserShouldLogin() {
        loginPage.openPage();
        loginPage.login("standard_user", "secret_sauce");

        assertThat(inventoryPage.getTitle()).isEqualTo("Products");
    }

    @Test
    public void invalidUserShouldShowError() {
        loginPage.openPage();
        loginPage.login("locked_out_user", "secret_sauce");

        assertThat(loginPage.isErrorDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessage()).contains("locked out");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
