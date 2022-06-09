package tz.jetbrains.youtrack.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tz.jetbrains.youtrack.AbstractPage;

import java.util.Map;

public class IssueFieldsPanel extends AbstractPage<IssueFieldsPanel> {

    private final By projectButtonLocator = By.xpath("//button[@data-test='issue-project']");
    private final By priorityButtonLocator = By.xpath("//button[@data-test='Priority']");
    private final By issueTypeButtonLocator = By.xpath("//button[@data-test='Type']");
    private final By issueStateButtonLocator = By.xpath("//button[@data-test='State']");

    public IssueFieldsPanel(WebDriver driver) {
        super(driver, "", IssueFieldsPanel.class);
    }

    public Map<String, WebElement> getAvailablePriorities() {
        getPriorityButton().click();
        return mapWebElements(getRingListItems());
    }

    public Map<String, WebElement> getAvailableTypes() {
        getTypeButton().click();
        return mapWebElements(getRingListItems());
    }

    public Map<String, WebElement> getAvailableStates() {
        getStateButton().click();
        return mapWebElements(getRingListItems());
    }

    public String getProjectName() {
        return getWebElement(projectButtonLocator).getText();
    }

    public String getPriorityName() {
        return getPriorityButton().getText();
    }

    public String getTypeName() {
        return getTypeButton().getText();
    }

    public String getStateName() {
        return getStateButton().getText();
    }

    protected WebElement getPriorityButton() {
        return getWebElement(priorityButtonLocator);
    }

    protected WebElement getTypeButton() {
        return getWebElement(issueTypeButtonLocator);
    }

    protected WebElement getStateButton() {
        return getWebElement(issueStateButtonLocator);
    }
}
