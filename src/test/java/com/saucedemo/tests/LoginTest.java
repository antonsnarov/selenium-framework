package com.saucedemo.tests;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Epic("Authentication")
@Feature("Login")
public class LoginTest extends BaseTest {

    @Test(description = "Успешный логин с валидными данными")
    @Story("Positive login")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage();

        InventoryPage inventoryPage = loginPage.login(
                config.getStandardUser(),
                config.getPassword()
        );

        assertEquals(inventoryPage.getPageTitle(), "Products",
                "После логина должна открыться страница Products");
        assertTrue(inventoryPage.isLoaded(),
                "Страница инвентаря должна загрузиться");
    }

    @Test(description = "Ошибка при логине заблокированного пользователя")
    @Story("Negative login - locked user")
    @Severity(SeverityLevel.NORMAL)
    public void testLockedUserLogin() {
        LoginPage loginPage = new LoginPage();

        loginPage.enterUsername(config.getLockedUser())
                 .enterPassword(config.getPassword())
                 .clickLoginExpectingError();

        assertTrue(loginPage.isErrorDisplayed(),
                "Должно появиться сообщение об ошибке");
        assertTrue(loginPage.getErrorMessage().contains("locked out"),
                "Сообщение должно содержать 'locked out'");
    }

    @Test(description = "Ошибка при пустом логине и пароле")
    @Story("Negative login - empty credentials")
    @Severity(SeverityLevel.MINOR)
    public void testEmptyCredentialsLogin() {
        LoginPage loginPage = new LoginPage();

        loginPage.clickLoginExpectingError();

        assertTrue(loginPage.isErrorDisplayed(),
                "Должно появиться сообщение об ошибке");
        assertTrue(loginPage.getErrorMessage().contains("Username is required"),
                "Должна быть ошибка о пустом логине");
    }

    @Test(description = "Ошибка при неверном пароле")
    @Story("Negative login - wrong password")
    @Severity(SeverityLevel.NORMAL)
    public void testWrongPasswordLogin() {
        LoginPage loginPage = new LoginPage();

        loginPage.enterUsername(config.getStandardUser())
                 .enterPassword("wrong_password")
                 .clickLoginExpectingError();

        assertTrue(loginPage.isErrorDisplayed(),
                "Должно появиться сообщение об ошибке");
        assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
                "Должна быть ошибка о несовпадении логина/пароля");
    }
}
