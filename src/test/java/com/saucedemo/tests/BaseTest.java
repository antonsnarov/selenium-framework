package com.saucedemo.tests;

import com.saucedemo.config.ConfigReader;
import com.saucedemo.driver.DriverFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Базовый класс для всех тестов.
 * Управляет жизненным циклом WebDriver и скриншотами при падении.
 */
public class BaseTest {

    protected ConfigReader config = ConfigReader.getInstance();

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        try {
            System.out.println("=== Setting up WebDriver ===");
            System.out.println("Browser: " + config.getBrowser());
            System.out.println("Headless: " + config.isHeadless());
            System.out.println("Remote: " + config.isRemote());

            // Проверяем, можем ли мы работать без браузера для unit тестов
            String className = this.getClass().getSimpleName();
            if (className.contains("Unit") || className.contains("Simple")) {
                System.out.println("Skipping browser setup for unit test class: " + className);
                return;
            }

            // Настройка WebDriverManager для автоматической загрузки драйверов
            System.out.println("Setting up WebDriverManager...");
            WebDriverManager.chromedriver().clearDriverCache().setup();
            WebDriverManager.firefoxdriver().clearDriverCache().setup();
            System.out.println("WebDriverManager setup complete");

            System.out.println("Initializing driver...");
            DriverFactory.initDriver();
            System.out.println("Driver initialized successfully");

            System.out.println("Navigating to: " + config.getBaseUrl());
            DriverFactory.getDriver().get(config.getBaseUrl());
            System.out.println("Navigation complete");

        } catch (Exception e) {
            System.err.println("ERROR in setUp: " + e.getMessage());
            e.printStackTrace();
            // Для unit тестов не выбрасываем исключение
            String testName = Thread.currentThread().getStackTrace()[2].getMethodName();
            if (!testName.contains("Unit") && !testName.contains("Simple")) {
                throw e;
            }
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            System.out.println("=== Tearing down WebDriver ===");
            System.out.println("Test result: " + result.getStatus());

            // Делаем скриншот при падении теста и прикрепляем к Allure отчёту
            if (result.getStatus() == ITestResult.FAILURE) {
                System.out.println("Taking screenshot for failed test: " + result.getName());
                takeScreenshot("FAILED - " + result.getName());
            }

            System.out.println("Quitting driver...");
            DriverFactory.quitDriver();
            System.out.println("Driver quit successfully");

        } catch (Exception e) {
            System.err.println("ERROR in tearDown: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    @Attachment(value = "{attachmentName}", type = "image/png")
    public byte[] takeScreenshot(String attachmentName) {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
    }
}
