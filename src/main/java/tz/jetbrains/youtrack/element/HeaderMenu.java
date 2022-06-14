package tz.jetbrains.youtrack.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import tz.jetbrains.youtrack.AbstractElement;
import tz.jetbrains.youtrack.page.issue.NewIssuePage;

import java.util.Map;

public class HeaderMenu extends AbstractElement {

    private static final String SWITCH_UI_CHECKBOX_XPATH = "//div[@aria-label='Dropdown menu']" +
            "//input[@data-test='ring-toggle-input']";

    private final By profileButtonLocator = By.xpath("//div[@data-test='ring-dropdown ring-profile']/button");
    private final By issueButtonLocator = By.xpath("//button[@data-test='createIssueButton']");
    private final By appearanceProfileButtonLocator = By.xpath("//span[@data-test='appearance']");
    private final By issueLinkTypeDropboxLocator = By.xpath("//button[@data-test='issueLinkTypesDropdown']");
    private final By ringListItemLabelLocator = By.xpath("//div[@data-test='ring-list-item-label']");

    public HeaderMenu(WebDriver driver) {
        super(driver);
    }

    public NewIssuePage goToNewIssuePage() {
        getIssuerButton().click();
        return new NewIssuePage(driver);
    }

    public WebElement getIssuerButton() {
        return getWebElement(issueButtonLocator);
    }

    public void expandIssueLinkTypes() {
        getWebElement(issueLinkTypeDropboxLocator).click();
    }

    public Map<String, WebElement> getAvailableIssueLinkTypes() {
        expandIssueLinkTypes();
        return mapWebElements(getWebElements(ringListItemLabelLocator));
    }

    public void waitLoading() {
        wait.until(ExpectedConditions.attributeContains(issueButtonLocator, "data-test-loader", "true"));
    }

    public void switchUIToClassicMode() {
        getWebElement(profileButtonLocator).click();
        WebElement appearance = getWebElement(appearanceProfileButtonLocator);
        appearance.click();

        if (appearance.findElement(By.xpath(SWITCH_UI_CHECKBOX_XPATH)).isSelected()) {
            getWebElement(By.xpath(SWITCH_UI_CHECKBOX_XPATH + "/ancestor::span[@role='button']")).click();
        }
    }
}
