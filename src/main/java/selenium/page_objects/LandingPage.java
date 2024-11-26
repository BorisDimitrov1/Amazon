package selenium.page_objects;

import selenium.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LandingPage {

    private By deliverToCountryLabel = By.id("glow-ingress-line2");

    private By searchBox = By.id("twotabsearchtextbox");
    private By searchButton = By.id("nav-search-submit-button");

    private By allCategoriesMenu = By.id("nav-hamburger-menu");

    private By firstSeeAllButton = By.xpath("(//a[contains(@class,'hmenu-compressed-btn')])[1]");


    /**
     * Check if Bulgaria is presented, refresh if it is not
     */
    public void validateCorrectAmazonIsPresented(){

        WebDriverWait wait = new WebDriverWait(Browser.getDriver(), Duration.ofSeconds(3));

        try{
            wait.until(ExpectedConditions.textToBe(deliverToCountryLabel, "Bulgaria"));
        }catch (TimeoutException ex){
            Browser.getDriver().navigate().refresh();
        }

    }

    public void populateSearchBox(String input){
        WebElement search = Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));

        search.clear();
        search.sendKeys(input);
    }

    public void clickSearchButton(){
        Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(searchButton)).click();
    }

    public void clickAllCategoriesMenu(){
        Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(allCategoriesMenu)).click();
    }

    public void clickFirstSeeAllButton() {
        Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(firstSeeAllButton)).click();
    }
}
