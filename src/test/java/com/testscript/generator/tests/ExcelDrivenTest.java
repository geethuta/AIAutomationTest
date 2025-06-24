package com.testscript.generator.tests;

import com.testscript.generator.utils.ExcelUtils;
import com.testscript.generator.utils.ExcelUtils.TestCase;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ExcelDrivenTest {
    private WebDriver driver;

    // Default configuration for login scenario
    private static final String DEFAULT_URL = "https://example.com/login";
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpass";
    private static final String USERNAME_LOCATOR = "name=username";
    private static final String PASSWORD_LOCATOR = "name=password";
    private static final String LOGIN_BTN_LOCATOR = "id=loginBtn";
    private static final String EXPECTED_TITLE = "Dashboard";

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @DataProvider(name = "excelTestCases")
    public Iterator<Object[]> excelTestCases() throws IOException {
        String excelPath = "testcases/TSKMGT-3692_test_cases (5).xlsx";
        List<TestCase> testCases = ExcelUtils.getTestCases(excelPath);
        return testCases.stream().map(tc -> new Object[]{tc.summary, tc.steps}).iterator();
    }

    @Test(dataProvider = "excelTestCases")
    public void runTestCase(String summary, String steps) {
        System.out.println("Test Summary: " + summary);
        System.out.println("Test Steps: " + steps);
        for (String step : steps.split(";")) {
            executeStep(step.trim());
        }
    }

    private void executeStep(String step) {
        if (step.isEmpty()) return;
        String lower = step.toLowerCase();
        try {
            // Natural language mappings
            if (lower.contains("access the system")) {
                driver.get(DEFAULT_URL);
            } else if (lower.contains("enter username")) {
                driver.findElement(parseBy(USERNAME_LOCATOR)).sendKeys(USERNAME);
            } else if (lower.contains("enter password")) {
                driver.findElement(parseBy(PASSWORD_LOCATOR)).sendKeys(PASSWORD);
            } else if (lower.contains("click the login button")) {
                driver.findElement(parseBy(LOGIN_BTN_LOCATOR)).click();
            } else if (lower.contains("verify the system's response is as expected")) {
                Assert.assertEquals(driver.getTitle(), EXPECTED_TITLE, "Title assertion failed");
            } 
            // Fallback to command-based syntax
            else if (step.toUpperCase().startsWith("OPEN ")) {
                String url = step.substring(5).trim();
                driver.get(url);
            } else if (step.toUpperCase().startsWith("CLICK ")) {
                By by = parseBy(step.substring(6).trim());
                driver.findElement(by).click();
            } else if (step.toUpperCase().startsWith("TYPE ")) {
                String[] parts = step.substring(5).trim().split(" ", 2);
                By by = parseBy(parts[0]);
                String text = parts.length > 1 ? parts[1] : "";
                WebElement el = driver.findElement(by);
                el.clear();
                el.sendKeys(text);
            } else if (step.toUpperCase().startsWith("ASSERT_TITLE ")) {
                String expected = step.substring(13).trim();
                Assert.assertEquals(driver.getTitle(), expected, "Title assertion failed");
            } else {
                System.out.println("Unknown step: " + step);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error executing step: '" + step + "'", e);
        }
    }

    private By parseBy(String locator) {
        // Support id=, name=, css=, xpath=
        if (locator.startsWith("id=")) {
            return By.id(locator.substring(3));
        } else if (locator.startsWith("name=")) {
            return By.name(locator.substring(5));
        } else if (locator.startsWith("css=")) {
            return By.cssSelector(locator.substring(4));
        } else if (locator.startsWith("xpath=")) {
            return By.xpath(locator.substring(6));
        } else {
            throw new IllegalArgumentException("Unsupported locator: " + locator);
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
} 