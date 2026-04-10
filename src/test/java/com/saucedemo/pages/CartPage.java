package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object для страницы корзины
 */
public class CartPage extends BasePage {

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @Step("Получить количество товаров в корзине")
    public int getCartItemCount() {
        return cartItems.size();
    }

    @Step("Нажать Checkout")
    public CheckoutPage proceedToCheckout() {
        click(checkoutButton);
        return new CheckoutPage();
    }

    @Step("Продолжить покупки")
    public InventoryPage continueShopping() {
        click(continueShoppingButton);
        return new InventoryPage();
    }

    public boolean isLoaded() {
        return isDisplayed(org.openqa.selenium.By.className("cart_list"));
    }
}
