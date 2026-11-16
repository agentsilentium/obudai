
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import obudai.pages.CartPage;
import obudai.pages.LoginPage;
import obudai.pages.ProductsPage;

import static org.assertj.core.api.Assertions.assertThat;

public class SauceDemoTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;

    @BeforeClass
    public void setUp() {
        driver = new EdgeDriver();
        driver.manage().window().maximize();

        // Instantiate page objects
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        cartPage = new CartPage(driver);
    }

    // @Test(priority = 1)
    // public void testSuccessfulLogin() {
    //     loginPage.open();
    //     loginPage.login("standard_user", "secret_sauce");

    //     String title = productsPage.getTitle();
    //     Assert.assertEquals(title, "Products", "User should be navigated to Products page after login.");
    //     System.out.println("✅ Login successful, on Products page.");
    // }

    // @Test(priority = 2, dependsOnMethods = "testSuccessfulLogin")
    // public void testAddToCartAndCheckout() {
    //     productsPage.addFirstProductToCart();
    //     productsPage.goToCart();

    //     int count = cartPage.getCartItemCount();
    //     Assert.assertEquals(count, 1, "Cart should contain exactly one item.");

    //     System.out.println("✅ 1 item successfully added to cart.");

    //     cartPage.proceedToCheckout();
    //     System.out.println("🛒 Proceeded to checkout page.");
    // }

    @Test(priority = 2, dependsOnMethods = "testSuccessfulLogin")
    public void testAddToCartAndCheckout2() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait until the first Add to Cart button is clickable
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".inventory_item button")));

        // Add first item to cart
        productsPage.addFirstProductToCart();

        // Wait until the cart icon is visible and clickable
        wait.until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link")));

        // Go to cart
        productsPage.goToCart();

        // Wait until at least one item is present in the cart
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.className("cart_item"), 0));

        // Verify cart contents
        int count = cartPage.getCartItemCount();
        Assert.assertEquals(count, 1, "Cart should contain exactly one item.");
        System.out.println("✅ 1 item successfully added to cart.");

        assertThat(count)
        .as("Check cart contains exactly 1 item after adding product")
        .isEqualTo(1);

        // Wait until the checkout button is clickable
        wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout")));

        // Proceed to checkout
        cartPage.proceedToCheckout();
        System.out.println("🛒 Proceeded to checkout page.");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}