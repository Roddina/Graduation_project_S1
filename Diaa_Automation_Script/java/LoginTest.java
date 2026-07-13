package tests;

import baseTest.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    String validUsername = "demouser";
    String validPassword = "testingisfun99";
    String invalidPassword = "wrongpassword";

    @Test(priority = 1)
    public void validLoginWithValidCredentials(){
        Assert.assertEquals(homePage.getActualTitle(), homePage.getExpectedTitle());

        loginPage = homePage.clickSignInLink();
        Assert.assertEquals(loginPage.getActualTitle(), loginPage.getExpectedTitle());

        homePage = loginPage.loginWith(validUsername, validPassword);

//        Assert.assertTrue(homePage.isLogoutButtonDisplayed());
        Assert.assertEquals(homePage.getLoggedInUsername(), validUsername);
    }

    @Test(priority = 2)
    public void loginWithInvalidUsername(){
        loginPage = homePage.clickSignInLink();
        loginPage.selectUsername("invalidUser123");

        Assert.assertFalse(loginPage.isLoginButtonEnabled());
    }

    @Test(priority = 3)
    public void loginWithInvalidPassword(){
        loginPage = homePage.clickSignInLink();
        loginPage.selectUsername(validUsername);
        loginPage.selectPassword(invalidPassword);
        loginPage.clickLoginButton();

        Assert.assertTrue(loginPage.getCurrentUrl().contains("/signin"));
    }


    @Test(priority = 4)
    public void logout(){
        loginPage = homePage.clickSignInLink();
        homePage = loginPage.loginWith(validUsername, validPassword);
        Assert.assertTrue(homePage.isLogoutButtonDisplayed());

        homePage = homePage.clickLogoutButton();

        Assert.assertTrue(homePage.isSignInLinkDisplayed());
    }

    @Test(priority = 5)
    public void verifyLoggedInUsername(){
        loginPage = homePage.clickSignInLink();
        homePage = loginPage.loginWith(validUsername, validPassword);

        Assert.assertEquals(homePage.getLoggedInUsername(), validUsername);
    }
}

