package obudai.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage<CartPage> {

    private final By cartItems = By.className("cart_item");
    private final By checkoutButton = By.id("checkout");
    private final By cartTitle = By.cssSelector(".title");
    private final By removeButton = By.cssSelector(".cart_button");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected CartPage self() {
        return this;
    }

    public int getCartItemCount() {
        List<?> items = driver.findElements(cartItems);
        return items.size();
    }

    public String getTitle() {
        return getText(cartTitle);
    }

    public CheckoutPage clickCheckout() {
        click(checkoutButton);
        return new CheckoutPage(driver);
    }

    public CartPage removeFirstItem() {
        return click(removeButton);
    }

    public CheckoutPage proceedToCheckout() {
        return clickCheckout();
    }
}
