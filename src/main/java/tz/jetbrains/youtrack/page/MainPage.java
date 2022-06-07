package tz.jetbrains.youtrack.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tz.jetbrains.youtrack.page.issue.NewIssuePage;

public class MainPage extends AbstractPage<MainPage> {

    private final By issueButtonLocator = By.xpath("//button[@data-test='createIssueButton']");

    public MainPage(WebDriver driver) {
        super(driver, "/", MainPage.class);
    }

    public NewIssuePage goToNewIssuePage() {
        getIssuerButton().click();
        return new NewIssuePage(driver);
    }

    public WebElement getIssuerButton() {
        return getWebElement(issueButtonLocator);
    }
}
