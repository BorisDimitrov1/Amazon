package selenium.utils;

import org.testng.Assert;

import java.util.List;

public class CartAssertions {

    /**
     * Verify that all strings from a list are Contained in another list
     *
     * @param expected list of expected names
     * @param actual   list of actual names
     */
    public static void verifyProducts(List<String> expected, List<String> actual) {
        /*
        Doing this verification since there are cases where the full name is not presented on the results page, but it is presented on the cart page
        * Example:
        * In Search:          Windows 11 Laptop 15.6 Inch 4GB DDR 192GB Memory,with Intel N3450 Up to 2.2 GHz,HD IPS Display,Thin & Light Notebook PC,USB3.0,Mini HDMI,10000mAh Battery (White), LeadBook T15 Plus
        * In Cart: Exilapsire Windows 11 Laptop 15.6 Inch 4GB DDR 192GB Memory,with Intel N3450 Up to 2.2 GHz,HD IPS Display,Thin & Light Notebook PC,USB3.0,Mini HDMI,10000mAh Battery (White), LeadBook T15 Plus
         */


        //For each product
        for (String addedProduct : expected) {

            boolean isPresented = false;

            for (String itemInCart : actual) {

                //Verify that is contained in the actual list
                if (itemInCart.contains(addedProduct)) {
                    isPresented = true;
                    break;
                }
            }

            Assert.assertTrue(isPresented, "Product: " + addedProduct + " was not presented in the cart.");
        }
    }
}
