package tz.jetbrains.youtrack;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import tz.jetbrains.helper.ConfigHelper;
import tz.jetbrains.youtrack.page.LoginPage;
import tz.jetbrains.youtrack.page.MainPage;
import tz.jetbrains.youtrack.page.issue.NewIssuePage;

import java.io.File;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class IssueCreationTests extends BaseHooks {

    private static String username;
    private static String password;

    @BeforeAll
    static void prepareUser() {
        var youTrackProperties = ConfigHelper.getYouTrackProperties();
        username = youTrackProperties.getLogin();
        password = youTrackProperties.getPassword();
    }

    @Test
    @DisplayName("Создание issue после ввода обязательного поля (summary)")
    void checkIssueCreationWithRequiredArgs() {
        var newIssuePage = signInAndGoToNewIssuePage();
        String expectedSummary = inputAndGetRandomSummary(newIssuePage);

        var issuePage = newIssuePage.createIssue();
        assertEquals(expectedSummary, issuePage.getSummary());
    }

    @Test
    @DisplayName("Создание issue после ввода summary + description")
    void checkIssueCreationWithSummaryAndDesc() {
        var newIssuePage = signInAndGoToNewIssuePage();
        String expectedSummary = inputAndGetRandomSummary(newIssuePage);
        String expectedDesc = faker.bojackHorseman().quotes();
        newIssuePage.inputTextToEditor(expectedDesc);

        var issuePage = newIssuePage.createIssue();
        assertAll(
                () -> assertEquals(expectedSummary, issuePage.getSummary()),
                () -> assertEquals(expectedDesc, issuePage.getDescription())
        );
    }

    @Test
    @DisplayName("Создание issue после ввода summary + attachment")
    void checkIssueCreationWithSummaryAndAttach() {
        var newIssuePage = signInAndGoToNewIssuePage();
        String expectedSummary = inputAndGetRandomSummary(newIssuePage);
        File pic = new File("src/test/resources/files/image.png");

        newIssuePage.uploadFile(pic.getAbsolutePath());
        var issuePage = newIssuePage.createIssue();
        assertAll(
                () -> assertEquals(expectedSummary, issuePage.getSummary()),
                () -> assertTrue(issuePage.attachContainImage(pic.getName()))
        );
    }

    @Test
    @DisplayName("Создание issue после изменения параметров")
    void checkIssueCreationWhenIssueFieldsChanged() {
        var newIssuePage = signInAndGoToNewIssuePage();
        String expectedSummary = inputAndGetRandomSummary(newIssuePage);

        var issueFieldsOnCreatePage = newIssuePage.getIssueFieldsPanel();
        String expectedPriority = pickAndGetNotCurrentFieldValue(
                issueFieldsOnCreatePage.getPriority(), issueFieldsOnCreatePage.getAvailablePriorities()
        );
        String expectedType = pickAndGetNotCurrentFieldValue(
                issueFieldsOnCreatePage.getType(), issueFieldsOnCreatePage.getAvailableTypes()
        );

        String expectedState = pickAndGetNotCurrentFieldValue(
                issueFieldsOnCreatePage.getState(), issueFieldsOnCreatePage.getAvailableStates()
        );

        var issuePage = newIssuePage.createIssue();
        var issueFieldsPanelOnViewPage = issuePage.getIssueFieldsPanel();
        assertAll(
                () -> assertEquals(expectedSummary, issuePage.getSummary()),
                () -> assertEquals(expectedPriority, issueFieldsPanelOnViewPage.getPriority()),
                () -> assertEquals(expectedType, issueFieldsPanelOnViewPage.getType()),
                () -> assertEquals(expectedState, issueFieldsPanelOnViewPage.getState())
        );
    }

    private String inputAndGetRandomSummary(NewIssuePage page) {
        String randomSummary = faker.address().fullAddress();
        page.inputSummary(randomSummary);
        return randomSummary;
    }

    private NewIssuePage signInAndGoToNewIssuePage() {
        LoginPage loginPage = new LoginPage(webDriver).open();
        MainPage mainPage = loginPage.signIn(username, password);
        return mainPage.goToNewIssuePage();
    }


    private String pickAndGetNotCurrentFieldValue(String currentValue, Map<String, WebElement> availableValues) {
        availableValues.remove(currentValue);
        Object[] entries = availableValues.entrySet().toArray();

        @SuppressWarnings("unchecked")
        Map.Entry<String, WebElement> entry =
                (Map.Entry<String, WebElement>) entries[new Random().nextInt(entries.length)];

        WebElement randomElement = entry.getValue();
        randomElement.click();

        return entry.getKey();
    }
}