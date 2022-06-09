package tz.jetbrains.youtrack.page.issue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tz.jetbrains.youtrack.AbstractPage;
import tz.jetbrains.youtrack.element.IssueFieldsPanel;

import java.util.Map;

public class NewIssuePage extends AbstractPage<NewIssuePage> {

    private final By createButtonLocator = By.xpath("//button[@data-test='createIssueAction']");
    private final By cancelButtonLocator = By.xpath("//button[@data-test='cancelAction']");
    private final By summaryFieldLocator = By.xpath("//textarea[@data-test='issueSummary']");
    private final By editorProseMirrorLocator = By.xpath("//div[contains(@class, 'ProseMirror')]");
    private final By attachmentLocator = By.xpath("//input[@type='file']");
    private final By draftsDropdownLocator = By.xpath("//span[@data-test='draftsDropdown']");
    private final By draftIssueListItemLocator = By.xpath("//yt-issue-list-item//a");
    private final By deleteDraftButtonLocator = By.xpath("//button[@data-test='deleteDraft']");
    private final By issueProjectButtonLocator = By.xpath("//button[@data-test='issue-project']");

    public NewIssuePage(WebDriver driver) {
        super(driver, "/newIssue", NewIssuePage.class);
    }

    public IssuePage createIssue() {
        WebElement createButton = getCreateButton();
        if (createButton.isEnabled()) {
            createButton.click();
        }
        return new IssuePage(driver);
    }

    public void cancelIssue() {
        getCancelButton().isEnabled();
        getCancelButton().click();
    }

    public WebElement getCreateButton() {
        return getWebElement(createButtonLocator);
    }

    public WebElement getCancelButton() {
        return getWebElement(cancelButtonLocator);
    }

    public WebElement getDeleteDraftButton() {
        return getWebElement(deleteDraftButtonLocator);
    }

    public IssueFieldsPanel getIssueFieldsPanel() {
        return new IssueFieldsPanel(driver);
    }

    public WebElement getDraftDropdownButton() {
        return getWebElement(draftsDropdownLocator);
    }

    public Map<String, WebElement> getAvailableDrafts() {
        getDraftDropdownButton().isEnabled();
        getDraftDropdownButton().click();
        return mapWebElements(getWebElements(draftIssueListItemLocator));
    }

    public Map<String, WebElement> getAvailableProjects() {
        getWebElement(issueProjectButtonLocator).click();
        return mapWebElements(getRingListItems());
    }

    public void inputSummary(String value) {
        getWebElement(summaryFieldLocator).sendKeys(value);
    }

    public void inputTextToEditor(String value) {
        WebElement editor = getWebElement(editorProseMirrorLocator);
        editor.click();
        editor.sendKeys(value);
    }

    public void uploadFile(String filePath) {
        WebElement attachment = driver.findElement(attachmentLocator);
        attachment.sendKeys(filePath);
        wait.until(ExpectedConditions.invisibilityOf(attachment));
    }

    public void waitUntilCreateButtonToBeClickable() {
        wait.until(ExpectedConditions.elementToBeClickable(getCreateButton()));
    }
}
