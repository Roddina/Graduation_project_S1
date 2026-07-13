package tests;

import baseTest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class HomePageTest extends BaseTest {
    String validUsername = "demouser";
    String validPassword = "testingisfun99";

    @Test(priority = 1)
    public void verifyHomePageLoadsSuccessfully(){
        Assert.assertEquals(homePage.getActualTitle(), homePage.getExpectedTitle());
        Assert.assertTrue(homePage.getProductCount() > 0);
    }


    @Test(priority = 2)
    public void verifyProductListIsDisplayed(){
        Assert.assertFalse(homePage.getProductItems().isEmpty());
        Assert.assertTrue(homePage.getProductItems().get(0).isDisplayed());
    }


    @Test(priority = 3)
    public void verifyProductInformation(){
        Assert.assertTrue(homePage.isEveryProductInfoDisplayed());
    }


    @Test(priority = 4)
    public void verifyProductCount(){
        Assert.assertTrue(homePage.getProductCount() >= 1);
    }


    @Test(priority = 5)
    public void verifyBrowserStackLogo(){
        Assert.assertTrue(homePage.isLogoDisplayed());
    }
    @Test(priority = 6)
    public void verifyFooterLinksDoNotOpen404Page() {
        loginPage = homePage.clickSignInLink();
        homePage = loginPage.loginWith(validUsername, validPassword);
        homePage.scrollToBottom();

        int linksCount = homePage.getFooterLinks().size();

        for (int i = 1; i < linksCount; i++) {

            homePage.scrollToBottom();

            homePage.clickFooterLink(i);

            if (homePage.is404PageDisplayed()) {
                Assert.fail("Footer link " + (i) + " redirected to a 404 page.");
            }

            driver.get(baseUrl);
            homePage.waitForProductsToLoad();
        }
    }
}