package com.ejada.automation.base;

import com.ejada.automation.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    static {
        // Suppress non-critical Selenium CDP version mismatch warnings in console output
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
    }

    public static void initDriver() {
        if (driverThread.get() == null) {
            String browser = ConfigReader.getProperty("browser").toLowerCase();
            WebDriver driver;

            // Synchronize driver instantiation to prevent Windows file-locking (os error 5)
            // when SeleniumManager extracts/reads driver binaries across parallel threads
            synchronized (DriverManager.class) {
                switch (browser) {
                    case "chrome":
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("--remote-allow-origins=*");
                        driver = new ChromeDriver(options);
                        break;
                    case "firefox":
                        driver = new FirefoxDriver();
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported browser in config.properties: " + browser);
                }
            }

            driver.manage().window().maximize();
            driverThread.set(driver);
        }
    }

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }
    }
}