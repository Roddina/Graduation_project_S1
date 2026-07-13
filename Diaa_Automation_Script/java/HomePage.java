package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {

    WebDriver driver;
    WebDriverWait wait;
    static String expectedTitle = "StackDemo";

    By signInLinkBy = By.id("signin");
    By logoBy = By.cssSelector("[class*='Navbar_logo']");

    By productItemBy = By.className("shelf-item");
    By productTitleBy = By.cssSelector("p.shelf-item__title");
    By productPriceBy = By.cssSelector("div.val");
    By productImageBy = By.cssSelector(".shelf-item__thumb img");

    By usernameDisplayBy = By.cssSelector(".username");
    By logoutButtonBy = By.xpath("//span[text()='Logout']");

    By footerLinksBy = By.cssSelector(".footer-links a");;
    By error404By = By.xpath("//h1[text()='404']");
    By bodyBy = By.tagName("body");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String getExpectedTitle() {
        return expectedTitle;
    }

    public String getActualTitle() {
        return driver.getTitle();
    }

    public void waitForProductsToLoad() {
        wait.until(ExpectedConditions.presenceOfElementLocated(productItemBy));
    }

    public LoginPage clickSignInLink() {
        wait.until(ExpectedConditions.elementToBeClickable(signInLinkBy)).click();
        return new LoginPage(driver);
    }

    public boolean isSignInLinkDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(signInLinkBy)).isDisplayed();
    }

    public boolean isLogoDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(logoBy)).isDisplayed();
    }

    public List<WebElement> getProductItems() {
        return driver.findElements(productItemBy);
    }

    public int getProductCount() {
        return getProductItems().size();
    }

    public boolean isEveryProductInfoDisplayed() {
        List<WebElement> products = getProductItems();
        for (WebElement product : products) {
            boolean hasImage = !product.findElements(productImageBy).isEmpty()
                    && product.findElement(productImageBy).isDisplayed();
            boolean hasTitle = !product.findElements(productTitleBy).isEmpty()
                    && !product.findElement(productTitleBy).getText().trim().isEmpty();
            boolean hasPrice = !product.findElements(productPriceBy).isEmpty()
                    && !product.findElement(productPriceBy).getText().trim().isEmpty();
            if (!hasImage || !hasTitle || !hasPrice) {
                return false;
            }
        }
        return true;
    }

    public String getLoggedInUsername() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameDisplayBy)).getText();
    }

    public boolean isLogoutButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButtonBy)).isDisplayed();
    }

    public HomePage clickLogoutButton() {
        driver.findElement(logoutButtonBy).click();
        return new HomePage(driver);
    }
    public List<WebElement> getFooterLinks() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(footerLinksBy));
    }
    public void clickFooterLink(int index) {
        getFooterLinks().get(index).click();
    }
    public boolean is404PageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(error404By))
                .getText()
                .equals("404");
    }
    public void scrollToBottom() {
        driver.findElement(bodyBy).sendKeys(Keys.END);
    }
}

