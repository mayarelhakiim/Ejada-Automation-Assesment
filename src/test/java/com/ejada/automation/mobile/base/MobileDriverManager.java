package com.ejada.automation.mobile.base;

import com.ejada.automation.utils.ConfigReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

public class MobileDriverManager {

    private static final ThreadLocal<AndroidDriver> mobileDriverThread = new ThreadLocal<>();

    public static void initDriver() {
        if (mobileDriverThread.get() == null) {
            File apkFile = new File(ConfigReader.getProperty("androidApkPath"));
            if (!apkFile.exists()) {
                throw new RuntimeException("APK file not found at: " + apkFile.getAbsolutePath());
            }

            UiAutomator2Options options = new UiAutomator2Options()
                    .setPlatformName("Android")
                    .setAutomationName("UiAutomator2")
                    .setDeviceName(ConfigReader.getProperty("androidDeviceName"))
                    .setApp(apkFile.getAbsolutePath())
                    .setAppPackage(ConfigReader.getProperty("androidAppPackage"))
                    .setAppWaitActivity(ConfigReader.getProperty("androidAppActivity"))
                    .setNewCommandTimeout(Duration.ofSeconds(60));

            try {
                String serverUrl = ConfigReader.getProperty("appiumServerUrl");
                AndroidDriver driver = new AndroidDriver(URI.create(serverUrl).toURL(), options);
                mobileDriverThread.set(driver);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Invalid Appium Server URL in config.properties", e);
            }
        }
    }

    public static AndroidDriver getDriver() {
        return mobileDriverThread.get();
    }

    public static void quitDriver() {
        AndroidDriver driver = mobileDriverThread.get();
        if (driver != null) {
            driver.quit();
            mobileDriverThread.remove();
        }
    }
}