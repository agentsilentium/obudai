import org.testng.annotations.*;
import org.testng.annotations.Listeners;
import obudai.pages.*;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(TestListener.class)
public class LoginAndCartTests extends TestBase {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    @BeforeMethod
    public void initPages() {
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
    }

    @Test(description = "Login with valid credentials")
    public void successfulLoginShouldOpenInventory() {
        inventoryPage = loginPage.openPage()
                .login("standard_user", "secret_sauce");
        assertThat(inventoryPage.getTitle()).isEqualTo("Products");
    }

    @Test(description = "Login with invalid credentials shows error")
    public void invalidLoginShouldShowError() {
        loginPage.openPage();
        loginPage.login("standard_user", "wrong_password");
        assertThat(loginPage.isErrorDisplayed()).isTrue();
        assertThat(loginPage.getErrorMessage())
                .containsIgnoringCase("Username and password do not match");
    }

    @Test(description = "Add items to cart and verify count")
    public void addMultipleItemsToCart() {
        inventoryPage = loginPage.openPage()
                .login("standard_user", "secret_sauce");

        inventoryPage.addFirstItemToCart()
                .addSecondItemToCart();

        cartPage = inventoryPage.goToCart();
        assertThat(cartPage.getCartItemCount()).isEqualTo(4);
    }
}
