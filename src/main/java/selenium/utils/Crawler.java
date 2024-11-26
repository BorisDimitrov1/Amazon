package selenium.utils;

import selenium.browser.Browser;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class Crawler {

    private final String RESULTS_FILE_PATH = System.getProperty("user.dir") + "/results/";

    public void visitLinksWriteInfoInDocument(Set<String> links) throws Exception {

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = timestamp + "_results.txt";

        File file = new File(RESULTS_FILE_PATH + filename);

        if(!file.createNewFile()){
            throw new Exception("File was not created. CreateNewFile() returns false");
        }

        try(FileWriter writer = new FileWriter(file.getPath())){

            for(String link : links){

                /*
                 * I tried going thought the links with CURL, Rest Assured and Selenium Chrome Dev Tools to intercept the response
                 * but amazon knows that this is automation and it is treating it like a BOT
                 * In a real life project I would either
                 * 1) Get all the headers from the current selenium session and try to make it look like a real person in the request that I am going to do
                 * 2) Add chrome profile to the driver
                 */
                Browser.getDriver().navigate().to(link);

                String title = Browser.getDriver().getTitle();


                //There is no way for me to be sure that a link is dead if I don't have the response code or some locator
                //Lets assume that the page title will start with 404 if there is a dead link
                boolean isDead = title.startsWith("404");
                String status = !isDead ? "OK" : "Dead link";

                String contentToWrite = link + " " + Browser.getDriver().getTitle() + " " + status + "\r\n";

                writer.append(contentToWrite);
            }
        }
    }
}
