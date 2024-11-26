package tests;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import selenium.browser.Browser;

import selenium.page_objects.CartPage;
import selenium.page_objects.LandingPage;
import selenium.page_objects.ResultsPage;
import selenium.page_objects.common.CategoriesMenu;
import selenium.utils.CartAssertions;
import selenium.utils.Crawler;

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

    private CategoriesMenu categoriesMenu = new CategoriesMenu();
    private Crawler crawer = new Crawler();



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
                "\r\nExpected and actual items in the cart does not match." +
                        "\r\nExpected: " + addedItemNames +
                        "\r\nActual: " + itemNamesInCart);

        CartAssertions.verifyProducts(addedItemNames, itemNamesInCart);
    }

    @Test
    public void Task2() throws Exception {
        Browser.getDriver().get(APP_UNDER_TEST);
        landingPage.validateCorrectAmazonIsPresented();

        //Expand all categories menu
        landingPage.clickAllCategoriesMenu();

        landingPage.clickFirstSeeAllButton();

        //Get a list for all the sub menus of Shop By Department
        List<String> shopByDepartmentSubMenus = categoriesMenu.getAllSubCategories("Shop by department");

        Set<String> allLinks = categoriesMenu.getAllLinksForSubMenu(shopByDepartmentSubMenus);

        crawer.visitLinksWriteInfoInDocument(allLinks);
    }


    @AfterTest
    public void afterScenario() {
        Browser.tearDown();
    }
}
