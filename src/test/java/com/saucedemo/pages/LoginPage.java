package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object для страницы логина saucedemo.com
 */
public class LoginPage extends BasePage {

    @FindBy(id = "user-name")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @Step("Ввести логин: {username}")
    public LoginPage enterUsername(String username) {
        type(usernameInput, username);
        return this;
    }

    @Step("Ввести пароль")
    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    @Step("Нажать кнопку Login")
    public InventoryPage clickLogin() {
        click(loginButton);
        return new InventoryPage();
    }

    @Step("Логин с невалидными данными")
    public LoginPage clickLoginExpectingError() {
        click(loginButton);
        return this;
    }

    // Fluent-метод: логин за один вызов
    @Step("Выполнить логин под пользователем: {username}")
    public InventoryPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return errorMessage.isDisplayed();
    }
}
