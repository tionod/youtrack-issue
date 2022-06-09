package tz.jetbrains.youtrack.page;

import org.openqa.selenium.WebDriver;
import tz.jetbrains.youtrack.AbstractPage;

public class DashboardPage extends AbstractPage<DashboardPage> {

    public DashboardPage(WebDriver driver) {
        super(driver, "/dashboard", DashboardPage.class);
    }
}
