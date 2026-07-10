package testcases;

import base.BaseTest;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutCompletePage;
import pages.CheckoutStepOnePage;
import pages.CheckoutStepTwoPage;
import pages.LoginPage;
import pages.ProductDetailsPage;
import pages.ProductsPage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Automation coverage for TC_001 through TC_020 in sauce_demo_TestCases.pdf. */
public class SauceDemoTestCasesTest extends BaseTest {
    private static final String USER = "standard_user";
    private static final String PASSWORD = "secret_sauce";
    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT = "Sauce Labs Bike Light";

    private ProductsPage login() {
        LoginPage page = new LoginPage(driver);
        page.login(USER, PASSWORD);
        ProductsPage products = new ProductsPage(driver);
        Assert.assertTrue(products.isDisplayed(), "Inventory page did not load after login");
        return products;
    }

    private CheckoutStepOnePage beginCheckout() {
        ProductsPage products = login();
        products.addToCart(BACKPACK);
        return products.openCart().clickCheckout();
    }

    private void assertUrlContains(String fragment) {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.urlContains(fragment));
        Assert.assertTrue(driver.getCurrentUrl().contains(fragment), "Expected URL fragment: " + fragment);
    }

    @Test(description = "TC_001 - Standard user can log in")
    public void tc001SuccessfulLogin() { login(); assertUrlContains("inventory.html"); }

    @Test(description = "TC_002 - Locked-out user receives an error")
    public void tc002LockedOutUser() {
        LoginPage page = new LoginPage(driver);
        page.login("locked_out_user", PASSWORD);
        Assert.assertEquals(page.getErrorMessage(), "Epic sadface: Sorry, this user has been locked out.");
    }

    @Test(description = "TC_003 - Incorrect password receives an error")
    public void tc003IncorrectPassword() {
        LoginPage page = new LoginPage(driver);
        page.login(USER, "wrong_password");
        Assert.assertEquals(page.getErrorMessage(), "Epic sadface: Username and password do not match any user in this service");
    }

    @Test(description = "TC_004 - Username is required")
    public void tc004EmptyUsername() {
        LoginPage page = new LoginPage(driver);
        page.login("", PASSWORD);
        Assert.assertEquals(page.getErrorMessage(), "Epic sadface: Username is required");
    }

    @Test(description = "TC_005 - Password is required")
    public void tc005EmptyPassword() {
        LoginPage page = new LoginPage(driver);
        page.login(USER, "");
        Assert.assertEquals(page.getErrorMessage(), "Epic sadface: Password is required");
    }

    @Test(description = "TC_006 - Cart has the selected products, prices, and quantities")
    public void tc006CartContents() {
        ProductsPage products = login();
        products.addToCart(BACKPACK); products.addToCart(BIKE_LIGHT);
        CartPage cart = products.openCart();
        Assert.assertEquals(cart.getCartItemCount(), 2);
        Assert.assertEquals(cart.getCartItemNames(), List.of(BACKPACK, BIKE_LIGHT));
        Assert.assertEquals(cart.getPrice(BACKPACK), "$29.99");
        Assert.assertEquals(cart.getPrice(BIKE_LIGHT), "$9.99");
        Assert.assertEquals(cart.getQuantity(BACKPACK), 1);
        Assert.assertEquals(cart.getQuantity(BIKE_LIGHT), 1);
    }

    @Test(description = "TC_007 - Removing an item updates cart and badge")
    public void tc007RemoveFromCart() {
        ProductsPage products = login(); products.addToCart(BACKPACK);
        CartPage cart = products.openCart(); cart.removeItem(BACKPACK);
        Assert.assertEquals(cart.getCartItemCount(), 0);
        Assert.assertEquals(new ProductsPage(driver).getCartBadgeCount(), 0);
    }

    @Test(description = "TC_008 - Continue Shopping returns to inventory and retains cart")
    public void tc008ContinueShopping() {
        ProductsPage products = login(); products.addToCart(BACKPACK);
        products = products.openCart().clickContinueShopping();
        Assert.assertTrue(products.isDisplayed());
        Assert.assertEquals(products.getCartBadgeCount(), 1);
    }

    @Test(description = "TC_009 - Checkout opens customer information step")
    public void tc009CheckoutEntry() {
        CheckoutStepOnePage checkout = beginCheckout();
        assertUrlContains("checkout-step-one.html");
        checkout.enterFirstName("Jane");
    }

    @Test(description = "TC_010 - First name is required")
    public void tc010FirstNameRequired() {
        CheckoutStepOnePage checkout = beginCheckout();
        checkout.enterLastName("Doe"); checkout.enterPostalCode("12345"); checkout.clickContinue();
        Assert.assertEquals(checkout.getErrorMessage(), "Error: First Name is required");
    }

    @Test(description = "TC_011 - Last name is required")
    public void tc011LastNameRequired() {
        CheckoutStepOnePage checkout = beginCheckout();
        checkout.enterFirstName("Jane"); checkout.enterPostalCode("12345"); checkout.clickContinue();
        Assert.assertEquals(checkout.getErrorMessage(), "Error: Last Name is required");
    }

    @Test(description = "TC_012 - Postal code is required")
    public void tc012PostalCodeRequired() {
        CheckoutStepOnePage checkout = beginCheckout();
        checkout.enterFirstName("Jane"); checkout.enterLastName("Doe"); checkout.clickContinue();
        Assert.assertEquals(checkout.getErrorMessage(), "Error: Postal Code is required");
    }

    @Test(description = "TC_013 - Overview totals are correct")
    public void tc013CheckoutTotals() {
        ProductsPage products = login(); products.addToCart(BACKPACK); products.addToCart(BIKE_LIGHT);
        CheckoutStepTwoPage overview = products.openCart().clickCheckout().fillInformationAndContinue("Jane", "Doe", "12345");
        Assert.assertEquals(overview.getOrderedItemNames(), List.of(BACKPACK, BIKE_LIGHT));
        Assert.assertEquals(overview.getSubtotal(), "Item total: $39.98");
        Assert.assertEquals(overview.getTax(), "Tax: $3.20");
        Assert.assertEquals(overview.getTotal(), "Total: $43.18");
    }

    @Test(description = "TC_014 - Finish completes the order")
    public void tc014FinishOrder() {
        CheckoutCompletePage complete = beginCheckout().fillInformationAndContinue("Jane", "Doe", "12345").clickFinish();
        Assert.assertEquals(complete.getConfirmationHeader(), "Thank you for your order!");
        Assert.assertEquals(new ProductsPage(driver).getCartBadgeCount(), 0);
    }

    @Test(description = "TC_015 - Cancel checkout returns to cart and retains item")
    public void tc015CancelCheckout() {
        CartPage cart = beginCheckout().clickCancel();
        assertUrlContains("cart.html");
        Assert.assertEquals(cart.getCartItemNames(), List.of(BACKPACK));
    }

    @Test(description = "TC_016 - Back to Products returns to inventory")
    public void tc016BackToProducts() {
        ProductDetailsPage detail = login().clickProduct(BACKPACK);
        Assert.assertTrue(detail.clickBackToProducts().isDisplayed());
        assertUrlContains("inventory.html");
    }

    @Test(description = "TC_017 - Price sort persists after cart navigation")
    public void tc017SortPersists() {
        ProductsPage products = login(); products.sortByPriceHighToLow();
        List<Double> expected = new ArrayList<>(products.getDisplayedProductPrices()); expected.sort(Comparator.reverseOrder());
        products = products.openCart().clickContinueShopping();
        Assert.assertEquals(products.getSelectedSortOption(), "hilo");
        Assert.assertEquals(products.getDisplayedProductPrices(), expected);
    }

    @Test(description = "TC_018 - All six product images load")
    public void tc018ImagesLoad() { Assert.assertTrue(login().areAllProductImagesLoaded(), "One or more product images failed to load"); }

    @Test(description = "TC_019 - Inventory DOM content loads in under three seconds")
    public void tc019InventoryPerformance() {
        login();
        Number duration = (Number) ((JavascriptExecutor) driver).executeScript(
                "return performance.getEntriesByType('navigation')[0].domContentLoadedEventEnd;");
        Assert.assertNotNull(duration, "Navigation timing was unavailable");
        Assert.assertTrue(duration.doubleValue() < 3000, "Inventory DOMContentLoaded took " + duration + "ms");
    }

    @Test(description = "TC_020 - Session persists after refresh")
    public void tc020SessionPersistsAfterRefresh() {
        login(); driver.navigate().refresh();
        Assert.assertTrue(new ProductsPage(driver).isDisplayed());
        assertUrlContains("inventory.html");
    }
}
