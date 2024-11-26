package tests;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import selenium.browser.Browser;

import selenium.page_objects.CartPage;
import selenium.page_objects.LandingPage;
import selenium.page_objects.ResultsPage;
import selenium.utils.CartAssertions;

import java.util.*;


public class AmazonTest {

    /*
    It could be passed as a variable to the mvn test command and used here
    but as environments won't change I don't think there is reason to do it here
     */
    private static final String APP_UNDER_TEST = "https://www.amazon.com/";

    private LandingPage landingPage = new LandingPage();
    private ResultsPage resultsPage = new ResultsPage();

    private CartPage cartPage = new CartPage();


    @BeforeTest
    public void beforeScenario() {
        Browser.initChrome();
    }

    @Test
    public void Task1() {
        Browser.getDriver().get(APP_UNDER_TEST);
        landingPage.validateCorrectAmazonIsPresented();

        //Search for laptop
        landingPage.populateSearchBox("laptop");
        landingPage.clickSearchButton();

        //Get all names of the products that we are going to add
        List<String> addedItemNames = resultsPage.getAllNamesForNonDiscountedItemsInStockWithoutAdds();

        //Add all the items to the cart
        resultsPage.addAllNonDiscountedItemsInStockWithoutAddsToCart();
        resultsPage.clickGoToCartButton();

        List<String> itemNamesInCart = cartPage.getItemNames();

        Assert.assertEquals(addedItemNames.size(), itemNamesInCart.size(),
                "Expected and actual items in the cart does not match." +
                        "\nExpected: " + addedItemNames +
                        "\nActual: " + itemNamesInCart);

        CartAssertions.verifyProducts(addedItemNames, itemNamesInCart);
    }

    @AfterTest
    public void afterScenario() {
        Browser.tearDown();
    }
}
