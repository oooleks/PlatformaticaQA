package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static runner.ProjectUtils.fill;

public final class ExportEditPage extends BaseEditPage<ExportPage> {

    @FindBy(id = "int")
    private WebElement inputInt;

    @Override
    protected ExportPage createPage() {
        return new ExportPage(getDriver());
    }

    public ExportEditPage(WebDriver driver) {
        super(driver);
    }

    public ExportEditPage fillInt (String int_) {
        fill(getWait(),inputInt,int_);
        return this;
    }
}
