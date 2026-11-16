import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DemoTest {
    private WebDriver driver;

    // @BeforeClass
    // public void setUp() {
    // driver = new EdgeDriver();
    // driver.manage().window().maximize();
    // }

    // BeforeMethod! to make it standalone

    @DataProvider(name = "loginUsers")
    public Object[][] loginUsers() {
        return new Object[][] {
                { "standard_user", "secret_sauce", true }, // valid login
                { "locked_out_user", "secret_sauce", false }, // should fail
                { "visual_user", "secret_sauce", true },
                { "performance_glitch_user", "secret_sauce", true },
                { "problem_user", "secret_sauce", true }
        };
    }

    // private class UserScenario {
    //     private String username;
    //     private String password;
    //     private boolean expectedSuccess;

    //     public UserScenario(String username, String password, boolean expectedSuccess) {
    //         this.username = username;
    //         this.password = password;
    //         this.expectedSuccess = expectedSuccess;
    //     }

    //     public String username() {
    //         return username;
    //     }

    //     public String password() {
    //         return password;
    //     }

    //     public boolean expectedSuccess() {
    //         return expectedSuccess;
    //     }
    // }

    //vesd össze: Lombok

    public record UserScenario(String username, String password, boolean expectedSuccess) { }

    @DataProvider(name = "loginUsersCustom")
    public Object[][] loginUsersCustom() {
        return new Object[][] {
                { new UserScenario("standard_user", "secret_sauce", true) }, // valid login
                { new UserScenario("locked_out_user", "secret_sauce", false) }, // should fail
                { new UserScenario("visual_user", "secret_sauce", true) },
                { new UserScenario("performance_glitch_user", "secret_sauce", true) },
                { new UserScenario("problem_user", "secret_sauce", true) }
        };
    }

    @Test(dataProvider = "loginUsers")
    public void testLoginWithMultipleUsers(String username, String password, boolean expectedSuccess) {
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
        if (expectedSuccess) {
            String title = driver.findElement(By.className("title")).getText();
            Assert.assertEquals(title, "Products", "User should be navigated to Products page after login.");
        } else {
            String error = driver.findElement(By.xpath("//h3[@data-test=\"error\"]")).getText();
            Assert.assertEquals(error, "Epic sadface: Sorry, this user has been locked out.");
        }
        driver.quit();
    }

    @Test(dataProvider = "loginUsersCustom")
    public void testLoginWithMultipleUsersCustom(UserScenario userScenario) {
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys(userScenario.username());
        driver.findElement(By.id("password")).sendKeys(userScenario.password());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("login-button")))).click();
        // driver.findElement(By.id("login-button")).click();
        if (userScenario.expectedSuccess()) {
            String title = driver.findElement(By.className("title")).getText();
            Assert.assertEquals(title, "Products", "User should be navigated to Products page after login.");
        } else {
            String error = driver.findElement(By.xpath("//h3[@data-test=\"error\"]")).getText();
            Assert.assertEquals(error, "Epic sadface: Sorry, this user has been locked out.");
        }
        driver.quit();
    }

    @Test(priority = 1)
    public void testSuccessfulLogin() {
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // User should be navigated to Products page after login."
        String title = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(title, "Products", "User should be navigated to Products page after login.");
        // még requirement? legyenek kint productok
        List<WebElement> products = driver.findElements(By.xpath("//div[@data-test=\"inventory-item\"]"));
        Assert.assertTrue(products.size() > 0, "There are no products displayed");
        Assert.assertFalse(products.isEmpty(), "There are no products displayed");
        // problem? ismétlődés , nem lép ki, ha nem fut le, etc
        driver.quit();
    }

    // legyenek függetlenek, vagy sem, külön futtatás
    @Test(priority = 2, dependsOnMethods = "testSuccessfulLogin")
    public void testAddToCartAndCheckout() {
        driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // User should be navigated to Products page after login."
        String title = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(title, "Products", "User should be navigated to Products page after login.");
        // még requirement? legyenek kint productok
        List<WebElement> products = driver.findElements(By.xpath("//div[@data-test=\"inventory-item\"]"));
        // null check???
        Assert.assertNotNull(products);
        Assert.assertFalse(products.isEmpty(), "There are no products displayed");

        // dinamikus
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//div[@data-test=\"inventory-item\"]"), 3));
        // olvasható, chainelhető, segít! isempty után ...
        final int MINIMUM_PRODUCT_NUMBER = 3;
        assertThat(products).as("Check if there are at least 3 items").isNotNull().isNotEmpty()
                .hasSizeGreaterThan(MINIMUM_PRODUCT_NUMBER).withFailMessage("There were: " + products.toString());
        // egy és az első
        // assertThat(products).singleElement().hasSameClassAs(WebElement.class);
        WebElement firstItem = driver.findElement(By.xpath("//div[@data-test=\"inventory-item\"]"));
        WebElement addToCartButton = firstItem.findElement(By.xpath("//button[text()=\"Add to cart\"]"));
        addToCartButton.click();

        List<WebElement> removeButtons = firstItem.findElements(By.xpath("//button[text()=\"Remove\"]"));
        Assert.assertFalse(removeButtons.isEmpty(), "Remove button did not appear");
        WebElement cartBadge = firstItem.findElement(By.xpath("//span[@class=\"shopping_cart_badge\"]"));
        String cartQuantity = cartBadge.getText();
        Assert.assertTrue(cartQuantity.equals("1"), "There should be exactly one item in the cart");

        driver.findElement(By.xpath("//div/a[@data-test=\"shopping-cart-link\"]")).click();
        List<WebElement> cartItems = driver.findElements(By.className("cart_item"));
        Assert.assertTrue(cartItems.size() == 1, "There should be exactly one item in the cart");
        driver.findElement(By.id("checkout")).click();
        String checkoutTitle = driver.findElement(By.className("title")).getText();
        Assert.assertEquals(checkoutTitle, "Checkout: Your Information");
        driver.quit();

        // page object model !!!!
    }
}
// @AfterMethod
// driver.close();

// @AfterClass
// public void tearDown() {
// driver.quit();
// }
// }
