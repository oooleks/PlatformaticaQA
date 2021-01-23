package model;

import org.openqa.selenium.WebDriver;

public final class DefaultPage extends BaseTablePage<DefaultPage, DefaultEditPage> {

    public DefaultPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected DefaultEditPage createEditPage() {
        return new DefaultEditPage(getDriver());
    }
}
