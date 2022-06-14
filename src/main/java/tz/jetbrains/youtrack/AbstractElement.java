package tz.jetbrains.youtrack;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractElement {

    private final By ringListItemLabelLocator = By.xpath("//span[@data-test='ring-list-item-label']");

    protected final WebDriver driver;
    protected WebDriverWait wait;

    protected AbstractElement(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement getWebElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> getWebElements(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected List<WebElement> getRingListItems() {
        return getWebElements(ringListItemLabelLocator);
    }

    protected Map<String, WebElement> mapWebElements(List<WebElement> elements) {
        Map<String, WebElement> map = new HashMap<>();
        elements.forEach(e -> map.put(e.getText().split("\n")[0], e));
        return map;
    }
}
