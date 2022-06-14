package tz.jetbrains.youtrack;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tz.jetbrains.helper.ConfigHelper;

import java.time.Duration;

public abstract class AbstractPage<T> extends AbstractElement {

    private final By pageLoaderLocator = By.xpath("//div[@class='yt-page-loader']");

    private final Class<T> pageClass;

    protected final String url;
    protected WebDriverWait wait;

    protected AbstractPage(WebDriver driver, String path, Class<T> pageClass) {
        super(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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
}
