package com.saucedemo.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Page Object для страницы каталога товаров (после логина)
 */
public class InventoryPage extends BasePage {

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement burgerMenuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @Step("Получить заголовок страницы")
    public String getPageTitle() {
        return getText(pageTitle);
    }

    @Step("Добавить первый товар в корзину")
    public InventoryPage addFirstItemToCart() {
        WebElement addButton = inventoryItems.get(0)
                .findElement(By.cssSelector("[data-test^='add-to-cart']"));
        click(addButton);
        return this;
    }

    @Step("Добавить товар в корзину по имени: {itemName}")
    public InventoryPage addItemToCartByName(String itemName) {
        inventoryItems.stream()
                .filter(item -> item.findElement(By.className("inventory_item_name"))
                        .getText().equals(itemName))
                .findFirst()
                .ifPresent(item -> {
                    WebElement addButton = item.findElement(
                            By.cssSelector("[data-test^='add-to-cart']"));
                    click(addButton);
                });
        return this;
    }

    @Step("Получить количество товаров в корзине")
    public int getCartItemCount() {
        try {
            return Integer.parseInt(cartBadge.getText());
        } catch (Exception e) {
            return 0;
        }
    }

    @Step("Перейти в корзину")
    public CartPage goToCart() {
        click(cartLink);
        return new CartPage();
    }

    @Step("Выйти из аккаунта")
    public LoginPage logout() {
        click(burgerMenuButton);
        waitForElement(By.id("logout_sidebar_link"));
        click(logoutLink);
        return new LoginPage();
    }

    @Step("Получить количество товаров на странице")
    public int getInventoryItemCount() {
        return inventoryItems.size();
    }

    public boolean isLoaded() {
        return isDisplayed(By.className("inventory_list"));
    }
}
