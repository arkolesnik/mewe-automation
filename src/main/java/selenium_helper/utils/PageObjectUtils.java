package selenium_helper.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class PageObjectUtils {

    public static final String SCRIPT_DISPLAY_BLOCK = "arguments[0].style.display='block'";

    public static String getPathToFile(URL resource) {
        File file = null;
        try {
            file = Paths.get(resource.toURI()).toFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public static void setVisible(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript(SCRIPT_DISPLAY_BLOCK, element);
    }
}
