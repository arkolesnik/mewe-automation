package selenium_helper.pageobjects;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import selenium_helper.utils.PageObjectUtils;

import java.net.URL;
import java.util.*;

public class MainPage extends AbstractPage {

    @FindBy(css = "div.header_user_avatar")
    private WebElement userDropdown;

    @FindBy(css = "div.postbox-placeholder_textarea")
    private WebElement messageInput;

    @FindBy(id = "postbox_header_textarea")
    private WebElement messageBox;

    @FindBy(id = "photos-upload")
    private WebElement invisiblePhotoForm;

    @FindBy(css = "#photos-upload input")
    private WebElement photoInput;

    @FindBy(xpath = ".//button[text()='Share']")
    private WebElement shareButton;

    @FindBy(css = "div.post-admin")
    private List<WebElement> posts;

    @FindBy(css = "div.threads-list div.thread")
    private List<WebElement> chatList;

    @FindBy(css = "div.small-chat")
    private WebElement chat;

    @FindBy(css = "div.chat_send-form div.mw-content-editable")
    private WebElement chatInput;

    @FindBy(css = "div.small-chat div.attachment-btn div.dropdown-menu_opener")
    private WebElement chatPlusButton;

    @FindBy(css = "form.chat-form-upload input.chat-form-input")
    private WebElement chatFileInput;

    @FindBy(css = "form.chat-form-upload")
    private WebElement invisibleFileFormChat;

    @FindBy(css = "div.chat-message")
    private List<WebElement> allChatMessages;

    @FindBy(css = "div.chat-message .chat-message_body p")
    private List<WebElement> chatTextMessages;

    private By allPostsLocator = By.cssSelector("div.post-admin");
    private By allChatMessagesLocator = By.cssSelector("div.chat-message");
    private By textElementFromPostLocator = By.cssSelector("div.post_text p");
    private By photoElementFromPostLocator = By.cssSelector("div.c-mw-post-photo");
    private By postboxLocator = By.cssSelector("div.postbox");
    private By photoContentLocator = By.xpath(".//div[@class='chat-message_content']//p[contains(@class, 'photo-post')]");
    private By attachmentContentLocator = By.className("chat_message_attachment");

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public boolean isUserLoggedIn(WebDriverWait wait) {
        try {
            wait.until(ExpectedConditions.visibilityOf(userDropdown));
        } catch (StaleElementReferenceException e) {
            wait.until(ExpectedConditions.visibilityOf(userDropdown));
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    public void insertMessage(String message) {
        messageInput.click();
        messageBox.sendKeys(message);
    }

    public void attachPhotoToPost(URL resource, WebDriver driver) {
        String path = PageObjectUtils.getPathToFile(resource);
        PageObjectUtils.setVisible(driver, invisiblePhotoForm);
        photoInput.sendKeys(path);
    }

    public void clickShareButton(WebDriverWait wait, WebDriver driver) {
        wait.until(ExpectedConditions.elementToBeClickable(shareButton));
        WebElement postboxElement = driver.findElement(postboxLocator);
        shareButton.click();
        wait.until(ExpectedConditions.stalenessOf(postboxElement));
    }

    public int getNumberOfPosts() {
        return posts.size();
    }

    public String isLatestPostAsExpected(WebDriverWait wait, int numberOfPostsBefore) {
        StringBuilder error = new StringBuilder();
        List<WebElement> elementsAfterAdd;
        try {
            elementsAfterAdd = waitForUpdatedElementList(wait, allPostsLocator, numberOfPostsBefore);
        } catch (TimeoutException e) {
            error.append("Post is not visible; \n");
            return error.toString();
        }
        try {
            elementsAfterAdd.get(elementsAfterAdd.size() - 1).findElement(textElementFromPostLocator);
        } catch (NoSuchElementException e) {
            error.append("There is no text in post; \n");
        }
        try {
            elementsAfterAdd.get(elementsAfterAdd.size() - 1).findElement(photoElementFromPostLocator);
        } catch (NoSuchElementException e) {
            error.append("There is no photo in post; \n");
        }
        return error.toString();
    }

    public void openTopChat(WebDriverWait wait) {
        chatList.get(0).click();
        wait.until(ExpectedConditions.visibilityOf(chat));
    }

    public void insertTextInChat(String text) {
        chatInput.sendKeys(text);
    }

    public void sendMessageToChat() {
        chatInput.sendKeys(Keys.ENTER);
    }

    public void attachFileToChat(URL resource, WebDriver driver) {
        chatPlusButton.click();
        String path = PageObjectUtils.getPathToFile(resource);
        PageObjectUtils.setVisible(driver, invisibleFileFormChat);
        chatFileInput.sendKeys(path);
        chatPlusButton.click();
    }

    public String getTextFromLatestMessageInChat() {
        return chatTextMessages.get(chatTextMessages.size() - 1).getText();
    }

    public boolean latestMessageContainPhoto(WebDriverWait wait, int numberOfMessagesBefore) {
        try {
            List<WebElement> elementsAfterAdd
                    = waitForUpdatedElementList(wait, allChatMessagesLocator, numberOfMessagesBefore);
            wait.until(ExpectedConditions.visibilityOf(
                    elementsAfterAdd.get(elementsAfterAdd.size() - 1).findElement(photoContentLocator)));
        } catch (TimeoutException e) {
            return false;
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public boolean latestMessageContainVideo(WebDriverWait wait, int numberOfMessagesBefore) {
        try {
            List<WebElement> elementsAfterAdd =
                    waitForUpdatedElementList(wait, allChatMessagesLocator, numberOfMessagesBefore);
            wait.until(ExpectedConditions.visibilityOf(
                    elementsAfterAdd.get(elementsAfterAdd.size() - 1).findElement(attachmentContentLocator)));
        } catch (TimeoutException e) {
            return false;
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    public int getNumberOfMessages() {
        return allChatMessages.size();
    }

    private List<WebElement> waitForUpdatedElementList(WebDriverWait wait, By locator, int numberOfMessagesBefore) throws TimeoutException {
        return wait.until(ExpectedConditions.numberOfElementsToBe(
                locator, numberOfMessagesBefore + 1));
    }

}
