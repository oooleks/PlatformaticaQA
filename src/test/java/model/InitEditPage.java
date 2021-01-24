package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class InitEditPage extends BaseEditPage <InitPage>{

    @FindBy(xpath = "//input[@id='string']")
    private WebElement stringData;

    @FindBy(xpath = "//span//textarea[@id='text']")
    private WebElement textData;

    @FindBy(xpath = "//input[@id='int']")
    private WebElement intData;

    @FindBy(xpath = "//input[@id='decimal']")
    private WebElement decimalData;

    @FindBy(xpath = "//input[@id='date']")
    private WebElement dateData;

    @FindBy(xpath = "//input[@id='datetime']")
    private WebElement dateTimeData;

    @FindBy(xpath = "//div[@class='filter-option-inner-inner' and .='User 1 Demo']")
    private WebElement userData;

    @FindBy(xpath = "//div[@class='togglebutton']/label/input")
    private WebElement switchData;

    @FindBy(xpath = "//div[@class='filter-option-inner-inner' and .='Two']")
    private WebElement listData;

    public InitEditPage(WebDriver driver) {
        super(driver);
    }

    public ErrorPage clickSaveButtonReturnError() {
        saveButton.click();
        return new ErrorPage(getDriver());
    }

    public String getStringValue() {
     return stringData.getAttribute("value");
    }

    public String getTextDataValue() {
        return textData.getText();
    }

    public String getIntData() {
        return intData.getAttribute("value");
    }

    public String decimalDataValue() {
        return decimalData.getAttribute("value");
    }

    public String dateDataValue() {
        return dateData.getAttribute("value");
    }

    public String dateTimeDataValue() {
        return dateTimeData.getAttribute("value");
    }

    public String userDataText() {
        return userData.getText();
    }

    public boolean switchCheck() {
        return (switchData.isEnabled() == true);
    }

    public String listDataCheck() {
        return listData.getText();
    }

    @Override
    protected InitPage createPage(){
        return new InitPage(getDriver());
    };
}