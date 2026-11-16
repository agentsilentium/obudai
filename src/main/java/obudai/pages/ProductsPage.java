package obudai.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Legacy alternative to InventoryPage, retained so existing examples still compile. */
public class ProductsPage extends BasePage<ProductsPage> {

    private final By pageTitle = By.className("title");
    private final By firstAddToCartButton = By.cssSelector(".inventory_item button");
    private final By cartIcon = By.className("shopping_cart_link");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected ProductsPage self() {
        return this;
    }

    public String getTitle() {
        return getText(pageTitle);
    }

    public ProductsPage addFirstProductToCart() {
        return click(firstAddToCartButton);
    }

    public CartPage goToCart() {
        click(cartIcon);
        return new CartPage(driver);
    }
}
