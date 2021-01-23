package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import runner.ProjectUtils;

public class FieldsEdit1Page extends BasePage{

    @FindBy(xpath = "//input[contains(@name, 'title')]")
    private WebElement inputTitle;

    @FindBy(xpath = "//textarea[@id = 'comments']")
    private WebElement inputComments;

    @FindBy(xpath = "//input[contains(@name, 'int')]")
    private WebElement inputInt;

    @FindBy(xpath = "//button[text() = 'Save']")
    private WebElement saveBtn;

    public FieldsEdit1Page(WebDriver driver) {
        super(driver);
    }

    public FieldsEdit1Page sendKeys(String title, String comments, String int_){
        inputTitle.clear();
        inputComments.clear();
        inputInt.clear();

        ProjectUtils.sendKeys(inputTitle, title);
        ProjectUtils.sendKeys(inputComments, comments);
        ProjectUtils.sendKeys(inputInt, int_);

        return new FieldsEdit1Page(getDriver());
    }

    public Fields1Page clickSaveBtn(){
        ProjectUtils.click(getDriver(), saveBtn);

        return new Fields1Page(getDriver());
    }
}
