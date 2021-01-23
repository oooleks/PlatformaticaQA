package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Main1Page extends BasePage {

    @FindBy(id = "pa-menu-item-45")
    private WebElement fieldsBtn;

    public Main1Page(WebDriver driver) {
        super(driver);
    }

    public Fields1Page clickFields(){
        fieldsBtn.click();

        return new Fields1Page(getDriver());
    }
}