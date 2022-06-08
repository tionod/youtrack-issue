package tz.jetbrains.youtrack.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tz.jetbrains.youtrack.page.issue.NewIssuePage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssueFieldsPanel extends NewIssuePage {

    private final By priorityButtonLocator = By.xpath("//button[@data-test='Priority']");
    private final By issueTypeButtonLocator = By.xpath("//button[@data-test='Type']");
    private final By issueStateButtonLocator = By.xpath("//button[@data-test='State']");
    private final By ringListItemLocator = By.xpath("//div[@data-test='ring-list-item-action ring-list-item']/button");

    public IssueFieldsPanel(WebDriver driver) {
        super(driver);
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

    public String getPriority() {
        return getPriorityButton().getText();
    }

    public String getType() {
        return getTypeButton().getText();
    }

    public String getState() {
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

    protected List<WebElement> getRingListItems() {
        return getWebElements(ringListItemLocator);
    }

    private Map<String, WebElement> mapWebElements(List<WebElement> elements) {
        Map<String, WebElement> map = new HashMap<>();
        elements.forEach(e -> map.put(e.getText().split("\n")[0], e));
        return map;
    }

}
