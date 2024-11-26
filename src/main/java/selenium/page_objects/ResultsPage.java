package selenium.page_objects;

import selenium.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class ResultsPage {

    /**
     * span[@class='a-price'] --> all items with prices
     * parent::a[not(div)] --> parent <a> node without a div in it - div contains the discount element
     * ancestor::div[@class='a-section a-spacing-small a-spacing-top-small'] --> elements that are not adds
     * button --> assuming that if the product is out of stock there won't be add to cart button
     */
    private static final String BASE_XPATH_NON_DISCOUNTED_ITEMS_IN_STOCK_WITHOUT_ADDS = "//span[@class='a-price']//parent::a[not(div)]//ancestor::div[@class='a-section a-spacing-small a-spacing-top-small']//button[@id]";

    private By nonDiscountedItemsInStockWithoutAdds = By.xpath(BASE_XPATH_NON_DISCOUNTED_ITEMS_IN_STOCK_WITHOUT_ADDS);

    /**
     * By appending ancestor and getting the h2 from the BASE_XPATH_NON_DISCOUNTED_ITEMS_IN_STOCK_WITHOUT_ADDS we are getting all the titles of those items
     */
    private By namesOfNonDiscountedItemsInStockWithoutAdds = By.xpath(BASE_XPATH_NON_DISCOUNTED_ITEMS_IN_STOCK_WITHOUT_ADDS + "//ancestor::div[@class='a-section a-spacing-small a-spacing-top-small']//h2");

    private By goToCartButton = By.xpath("//a[contains(text(),'Go to Cart')]");



    public void addAllNonDiscountedItemsInStockWithoutAddsToCart(){
        List<WebElement> allItems = Browser.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(nonDiscountedItemsInStockWithoutAdds));

        for(WebElement item : allItems){
            item.click();

            /*
             Wait until the button is presented again
             otherwise it is getting glitchy and not all items are added to the cart
             */
            Browser.wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(item)));
        }
    }

    public List<String> getAllNamesForNonDiscountedItemsInStockWithoutAdds(){
        List<String> names = new ArrayList<String>();

        List<WebElement> namesAsElements = Browser.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(namesOfNonDiscountedItemsInStockWithoutAdds));

        for(WebElement name : namesAsElements){
            names.add(name.getText().trim());
        }

        return names;
    }


    /*
     * Sometimes the page is still loading after adding item to the cart
     * it is throwing a StaleElementReferenceException if you try to locate the element directly
     * that's why I handled it with try catch and ExpectedConditions.refreshed before that
     */
    public void clickGoToCartButton(){

        Browser.wait.until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfElementLocated(goToCartButton)));

        try{
            Browser.wait.until(ExpectedConditions.elementToBeClickable(goToCartButton)).click();
        }catch (StaleElementReferenceException ex){
            Browser.wait.until(ExpectedConditions.elementToBeClickable(goToCartButton)).click();
        }
    }
}
