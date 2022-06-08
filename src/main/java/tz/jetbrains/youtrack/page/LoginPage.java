package tz.jetbrains.youtrack.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import tz.jetbrains.youtrack.AbstractPage;

public class LoginPage extends AbstractPage<LoginPage> {

    private final By usernameFieldLocator = By.xpath("//input[@data-test='username-field']");
    private final By passwordFieldLocator = By.xpath("//input[@data-test='password-field']");
    private final By loginButtonLocator = By.xpath("//button[@data-test='login-button']");

    public LoginPage(WebDriver driver) {
        super(driver, "/hub/auth/login", LoginPage.class);
    }

    public MainPage signIn(String username, String password) {
        setUsername(username);
        setPassword(password);
        getLoginButton().click();
        return new MainPage(driver);
    }

    public void setUsername(String username) {
        getWebElement(usernameFieldLocator).sendKeys(username);
    }

    public void setPassword(String password) {
        getWebElement(passwordFieldLocator).sendKeys(password);
    }

    public WebElement getLoginButton() {
        return getWebElement(loginButtonLocator);
    }
}
