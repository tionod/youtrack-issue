package tz.jetbrains.youtrack;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tz.jetbrains.helper.ConfigHelper;
import tz.jetbrains.pojo.YouTrackProperties;
import tz.jetbrains.youtrack.page.LoginPage;
import tz.jetbrains.youtrack.page.MainPage;
import tz.jetbrains.youtrack.page.issue.IssuePage;
import tz.jetbrains.youtrack.page.issue.NewIssuePage;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class IssueCreateTests extends BaseHooks {

    @Test
    void checkCreateButtonWhenSummaryIsEmpty() {
        NewIssuePage newIssuePage = signInAndOpenNewIssuePage();
        assertFalse(newIssuePage.getCreateButton().isEnabled());
    }

    @Test
    void checkCreateButtonWhenSummaryIsNotEmpty() {
        NewIssuePage newIssuePage = signInAndOpenNewIssuePage();
        newIssuePage.inputSummary("test summary");
        assertTrue(newIssuePage.getCreateButton().isEnabled());
    }

    @Test
    void checkCancelButtonRedirect() {
        NewIssuePage newIssuePage = signInAndOpenNewIssuePage();
        assertTrue(newIssuePage.getCancelButton().isEnabled());
        newIssuePage.getCancelButton().click();
        Assertions.assertAll(
                () -> assertFalse(newIssuePage.getCurrentUrl().contains("/newIssues")),
                () -> assertFalse(newIssuePage.getTitle().contains("New issue"))
        );
    }

    @Test
    void checkIssueCreationWithRequiredArgs() {
        NewIssuePage newIssuePage = signInAndOpenNewIssuePage();
        newIssuePage.inputSummary(faker.pokemon().name() + " " + faker.number().digit());
        newIssuePage.getCreateButton().click();
        IssuePage issuePage = new IssuePage(webDriver);
        Assertions.assertNotNull(issuePage.getIssueId());
    }

    @Test
    void checkIssueCreationWithSummaryAndDesc() {
        NewIssuePage newIssuePage = signInAndOpenNewIssuePage();
        newIssuePage.inputSummary(faker.pokemon().name() + " " + faker.number().digit());
        newIssuePage.inputTextToEditor("test desc");
        newIssuePage.getCreateButton().click();
        IssuePage issuePage = new IssuePage(webDriver);
        Assertions.assertNotNull(issuePage.getIssueId());
    }

    @Test
    void checkIssueCreationWithSummaryAndAttach() {
        NewIssuePage newIssuePage = signInAndOpenNewIssuePage();
        newIssuePage.inputSummary(faker.pokemon().name() + " " + faker.number().digit());
        File pic = new File("src/test/resources/files/image.png");
        newIssuePage.uploadFile(pic.getAbsolutePath());
        newIssuePage.getCreateButton().click();
        IssuePage issuePage = new IssuePage(webDriver);
        Assertions.assertNotNull(issuePage.getIssueId());
    }



    private NewIssuePage signInAndOpenNewIssuePage() {
        MainPage mainPage = openLoginPageAndSignInByDefaultUser();
        return mainPage.goToNewIssuePage();
    }

    private MainPage openLoginPageAndSignInByDefaultUser() {
        YouTrackProperties youTrackProperties = ConfigHelper.getYouTrackProperties();
        LoginPage loginPage = new LoginPage(webDriver).open();
        return loginPage.signIn(youTrackProperties.getLogin(), youTrackProperties.getPassword());
    }
}
