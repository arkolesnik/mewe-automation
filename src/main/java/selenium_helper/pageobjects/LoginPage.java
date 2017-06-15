package selenium_helper.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends AbstractPage {

    @FindBy(id = "login-fake-btn")
    private WebElement memberLoginButton;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void openLoginForm(WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(memberLoginButton));
        memberLoginButton.click();
    }

    public void loginSuccess(String username, String password) {
        emailInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();
    }
}
