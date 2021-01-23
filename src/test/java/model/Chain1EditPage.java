package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import runner.ProjectUtils;

public class Chain1EditPage extends BaseEditPage<Chain1Page> {

    @FindBy(id = "f1")
    WebElement f1;

    @FindBy(id = "f10")
    WebElement f10;

    @FindBy(id = "pa-entity-form-save-btn")
    WebElement saveButton;


    public Chain1EditPage(WebDriver driver) {
        super(driver);
    }
    @Override
    protected Chain1Page createPage() {
        return new Chain1Page(getDriver());
    }



    public Chain1EditPage inputInitialValue() {
        ProjectUtils.sendKeys(f1, "1");
        getWait().until(ExpectedConditions.attributeToBeNotEmpty(f10, "value"));
        return this;
    }


}


