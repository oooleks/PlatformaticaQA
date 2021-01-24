package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static runner.ProjectUtils.fill;

public final class ChevronEditPage extends BaseEditPage<ChevronPage> {

    public ChevronEditPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//div[contains(text(),'Pending')]")
    private WebElement statusButton;

    @FindBy(xpath = "//span[contains(text(),'Fulfillment')]")
    private WebElement fulfillmentButton;

    @FindBy(xpath = "//textarea[@id = 'text']")
    private WebElement inputString;

    @FindBy(xpath = "//input[@id = 'int']")
    private WebElement inputInt;

    @FindBy(xpath = "//input[@id = 'decimal']")
    private WebElement inputDecimal;

    @FindBy(xpath = "//input[@id = 'date']")
    private WebElement inputDate;

    @FindBy(xpath = "//input[@id = 'datetime']")
    private WebElement inputDateTime;

    @Override
    protected ChevronPage createPage() {
        return new ChevronPage(getDriver());
    }

    public ChevronEditPage chooseRecordStatus() {
        statusButton.click();
        getActions().moveToElement(fulfillmentButton).perform();
        fulfillmentButton.click();
        return this;
    }

    public ChevronEditPage sendKeys(String comments, String int_, String decimal, String Time, String Data) {
        fill(getWait(), inputString, comments);
        fill(getWait(), inputInt, int_);
        fill(getWait(), inputDecimal, decimal);
        fill(getWait(), inputDate, Data);
        fill(getWait(), inputDateTime, Time);
        return this;
    }
}
