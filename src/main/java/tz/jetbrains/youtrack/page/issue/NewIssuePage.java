package tz.jetbrains.youtrack.page.issue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tz.jetbrains.youtrack.page.AbstractPage;

public class NewIssuePage extends AbstractPage<NewIssuePage> {

    private final By createButtonLocator = By.xpath("//button[@data-test='createIssueAction']");
    private final By cancelButtonLocator = By.xpath("//button[@data-test='cancelAction']");
    private final By summaryFieldLocator = By.xpath("//textarea[@data-test='issueSummary']");
    private final By editorProseMirrorLocator = By.xpath("//div[contains(@class, 'ProseMirror')]");
    private final By attachmentLocator = By.xpath("//input[@type='file']");

    public NewIssuePage(WebDriver driver) {
        super(driver, "/newIssue", NewIssuePage.class);
    }

    public WebElement getCreateButton() {
        return getWebElement(createButtonLocator);
    }

    public WebElement getCancelButton() {
        return getWebElement(cancelButtonLocator);
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

    public void inputSummary(String value) {
        getWebElement(summaryFieldLocator).sendKeys(value);
    }
}
