package obudai.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Common functionality shared by all page objects.
 *
 * The recursive type parameter (T extends BasePage<T>) lets inherited
 * interaction methods return the concrete page type instead of BasePage.
 */
public abstract class BasePage<T extends BasePage<T>> {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /** Returns this object as its concrete page type, without an unchecked cast. */
    protected abstract T self();

    protected T click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        return self();
    }

    protected T type(By locator, String text) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).sendKeys(text);
        return self();
    }

    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    protected T open(String url) {
        driver.get(url);
        return self();
    }

    /** Example of an inherited fluent method whose return type is the concrete page. */
    public T refresh() {
        driver.navigate().refresh();
        return self();
    }

    /** Example of another inherited fluent method. */
    public T navigateBack() {
        driver.navigate().back();
        return self();
    }
}
