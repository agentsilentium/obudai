package obudai.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage<LoginPage> {

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected LoginPage self() {
        return this;
    }

    public LoginPage openPage() {
        return open("https://www.saucedemo.com/");
    }

    /** Submits the login form and returns the page reached after a successful login. */
    public InventoryPage login(String username, String password) {
        type(usernameField, username)
                .type(passwordField, password);
        click(loginButton);
        return new InventoryPage(driver);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return !driver.findElements(errorMessage).isEmpty();
    }
}
