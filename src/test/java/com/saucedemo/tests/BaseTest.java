package com.saucedemo.tests;

import com.saucedemo.config.ConfigReader;
import com.saucedemo.driver.DriverFactory;
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
        DriverFactory.initDriver();
        DriverFactory.getDriver().get(config.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // Делаем скриншот при падении теста и прикрепляем к Allure отчёту
        if (result.getStatus() == ITestResult.FAILURE) {
            takeScreenshot("FAILED - " + result.getName());
        }
        DriverFactory.quitDriver();
    }

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    @Attachment(value = "{attachmentName}", type = "image/png")
    public byte[] takeScreenshot(String attachmentName) {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
    }
}
