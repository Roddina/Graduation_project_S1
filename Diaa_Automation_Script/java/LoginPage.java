package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {

    WebDriver driver;
    WebDriverWait wait;

    String expectedTitle = "StackDemo";

    By usernameDropdownBy = By.id("username");
    By usernameOptionsBy = By.cssSelector("div[id^='react-select-2-option']");
    By passwordDropdownBy = By.id("password");
    By passwordOptionsBy = By.cssSelector("div[id^='react-select-3-option']");
    By loginButtonBy = By.id("login-btn");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getExpectedTitle() {
        return expectedTitle;
    }

    public String getActualTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }


    public LoginPage selectUsername(String username) {
        wait.until(ExpectedConditions.elementToBeClickable(usernameDropdownBy)).click();
        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(usernameOptionsBy));
        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(username)) {
                option.click();
                break;
            }
        }
        return this;
    }


    public LoginPage selectPassword(String password) {
        wait.until(ExpectedConditions.elementToBeClickable(passwordDropdownBy)).click();
        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(passwordOptionsBy));
        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(password)) {
                option.click();
                break;
            }
        }
        return this;
    }

    public boolean isLoginButtonEnabled() {
        return driver.findElement(loginButtonBy).isEnabled();
    }

    public HomePage clickLoginButton() {
        driver.findElement(loginButtonBy).click();
        return new HomePage(driver);
    }

    public HomePage loginWith(String username, String password) {
        selectUsername(username);
        selectPassword(password);
        return clickLoginButton();
    }
}