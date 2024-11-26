package selenium.page_objects.common;

import selenium.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import selenium.page_objects.LandingPage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CategoriesMenu {

    private By categoriesMenu = By.xpath("//ul[@class='hmenu hmenu-visible']");

    //Escaping the ' with \"
    private String subMenuXpathTemplate = "//ul[contains(@class,'hmenu-visible')]//div[text()=\"%s\"]//parent::a";

    private By allLinksInCurrentMenu = By.xpath("//ul[contains(@class,'hmenu-visible')]//child::a[@class='hmenu-item']");


    /**
     * Get a list of subcategories after clicking See all for a category
     *
     * @param category
     * @return a List of all sub categories for a given category
     */
    public List<String> getAllSubCategories(String category) {

        WebElement ul = Browser.wait.until(ExpectedConditions.presenceOfElementLocated(categoriesMenu));

        //Get all the list items from the UL element
        List<WebElement> listItems = ul.findElements(By.tagName("li"));

        List<String> shopByDepartmentMenuItems = new ArrayList<String>();
        //For each li in list items
        for (WebElement li : listItems) {

            //If the LI is empty it is line separator we don't add it
            if (li.getText().isEmpty()) {
                continue;
            } else {
                //Add the item
                shopByDepartmentMenuItems.add(li.getText());
            }
        }

        List<String> subCategory = new ArrayList<>();

        //For each menu item
        for (int index = 0; index < shopByDepartmentMenuItems.size(); index++) {

            //When we see the shop by department menu item
            if (shopByDepartmentMenuItems.get(index).equalsIgnoreCase(category)) {

                //get all the following menu items
                for (int innerIndex = index + 1; innerIndex < shopByDepartmentMenuItems.size(); innerIndex++) {

                    if (!shopByDepartmentMenuItems.get(innerIndex).equalsIgnoreCase("See less")) {
                        subCategory.add(shopByDepartmentMenuItems.get(innerIndex));
                    } else {
                        //break if the next item is See less
                        break;
                    }
                }
                break;
            }
        }
        return subCategory;
    }


    /*
    The issue here is that when you click with Selenium on the sub menu Electronics for example
    and you try to get all the links with locator //ul[contains(@class,'hmenu-visible')]//child::a[@class='hmenu-item']
    you will actually see that they are duplicated and in addition if you scroll while the test is being executed you will
    see that the menu is ACTUALLY broken at this point and it duplicates the links + the back button of the menu
    That's why I decided to go for
    refresh --> click all categories --> click see all --> click sub menu --> get links.  Instead of
    click sub menu --> get the links --> click back button
    The issue is not reproducible without Selenium
     */
    public Set<String> getAllLinksForSubMenu(List<String> menus) {
        LandingPage landingPage = new LandingPage();

        Set<String> linksToVisit = new HashSet<>();

        for (String menu : menus) {

            Browser.getDriver().navigate().refresh();

            landingPage.clickAllCategoriesMenu();

            landingPage.clickFirstSeeAllButton();

            //Click on a sub menu
            clickSubMenu(menu);

            //Get the links of the sub menu as web elements
            List<WebElement> linksForCurrentMenu = getLinksFromCurrentMenu();

            //Get the actual links as Strings
            for (WebElement link : linksForCurrentMenu) {
                linksToVisit.add(link.getAttribute("href"));
            }
        }

        return linksToVisit;
    }

    public void clickSubMenu(String menuName){
        Browser.wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(subMenuXpathTemplate, menuName)))).click();
    }

    public List<WebElement> getLinksFromCurrentMenu(){
        return Browser.wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(allLinksInCurrentMenu));
    }
}
