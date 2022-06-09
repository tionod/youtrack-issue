package tz.jetbrains.webdriver;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;
import java.util.Map;

@UtilityClass
public class WebDriverService {

    public static final String REMOTE_ADDRESS = "http://localhost:4444/wd/hub";
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static WebDriver getDriver() {
        initRemoteDriver();
        return DRIVER.get();
    }

    public static void removeDriver() {
        DRIVER.get().quit();
        DRIVER.remove();
    }

    private static void initRemoteDriver() {
        DRIVER.set(initRemoteDriver(getDefaultCapabilities()));
    }

    @SneakyThrows
    private static WebDriver initRemoteDriver(DesiredCapabilities capabilities) {
        RemoteWebDriver driver = new RemoteWebDriver(URI.create(REMOTE_ADDRESS).toURL(), capabilities);
        driver.setFileDetector(new LocalFileDetector());
        return driver;
    }

    private static DesiredCapabilities getDefaultCapabilities() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setCapability(
                "selenoid:options",
                Map.<String, Object>of(
                        "enableVNC", true,
                        "enableVideo", true
                )
        );

        return capabilities;
    }
}
