import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.*;
import obudai.pages.LoginPage;
import obudai.pages.InventoryPage;
import obudai.pages.CartPage;
import obudai.pages.CheckoutPage;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CheckoutPage checkoutPage;

    @BeforeMethod
    public void setUp() {
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
        loginPage.openPage();
        inventoryPage = loginPage.login("standard_user", "secret_sauce");
    }

    @Test
    public void userCanAddItemAndCheckout() {
        // Add item
        inventoryPage.addFirstItemToCart();
        assertThat(inventoryPage.getCartCount()).isEqualTo("1");

        // Navigate to cartpage
        CartPage cartPage = inventoryPage.goToCart();
        assertThat(cartPage.getCartItemCount()).isEqualTo(1);

        // Proceed to checkout
        // clickCheckout returns the destination page object.
        checkoutPage = cartPage.clickCheckout();

        // These inherited and page-specific methods retain CheckoutPage as the type.
        checkoutPage.fillCustomerInfo("John", "Doe", "12345")
                .finishOrder();

        // Assert confirmation
        assertThat(checkoutPage.getConfirmationMessage())
                .as("Check order completion message")
                .isEqualTo("Thank you for your order!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
