package tz.jetbrains.youtrack.page.issue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import tz.jetbrains.youtrack.page.AbstractPage;

public class IssuePage extends AbstractPage<IssuePage> {

    private final By issueIdLocator = By.xpath("//span[@class='js-issue-id']");

    public IssuePage(WebDriver driver) {
        super(driver, "/issue", IssuePage.class);
    }

    public String getIssueId() {
        return getWebElement(issueIdLocator).getText();
    }

    public IssuePage open(String issueId) {
        return super.open(url + "/" + issueId.toUpperCase(), IssuePage.class);
    }
}
