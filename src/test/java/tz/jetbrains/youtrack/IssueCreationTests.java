package tz.jetbrains.youtrack;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tz.jetbrains.helper.ConfigHelper;
import tz.jetbrains.webdriver.WebDriverService;
import tz.jetbrains.youtrack.element.HeaderMenu;
import tz.jetbrains.youtrack.page.DashboardPage;
import tz.jetbrains.youtrack.page.LoginPage;
import tz.jetbrains.youtrack.page.issue.IssuePage;
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
    static void prepare() {
        var youTrackProperties = ConfigHelper.getYouTrackProperties();
        username = youTrackProperties.getLogin();
        password = youTrackProperties.getPassword();

        switchUIToClassicMode(username, password);
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
    @DisplayName("Создание issue после изменения проекта")
    void checkIssueCreationWhenProjectChanged() {
        var newIssuePage = signInAndGoToNewIssuePage();
        String expectedSummary = inputAndGetRandomSummary(newIssuePage);
        String expectedProject = pickAndGetNotCurrentFieldValue("", newIssuePage.getAvailableProjects());

        newIssuePage.waitUntilCreateButtonToBeClickable();
        var issue = newIssuePage.createIssue();
        assertAll(
                () -> assertEquals(expectedSummary, issue.getSummary()),
                () -> assertEquals(expectedProject, issue.getIssueFieldsPanel().getProjectName())
        );
    }

    @Test
    @DisplayName("Создание issue после изменения параметров")
    void checkIssueCreationWhenIssueFieldsChanged() {
        var newIssuePage = signInAndGoToNewIssuePage();
        String expectedSummary = inputAndGetRandomSummary(newIssuePage);

        var issueFieldsOnCreatePage = newIssuePage.getIssueFieldsPanel();
        String expectedPriority = pickAndGetNotCurrentFieldValue(
                issueFieldsOnCreatePage.getPriorityName(), issueFieldsOnCreatePage.getAvailablePriorities()
        );
        String expectedType = pickAndGetNotCurrentFieldValue(
                issueFieldsOnCreatePage.getTypeName(), issueFieldsOnCreatePage.getAvailableTypes()
        );

        String expectedState = pickAndGetNotCurrentFieldValue(
                issueFieldsOnCreatePage.getStateName(), issueFieldsOnCreatePage.getAvailableStates()
        );

        var issuePage = newIssuePage.createIssue();
        var issueFieldsPanelOnViewPage = issuePage.getIssueFieldsPanel();
        assertAll(
                () -> assertEquals(expectedSummary, issuePage.getSummary()),
                () -> assertEquals(expectedPriority, issueFieldsPanelOnViewPage.getPriorityName()),
                () -> assertEquals(expectedType, issueFieldsPanelOnViewPage.getTypeName()),
                () -> assertEquals(expectedState, issueFieldsPanelOnViewPage.getStateName())
        );
    }

    @Test
    @DisplayName("Создание issue cо связью с другой issue")
    void checkLinkedIssueCreation() {
        var firstIssueCreatePage = signInAndGoToNewIssuePage();
        String firstSummary = inputAndGetRandomSummary(firstIssueCreatePage);
        String firstIssueId = firstIssueCreatePage
                .createIssue()
                .getIssueId();

        var headerMenu = new HeaderMenu(webDriver);
        String pickedLinkType = pickAndGetNotCurrentFieldValue("", headerMenu.getAvailableIssueLinkTypes());
        headerMenu.waitLoading();

        var secondIssueCreatePage = new NewIssuePage(webDriver);
        String secondSummary = inputAndGetRandomSummary(secondIssueCreatePage);
        IssuePage issueWithLink = secondIssueCreatePage.createIssue();
        assertAll(
                () -> assertEquals(secondSummary, issueWithLink.getSummary()),
                () -> assertTrue(pickedLinkType.equalsIgnoreCase(issueWithLink.getIssueLinksListGroupTitleLocalor())),
                () -> assertEquals(firstIssueId, issueWithLink.getLinkedIssueId()),
                () -> assertEquals(firstSummary, issueWithLink.getLinkedIssueSummary())
        );
    }

    @Test
    @DisplayName("Создание issue из черновика")
    void checkIssueCreationFromDraft() {
        var draftIssueCreatePage = signInAndGoToNewIssuePage();
        String draftSummary = inputAndGetRandomSummary(draftIssueCreatePage);
        String draftDesc = faker.bojackHorseman().quotes();
        draftIssueCreatePage.inputTextToEditor(draftDesc);

        var draftIssueFieldsPanel = draftIssueCreatePage.getIssueFieldsPanel();
        String draftPriority = draftIssueFieldsPanel.getPriorityName();
        String draftType = draftIssueFieldsPanel.getTypeName();
        String draftState = draftIssueFieldsPanel.getStateName();

        Assertions.assertTrue(draftIssueCreatePage.getDeleteDraftButton().isDisplayed(), "черновик не был сохранен");

        draftIssueCreatePage.cancelIssue();
        new DashboardPage(webDriver).waitPageLoading();

        var headerMenu = new HeaderMenu(webDriver);
        var newIssuePage = headerMenu.goToNewIssuePage();
        newIssuePage.getAvailableDrafts()
                .get(draftSummary)
                .click();
        newIssuePage = new NewIssuePage(webDriver);
        var issue = newIssuePage.createIssue();
        var issueFieldsPanel = issue.getIssueFieldsPanel();
        Assertions.assertAll(
                () -> assertEquals(draftSummary, issue.getSummary()),
                () -> assertEquals(draftDesc, issue.getDescription()),
                () -> assertEquals(draftPriority, issueFieldsPanel.getPriorityName()),
                () -> assertEquals(draftType, issueFieldsPanel.getTypeName()),
                () -> assertEquals(draftState, issueFieldsPanel.getStateName())
        );

    }

    private String inputAndGetRandomSummary(NewIssuePage page) {
        String randomSummary = faker.address().fullAddress();
        page.inputSummary(randomSummary);
        return randomSummary;
    }

    private NewIssuePage signInAndGoToNewIssuePage() {
        var loginPage = new LoginPage(webDriver).open();
        loginPage.signIn(username, password);
        return new HeaderMenu(webDriver).goToNewIssuePage();
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

    /**
     * Проверка и отключение YouTrack Lite если интерфейс включен
     *
     * @param username пользователя от лица которого будут выполняться тесты
     * @param password пользователя от лица которого будут выполняться тесты
     */
    private static void switchUIToClassicMode(String username, String password) {
        WebDriver driver = WebDriverService.getDriver();
        var loginPage = new LoginPage(driver).open();
        loginPage.signIn(username, password);
        var headerMenu = new HeaderMenu(driver);
        headerMenu.switchUIToClassicMode();
        driver.quit();
    }
}
