package com.poly.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class LoginTest {
    private WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        // Giả lập User-Agent trình duyệt thực để tránh bị chặn trên GitHub Runner
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.saucedemo.com/");
    }

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][] {
            {"standard_user", "secret_sauce", true, ""},
            {"standard_user", "wrong_password", false, "Username and password do not match"},
            {"standard_user", "", false, "Password is required"},
            {"locked_out_user", "secret_sauce", false, "Sorry, this user has been locked out"}
        };
    }

    @Test(dataProvider = "loginData", groups = {"smoke", "regression"})
    public void testLoginScenarios(String username, String password, boolean isSuccessExpected, String expectedMessage) {
        WebElement txtUser = driver.findElement(By.id("user-name"));
        WebElement txtPass = driver.findElement(By.id("password"));
        WebElement btnLogin = driver.findElement(By.id("login-button"));

        txtUser.clear();
        txtUser.sendKeys(username);

        txtPass.clear();
        txtPass.sendKeys(password);

        btnLogin.click();

        if (isSuccessExpected) {
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("inventory.html"), "Đăng nhập thất bại với thông tin đúng!");
        } else {
            WebElement lblError = driver.findElement(By.cssSelector("h3[data-test='error']"));
            // Dùng contains để tránh lỗi lệch khoảng trắng/ký tự ẩn giữa các môi trường
            Assert.assertTrue(lblError.getText().contains(expectedMessage), 
                "Thông báo lỗi không chứa nội dung mong đợi. Thực tế: " + lblError.getText());
        }
    }

    @Test(groups = {"exception"}, expectedExceptions = org.openqa.selenium.NoSuchElementException.class)
    public void testNoSuchElementException() {
        driver.findElement(By.id("element_not_exist")).click();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File dir = new File("./screenshots");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File target = new File(dir, result.getName() + "_" + System.currentTimeMillis() + ".png");
            try {
                FileHandler.copy(source, target);
                System.out.println("[SCREENSHOT] Ảnh lỗi được lưu tại: " + target.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Không thể lưu ảnh chụp màn hình: " + e.getMessage());
            }
        }

        if (driver != null) {
            driver.quit();
        }
    }
}