package tz.jetbrains.youtrack;

import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import tz.jetbrains.webdriver.WebDriverService;

public class BaseHooks {
    protected static Faker faker = new Faker();
    protected WebDriver webDriver;

    @BeforeEach
    void setup() {
        webDriver = WebDriverService.getDriver();
    }

    @AfterEach
    void teardown() {
        WebDriverService.removeDriver();
    }
}
