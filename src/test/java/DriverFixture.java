import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import selenium_helper.utils.DriverUtils;

public class DriverFixture {

    public static final String URL = "https://mewe.com/";

    public WebDriver driver;
    public WebDriverWait wait;

    @BeforeSuite(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        DriverUtils.setOptionsForChrome(options);
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 15);

        driver.get(URL);
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        driver.quit();
    }

}
