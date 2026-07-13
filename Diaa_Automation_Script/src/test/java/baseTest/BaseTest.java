package baseTest;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import pages.HomePage;
import pages.LoginPage;

public class BaseTest {
    protected WebDriver driver;
    protected HomePage homePage;
    protected LoginPage loginPage;
    protected static String baseUrl = "https://bstackdemo.com/";


    @BeforeTest
    public void setUpTest(){
        System.out.println("Test Started");
    }

    @BeforeClass
    public  void setUp(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        homePage = new HomePage(driver);
    }

    @BeforeMethod
    public void goToHomePage(){
        driver.get(baseUrl);
        homePage.waitForProductsToLoad();
    }

    @AfterClass
    public void tearDown(){
        driver.quit();
    }

    @AfterTest
    public void tearDownTest(){
        System.out.println("Test Finished");
    }

}

