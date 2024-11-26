package selenium.page_objects;

import selenium.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class CartPage {

    private By itemNames = By.xpath("//h4//span[@class='a-truncate-full a-offscreen']");

    public List<String> getItemNames(){
        List<String> namesToReturn = new ArrayList<>();

        List<WebElement> namesAsElements = Browser.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(itemNames));

        for(WebElement name : namesAsElements){
            namesToReturn.add(name.getAttribute("innerText").trim());
        }

        return namesToReturn;
    }
}
