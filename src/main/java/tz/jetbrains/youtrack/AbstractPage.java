package tz.jetbrains.youtrack;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tz.jetbrains.helper.ConfigHelper;

import java.time.Duration;
import java.util.List;

public abstract class AbstractPage<T> {

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

    public T open() {
        return open(url, pageClass);
    }

    protected T open(String url, Class<T> pageClass) {
        driver.get(url);
        return PageFactory.initElements(driver, pageClass);
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected WebElement getWebElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> getWebElements(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }
}
