package com.saucedemo.tests;

import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Epic("Shopping")
@Feature("Cart")
public class CartTest extends BaseTest {

    private InventoryPage inventoryPage;

    @BeforeMethod
    public void loginBeforeEachTest() {
        inventoryPage = new LoginPage().login(
                config.getStandardUser(),
                config.getPassword()
        );
    }

    @Test(description = "Добавление товара в корзину")
    @Story("Add to cart")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddItemToCart() {
        inventoryPage.addFirstItemToCart();

        assertEquals(inventoryPage.getCartItemCount(), 1,
                "В корзине должен быть 1 товар");
    }

    @Test(description = "Корзина содержит добавленный товар")
    @Story("Cart contains items")
    @Severity(SeverityLevel.CRITICAL)
    public void testCartContainsAddedItem() {
        inventoryPage.addFirstItemToCart();
        CartPage cartPage = inventoryPage.goToCart();

        assertEquals(cartPage.getCartItemCount(), 1,
                "Корзина должна содержать 1 товар");
        assertTrue(cartPage.isLoaded(), "Страница корзины должна загрузиться");
    }

    @Test(description = "Добавление нескольких товаров в корзину")
    @Story("Add multiple items")
    @Severity(SeverityLevel.NORMAL)
    public void testAddMultipleItemsToCart() {
        inventoryPage
                .addItemToCartByName("Sauce Labs Backpack")
                .addItemToCartByName("Sauce Labs Bike Light");

        assertEquals(inventoryPage.getCartItemCount(), 2,
                "В корзине должно быть 2 товара");
    }
}
