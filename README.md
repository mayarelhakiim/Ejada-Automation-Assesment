# EJADA End-to-End Test Automation Assessment

A unified, scalable test automation framework covering **Web UI**, **Android Mobile**, and **REST API** testing. Built using **Java 21**, **Maven**, **Selenium 4**, **Appium 9**, **REST Assured 5**, **Cucumber 7 (BDD)**, and **TestNG**.

---

## Table of Contents
1. [Framework Architecture & Design Highlights](#1-framework-architecture--design-highlights)
2. [Technology Stack](#2-technology-stack)
3. [Project Directory Structure](#3-project-directory-structure)
4. [Prerequisites & Environment Setup](#4-prerequisites--environment-setup)
5. [Centralized Configuration (`config.properties`)](#5-centralized-configuration-configproperties)
6. [Detailed Test Coverage](#6-detailed-test-coverage)
7. [How to Run the Tests (Step-by-Step)](#7-how-to-run-the-tests-step-by-step)
8. [Viewing Test Execution Reports & Screenshots](#8-viewing-test-execution-reports--screenshots)
9. [Troubleshooting Common Issues](#9-troubleshooting-common-issues)

---

## 1. Framework Architecture & Design Highlights
* **Strict Page Object Model (POM):** Every distinct web page and mobile screen is encapsulated in its own dedicated class (`LoginPage`, `ProductsPage`, `CartPage`, `CheckoutPage`, `OverviewPage`, `OrderConfirmationPage`, `MobileLoginPage`), separating UI locators from test assertions.
* **Thread-Safe Parallel Execution:** Uses `ThreadLocal<WebDriver>` and `ThreadLocal<AndroidDriver>` coupled with Cucumber-TestNG's `@DataProvider(parallel = true)` to run Web scenarios concurrently without thread collision.
* **Externalized Data-Driven Testing:** Web negative login scenarios dynamically consume test datasets from `src/test/resources/testdata/invalid_credentials.json` using Jackson `ObjectMapper`.
* **Isolated Web & Mobile Hooks:** Uses Cucumber tag filtering (`not @Mobile` vs. `@Mobile`) so Web browser hooks and Android Appium hooks never interfere with one another.
* **Automated Failure Screenshots:** `@After` hooks automatically capture and embed PNG screenshots directly into the Cucumber HTML report whenever a Web or Mobile scenario fails.

---

## 2. Technology Stack
* **Programming Language:** Java 21 LTS
* **Build & Dependency Management:** Apache Maven
* **Web UI Automation:** Selenium WebDriver `4.25.0`
* **Mobile Automation:** Appium Java Client `9.3.0` (`UiAutomator2` driver)
* **API Automation:** REST Assured `5.5.0`
* **BDD Framework:** Cucumber Java & Cucumber TestNG `7.20.1`
* **Test Runner & Assertions:** TestNG `7.10.2`
* **JSON Parsing & Serialization:** Jackson Databind `2.18.1`

---

## 3. Project Directory Structure

```text
ejada-automation-assessment/
├── pom.xml                                                     # Maven dependencies & Surefire configuration
├── README.md                                                   # Project documentation & execution guide
└── src/test/
    ├── java/com/ejada/automation/
    │   ├── api/
    │   │   └── SimpleBooksApiTest.java                         # REST Assured CRUD API test suite (7 tests)
    │   ├── base/
    │   │   └── DriverManager.java                              # Thread-safe WebDriver initialization (Chrome/Firefox)
    │   ├── hooks/
    │   │   └── Hooks.java                                      # Web @Before and @After hooks + screenshot capture
    │   ├── mobile/
    │   │   ├── base/
    │   │   │   └── MobileDriverManager.java                    # Thread-safe AndroidDriver & UiAutomator2Options setup
    │   │   ├── hooks/
    │   │   │   └── MobileHooks.java                            # Mobile @Before("@Mobile") & @After("@Mobile") hooks
    │   │   ├── pages/
    │   │   │   └── MobileLoginPage.java                        # SauceLabs Android App login page object
    │   │   └── steps/
    │   │       └── MobileLoginSteps.java                       # Cucumber step definitions for Mobile login
    │   ├── pages/
    │   │   ├── LoginPage.java                                  # Web Login page object (saucedemo.com)
    │   │   ├── ProductsPage.java                               # Products catalog & dynamic price sorting logic
    │   │   ├── CartPage.java                                   # Shopping cart item verification
    │   │   ├── CheckoutPage.java                               # Customer details form (checkout-step-one.html)
    │   │   ├── OverviewPage.java                               # Pre-tax subtotal & URL verification (checkout-step-two.html)
    │   │   └── OrderConfirmationPage.java                      # Final order dispatch & Thank You confirmation
    │   ├── runners/
    │   │   ├── WebTestRunner.java                              # Parallel TestNG Cucumber runner for Web UI suites
    │   │   └── MobileTestRunner.java                           # Sequential TestNG Cucumber runner for Mobile suites
    │   ├── steps/
    │   │   ├── LoginSteps.java                                 # Step definitions for Web Scenario 1
    │   │   └── PurchaseSteps.java                              # Step definitions for Web Scenario 2
    │   └── utils/
    │       ├── ConfigReader.java                               # Properties file reader with CLI override support
    │       └── JsonDataReader.java                             # Jackson JSON reader for data-driven testing
    └── resources/
        ├── apps/
        │   └── Android.SauceLabs.Mobile.Sample.app.2.7.1.apk   # Target Android application binary
        ├── config/
        │   └── config.properties                               # Global environment, browser, Appium & API settings
        ├── features/
        │   ├── Login.feature                                   # Web Scenario 1: Data-driven invalid login (4 datasets)
        │   ├── Purchase.feature                                # Web Scenario 2: End-to-End purchase of top 2 items
        │   └── MobileLogin.feature                             # Mobile Scenarios: Positive & Negative Android login
        └── testdata/
            └── invalid_credentials.json                        # External test data for Web Scenario 1
```

---

## 4. Prerequisites & Environment Setup

### For Web UI & API Testing
1. **Java 21 LTS:** Ensure `java -version` returns JDK 21 and `JAVA_HOME` is configured.
2. **Apache Maven:** Ensure `mvn -version` is accessible from the terminal.
3. **Browsers:** Google Chrome and/or Mozilla Firefox installed (Selenium Manager automatically resolves `chromedriver` and `geckodriver`).

### Additional Requirements for Mobile Testing
1. **Node.js & Appium Server (v2.x / v3.x):**
   ```bash
   npm install -g appium
   appium driver install uiautomator2
   ```
2. **Android Studio & Android SDK:**
   * Configure `ANDROID_HOME` environment variable.
   * Add `%ANDROID_HOME%\platform-tools` to system `PATH`.
   * Create and boot an Android Virtual Device (Emulator) via Android Studio **Virtual Device Manager** (default device ID: `emulator-5554`).

---

## 5. Centralized Configuration (`config.properties`)

All environment parameters are managed in `src/test/resources/config/config.properties`:

```properties
# Supported values: chrome, firefox
browser=chrome

# Application Under Test URL
baseUrl=https://www.saucedemo.com/

# Explicit wait timeout in seconds
timeout=10

# Valid credentials for Scenario 2 & Mobile Positive Login
validUsername=standard_user
validPassword=secret_sauce

# Mobile Automation Configuration (Appium + Android)
appiumServerUrl=http://127.0.0.1:4723
androidDeviceName=emulator-5554
androidApkPath=src/test/resources/apps/Android.SauceLabs.Mobile.Sample.app.2.7.1.apk
androidAppPackage=com.swaglabsmobileapp
androidAppActivity=com.swaglabsmobileapp.MainActivity

# API Testing Configuration (Simple Books API)
apiBaseUrl=https://simple-books-api.click
```

---

## 6. Detailed Test Coverage

### Part 1: Web UI Automation (`https://www.saucedemo.com/`)
* **Scenario 1 (`Login.feature` - Data-Driven Negative Login):**
  * Reads 4 invalid credential datasets from `src/test/resources/testdata/invalid_credentials.json`.
  * Executes all 4 datasets in parallel and asserts the exact error banner text (`locked_out_user`, missing username, missing password, and mismatched credentials).
* **Scenario 2 (`Purchase.feature` - End-to-End Purchase Flow):**
  * Logs in with `standard_user` / `secret_sauce` and verifies redirection to the Products page.
  * Dynamically parses all product prices on the page, sorts them descending, and adds the **two most expensive products** (`Sauce Labs Fleece Jacket` at `$49.99` and `Sauce Labs Backpack` at `$29.99`) to the cart.
  * Navigates to the Cart page and verifies both selected items are listed.
  * Proceeds to Checkout (`checkout-step-one.html`), fills in customer details, and continues to Overview (`checkout-step-two.html`).
  * Verifies that the displayed pre-tax item subtotal (`$79.98`) equals the exact mathematical sum of the two selected items and asserts the URL is `https://www.saucedemo.com/checkout-step-two.html`.
  * Finishes the order and verifies both the `"Thank you for your order!"` header and `"Your order has been dispatched"` message.

### Part 2: Mobile Automation (`Android.SauceLabs.Mobile.Sample.app.2.7.1.apk`)
* **Positive Scenario (`@MobilePositiveLogin`):** Launches the SauceLabs Android app on the emulator, logs in with `standard_user` / `secret_sauce`, and verifies navigation to the `PRODUCTS` catalog screen.
* **Negative Scenario (`@MobileNegativeLogin`):** Launches the app, attempts login with `locked_out_user` / `secret_sauce`, and verifies the native error banner `"Sorry, this user has been locked out."`.

### Part 3: API Automation (`SimpleBooksApiTest.java` - REST Assured)
Executes a 7-step end-to-end CRUD lifecycle against `https://simple-books-api.click`:
1. **`GET /status`**: Verifies the API health status is `200 OK` and returns `"OK"`.
2. **`GET /books?type=fiction`**: Retrieves available fiction books and captures a valid `bookId`.
3. **`POST /api-clients/`**: Registers a unique API client using a dynamic timestamp email and extracts the `accessToken` (`201 Created`).
4. **`POST /orders`**: Submits a new book order using the Bearer token and extracts the generated `orderId` (`201 Created`).
5. **`GET /orders/{orderId}`**: Retrieves the created order by ID and asserts `id`, `bookId`, and `customerName` (`200 OK`).
6. **`PATCH /orders/{orderId}`**: Updates the `customerName` on the order (`204 No Content`) and performs a follow-up `GET` to confirm the change persisted on the server.
7. **`DELETE /orders/{orderId}`**: Deletes the order (`204 No Content`) and performs a follow-up `GET` to confirm the server returns `404 Not Found`.

---

## 7. How to Run the Tests (Step-by-Step)

### Suite 1: Running Web UI Tests (5 Parallel Scenarios)

#### Option A: From IntelliJ IDEA
1. Open `src/test/java/com/ejada/automation/runners/WebTestRunner.java`.
2. Right-click anywhere inside the editor and select **Run 'WebTestRunner'**.
3. Five browser instances will launch concurrently (4 for Scenario 1 + 1 for Scenario 2).

#### Option B: From Terminal (Maven CLI)
* **Run with default browser (Chrome):**
  ```bash
  mvn clean test -Dtest=WebTestRunner
  ```
* **Run with Firefox (Cross-Browser CLI Override):**
  ```bash
  mvn clean test -Dtest=WebTestRunner -Dbrowser=firefox
  ```
  *(Alternatively, change `browser=firefox` in `config.properties`).*

---

### Suite 2: Running API Tests (7 CRUD Tests)

#### Option A: From IntelliJ IDEA
1. Open `src/test/java/com/ejada/automation/api/SimpleBooksApiTest.java`.
2. Right-click inside the class and select **Run 'SimpleBooksApiTest'**.

#### Option B: From Terminal (Maven CLI)
```bash
mvn test -Dtest=SimpleBooksApiTest
```

---

### Suite 3: Running Mobile Tests (2 Android Scenarios)

#### Step 1: Start the Android Emulator
1. Open **Android Studio** -> **Virtual Device Manager** and click **Play (`▶`)** on your emulator.
2. Verify the emulator is online by running the following command in PowerShell/Terminal:
   ```bash
   adb devices
   ```
   Expected output:
   ```text
   List of devices attached
   emulator-5554   device
   ```
   *(If your emulator uses a different port, e.g., `emulator-5556`, update `androidDeviceName` in `config.properties`).*

#### Step 2: Start the Appium Server
Open a dedicated terminal window and start Appium:
```bash
appium
```
Wait until you see: `Appium REST http interface listener started on http://0.0.0.0:4723`. Keep this terminal running.

#### Step 3: Execute `MobileTestRunner`
* **From IntelliJ IDEA:**
  Right-click `src/test/java/com/ejada/automation/runners/MobileTestRunner.java` and select **Run 'MobileTestRunner'**.
* **From Terminal (Maven CLI):**
  ```bash
  mvn test -Dtest=MobileTestRunner
  ```

---

### Running All Suites Together (Web + Mobile + API)
If your Android Emulator and Appium server are running in the background, you can trigger the entire project (all 14 tests across Web, Mobile, and API) with a single Maven command:
```bash
mvn clean test
```

---

## 8. Viewing Test Execution Reports & Screenshots

After executing the tests, detailed HTML and JSON reports are automatically generated inside the `target/` folder:

### 1. Cucumber HTML Reports (Interactive BDD Reports)
* **Web UI Execution Report:**
  * **Path:** `target/cucumber-reports/web-cucumber-report.html`
  * **Machine-Readable JSON:** `target/cucumber-reports/web-cucumber.json`
* **Mobile Execution Report:**
  * **Path:** `target/cucumber-reports/mobile-cucumber-report.html`
  * **Machine-Readable JSON:** `target/cucumber-reports/mobile-cucumber.json`

**How to open:** In IntelliJ, right-click `web-cucumber-report.html` or `mobile-cucumber-report.html` -> **Open In** -> **Browser** -> **Chrome** (or double-click the file in Windows File Explorer).
* Includes pass/fail statistics, step durations, tags, and embedded failure screenshots (if any step fails).

### 2. TestNG / Maven Surefire Reports (Includes Web, Mobile & API Results)
When running via `mvn test` (or IntelliJ TestNG runner), TestNG generates summary reports at:
* **Emailable HTML Summary:** `target/surefire-reports/emailable-report.html`
* **Detailed Suite Index:** `target/surefire-reports/index.html`

---

## 9. Troubleshooting Common Issues

* **`UiAutomator2 server ... instrumentation process is not running (probably crashed)` (Mobile):**
  If the Android emulator's background instrumentation process hangs between runs, reset the helper packages on the emulator via terminal:
  ```bash
  adb uninstall io.appium.uiautomator2.server
  adb uninstall io.appium.uiautomator2.server.test
  adb uninstall com.swaglabsmobileapp
  ```
  Appium will automatically push fresh copies on the next test run.
* **Emulator shows `offline` in `adb devices`:**
  Run `adb kill-server && adb start-server` in your terminal, or select **Cold Boot Now** from the Android Studio Device Manager menu.
* **Firefox Binary Location on Windows:**
  `DriverManager.java` automatically checks standard Windows installation paths (`C:\Program Files\Mozilla Firefox\firefox.exe` and `AppData\Local\Mozilla Firefox\firefox.exe`) if Firefox is not registered in the system `PATH`.