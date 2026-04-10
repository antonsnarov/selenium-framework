package com.saucedemo.tests;

import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Epic("Shopping")
@Feature("Checkout")
public class CheckoutTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod
    public void loginAndAddItem() {
        inventoryPage = new LoginPage().login(
                config.getStandardUser(),
                config.getPassword()
        );
        inventoryPage.addFirstItemToCart();
    }

    @Test(description = "Полный флоу оформления заказа")
    @Story("Complete checkout flow")
    @Severity(SeverityLevel.CRITICAL)
    public void testCompleteCheckoutFlow() {
        CheckoutPage checkoutPage = inventoryPage
                .goToCart()
                .proceedToCheckout()
                .fillShippingInfo("John", "Doe", "12345")
                .clickContinue()
                .clickFinish();

        assertTrue(checkoutPage.isOrderComplete(),
                "Заказ должен быть успешно оформлен");
        assertEquals(checkoutPage.getSuccessMessage(), "Thank you for your order!",
                "Должно появиться сообщение об успешном заказе");
    }
}
