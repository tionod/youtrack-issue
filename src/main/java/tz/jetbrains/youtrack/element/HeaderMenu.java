package tz.jetbrains.youtrack.element;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tz.jetbrains.youtrack.AbstractPage;

public class HeaderMenu extends AbstractPage<HeaderMenu> {

    private static final String SWITCH_UI_CHECKBOX_XPATH = "//div[@aria-label='Dropdown menu']" +
            "//input[@data-test='ring-toggle-input']";

    private final By profileButtonLocator = By.xpath("//div[@data-test='ring-dropdown ring-profile']/button");
    private final By appearanceProfileButtonLocator = By.xpath("//span[@data-test='appearance']");

    public HeaderMenu(WebDriver driver) {
        super(driver, "", HeaderMenu.class);
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
