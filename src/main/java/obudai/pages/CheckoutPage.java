package obudai.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage<CheckoutPage> {

    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By confirmationText = By.cssSelector(".complete-header");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected CheckoutPage self() {
        return this;
    }

    public CheckoutPage fillCustomerInfo(String first, String last, String postal) {
        type(firstNameField, first)
                .type(lastNameField, last)
                .type(postalCodeField, postal);
        return click(continueButton);
    }

    public CheckoutPage finishOrder() {
        return click(finishButton);
    }

    public String getConfirmationMessage() {
        return getText(confirmationText);
    }
}
