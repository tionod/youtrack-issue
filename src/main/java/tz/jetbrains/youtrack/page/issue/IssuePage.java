package tz.jetbrains.youtrack.page.issue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import tz.jetbrains.youtrack.AbstractPage;
import tz.jetbrains.youtrack.element.IssueFieldsPanel;

public class IssuePage extends AbstractPage<IssuePage> {

    private final By issueIdLocator = By.xpath("//span[@class='js-issue-id']");
    private final By issueSummary = By.xpath("//h1[@data-test='issueSummary']");
    private final By issueDescription = By.xpath("//p[@data-test='issueDescription']");
    private final By attachment = By.xpath("//div[@data-test='attachment']");

    public IssuePage(WebDriver driver) {
        super(driver, "/issue", IssuePage.class);
    }

    public String getIssueId() {
        return getWebElement(issueIdLocator).getText();
    }

    public String getSummary() {
        return getWebElement(issueSummary).getText();
    }

    public String getDescription() {
        return getWebElement(issueDescription).getText();
    }

    public IssueFieldsPanel getIssueFieldsPanel() {
        return new IssueFieldsPanel(driver);
    }

    public boolean attachContainImage(String imageName) {
        return getWebElement(attachment)
                .findElement(By.xpath("//img[@alt='"+ imageName +"']"))
                .isDisplayed();
    }

    public IssuePage openIssue(String issueId) {
        return super.open(url + "/" + issueId.toUpperCase(), IssuePage.class);
    }
}
