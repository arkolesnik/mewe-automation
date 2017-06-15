import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.*;
import selenium_helper.pageobjects.LoginPage;
import selenium_helper.pageobjects.MainPage;

import java.net.URL;

public class ExampleTest extends DriverFixture {

    private LoginPage loginPage;
    private MainPage mainPage;

    @BeforeClass
    public void initializePages() {
        loginPage = new LoginPage(driver);
        mainPage = new MainPage(driver);
    }

    @Test
    @Parameters({"username", "password"})
    public void login(String username, String password) {
        loginPage.openLoginForm(wait);
        loginPage.loginSuccess(username, password);
        Assert.assertTrue(mainPage.isUserLoggedIn(wait), "User was probably not logged in \n");
    }

    @Test
    @Parameters("photoFileName")
    public void postRandomMessageWithPhoto(String photoFileName) {
        String randomMessage = RandomStringUtils.randomAlphabetic(30);
        int numberOfPostsBefore = mainPage.getNumberOfPosts();
        mainPage.insertMessage(randomMessage);
        URL photoResource = ExampleTest.class.getResource(photoFileName);
        mainPage.attachPhotoToPost(photoResource, driver);
        mainPage.clickShareButton(wait, driver);
        driver.navigate().refresh();

        String error = mainPage.isLatestPostAsExpected(wait, numberOfPostsBefore);
        Assert.assertTrue(error.toString().isEmpty(), error.toString());
    }

    @Test
    @Parameters({"photoFileName", "videoFileName"})
    public void sendRandomMessageWithFilesInChat(String photoFileName, String videoFileName) {
        String randomMessage = RandomStringUtils.randomAlphabetic(15);
        StringBuilder error = new StringBuilder();
        mainPage.openTopChat(wait);
        mainPage.insertTextInChat(randomMessage);
        mainPage.sendMessageToChat();

        if (!mainPage.getTextFromLatestMessageInChat().equalsIgnoreCase(randomMessage)) {
            error.append("Message was not sent; \n");
        }

        int numberOfMessageBefore;
        numberOfMessageBefore = mainPage.getNumberOfMessages();
        URL photoResource = ExampleTest.class.getResource(photoFileName);
        mainPage.attachFileToChat(photoResource, driver);

        if (!mainPage.latestMessageContainPhoto(wait, numberOfMessageBefore)) {
            error.append("Photo was not uploaded; \n");
        }

        numberOfMessageBefore = mainPage.getNumberOfMessages();
        URL videoResource = ExampleTest.class.getResource(videoFileName);
        mainPage.attachFileToChat(videoResource, driver);

        if (!mainPage.latestMessageContainVideo(wait, numberOfMessageBefore)) {
            error.append("Video was not attached; \n");
        }

        Assert.assertTrue(error.toString().isEmpty(), error.toString());
    }
}
