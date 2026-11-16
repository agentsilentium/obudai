package obudai.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class InventoryPage extends BasePage<InventoryPage> {

    private final By inventoryTitle = By.cssSelector(".title");
    private final By addToCartButton = By.cssSelector(".inventory_item button");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected InventoryPage self() {
        return this;
    }

    public String getTitle() {
        return getText(inventoryTitle);
    }

    public InventoryPage addFirstItemToCart() {
        return click(addToCartButton);
    }

    public InventoryPage addSecondItemToCart() {
        List<WebElement> buttons = driver.findElements(addToCartButton);
        buttons.get(1).click();
        return this;
    }

    public String getCartCount() {
        return getText(cartBadge);
    }

    public CartPage goToCart() {
        click(cartLink);
        return new CartPage(driver);
    }
}
