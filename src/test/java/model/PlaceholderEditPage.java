package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import runner.ProjectUtils;

import static runner.ProjectUtils.fill;

public final class PlaceholderEditPage  extends BasePage{

    @FindBy(xpath = "//input[@placeholder='String placeholder']")
    private WebElement newTitle;

    @FindBy(xpath = "//textarea[@placeholder='Text placeholder']")
    private WebElement newComments;

    @FindBy(xpath = "//input[@id='int']")
    private WebElement newInt;

    @FindBy(xpath = "//input[@id='decimal']")
    private WebElement newDecimal;

    @FindBy(xpath = "//div[contains(text(),'Demo')]")
    private WebElement userSelection;

    @FindBy(xpath = "//span[text()='User 2']")
    private WebElement newUser;

    @FindBy(id = "pa-entity-form-save-btn")
    private WebElement saveButton;

    public PlaceholderEditPage(WebDriver driver) {
        super(driver);
    }

    public PlaceholderEditPage sendKeys(String title,String comments,String int_,String decimal){
        fill(getWait(),newTitle,title);
        fill(getWait(),newComments,comments);
        fill(getWait(),newInt,int_);
        fill(getWait(),newDecimal,decimal);

        return this;
    }

    public PlaceholderEditPage selectUser(){
        ProjectUtils.click(getDriver(),userSelection);
        ProjectUtils.click(getDriver(),newUser);
        return this;
    }

    public PlaceholderPage clickSaveButton(){
        ProjectUtils.click(getDriver(),saveButton);
        return new PlaceholderPage(getDriver());
    }
}
