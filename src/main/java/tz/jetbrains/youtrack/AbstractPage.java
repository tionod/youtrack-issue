package tz.jetbrains.youtrack;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tz.jetbrains.helper.ConfigHelper;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPage<T> {

    private final By pageLoaderLocator = By.xpath("//div[@class='yt-page-loader']");
    private final By ringListItemLabelLocator = By.xpath("//span[@data-test='ring-list-item-label']");

    private final Class<T> pageClass;

    protected final String url;
    protected final WebDriver driver;
    protected WebDriverWait wait;

    protected AbstractPage(WebDriver driver, String path, Class<T> pageClass) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.url = ConfigHelper.getYouTrackProperties().getUrl() + path;
        this.pageClass = pageClass;
    }

    protected T open() {
        return open(url, pageClass);
    }

    protected T open(String url, Class<T> pageClass) {
        driver.get(url);
        return PageFactory.initElements(driver, pageClass);
    }

    protected void waitPageLoading() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(pageLoaderLocator));
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
