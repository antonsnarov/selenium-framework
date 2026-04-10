package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object для страницы оформления заказа
 */
public class CheckoutPage extends BasePage {

    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "finish")
    private WebElement finishButton;

    @FindBy(className = "complete-header")
    private WebElement successHeader;

    @Step("Заполнить форму доставки: {firstName} {lastName}, {postalCode}")
    public CheckoutPage fillShippingInfo(String firstName, String lastName, String postalCode) {
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(postalCodeInput, postalCode);
        return this;
    }

    @Step("Нажать Continue")
    public CheckoutPage clickContinue() {
        click(continueButton);
        return this;
    }

    @Step("Завершить заказ")
    public CheckoutPage clickFinish() {
        click(finishButton);
        return this;
    }

    @Step("Получить заголовок успешного заказа")
    public String getSuccessMessage() {
        return getText(successHeader);
    }

    public boolean isOrderComplete() {
        return isDisplayed(By.className("complete-header"));
    }
}
