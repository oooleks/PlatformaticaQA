import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectUtils;
import runner.type.Run;
import runner.type.RunType;

import java.util.UUID;

@Run(run = RunType.Multiple)
public class EntityCalendarTest extends BaseTest {

    private static final String STRING = UUID.randomUUID().toString();
    private static final int NUMBER = 25;
    private static final double NUMBER1 = 56.23;
    private static final String TEXT_COMMENTS = "DON'T WAOORY, BE HAPPY!";
    private static final String TITLE_FIELD = UUID.randomUUID().toString();
    private static final String TITLE_FIELD_NEW = UUID.randomUUID().toString();

    @Test
    public void newCalendar() throws InterruptedException {

        WebDriver driver = getDriver();

        WebElement calendar = driver.findElement(By.xpath("//p[contains(text(),'Calendar')]"));
        ProjectUtils.click(driver, calendar);

        WebElement newCalendar = driver.findElement(By.xpath("//div[@class='card-icon']/i"));
        newCalendar.click();

        WebElement titleElement = driver.findElement(By.xpath("//input[contains(@name, 'string')]"));
        titleElement.sendKeys(STRING);

        WebElement numberElement = driver.findElement(By.xpath("//input[contains(@name, 'int')]"));
        numberElement.sendKeys(String.valueOf(NUMBER));

        WebElement number1Element = driver.findElement(By.xpath("//input[contains(@name, 'decimal')]"));
        number1Element.sendKeys(String.valueOf(NUMBER1));

        WebElement dateElement = driver.findElement(By.xpath("//input[contains(@name, 'date')]"));
        dateElement.click();
        Actions actions = new Actions(driver);
        actions.moveToElement(dateElement).build().perform();

        WebElement dateTimeElement = driver.findElement(By.xpath("//input[contains(@name, 'datetime')]"));
        dateTimeElement.click();
        Actions actions1 = new Actions(driver);
        actions1.moveToElement(dateTimeElement).build().perform();

        WebElement submit = driver.findElement(By.xpath("//button[text() = 'Save']"));
        ProjectUtils.click(driver, submit);

        WebElement listElement =
                getWebDriverWait().until(ExpectedConditions.elementToBeClickable(By.xpath("//div[2]/div[1]//div[1]/div/ul/li[2]/a")));
        listElement.click();

        driver.findElement(By.xpath("//div[contains(text(), '" + STRING + "')]"));
    }

    @Test(dependsOnMethods = "newCalendar")
    public void editCalendar() throws InterruptedException {

        WebDriver driver = getDriver();

        WebElement calendar = driver.findElement(By.xpath("//p[contains(text(),'Calendar')]"));
        ProjectUtils.click(driver, calendar);

        WebElement list = driver.findElement(By.xpath("//div[@class='content']//li[2]"));
        list.click();

        WebElement editList = driver.findElement(By.xpath("//td/div/button"));
        editList.click();

        WebElement clickEdit =
                getWebDriverWait().until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='edit']")));
        clickEdit.click();

        WebElement str =
                getWebDriverWait().until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='string']")));
        str.clear();
        ProjectUtils.sendKeys(str,"New Record");

        WebElement text = driver.findElement(By.xpath("//textarea[@id='text']"));
        text.clear();
        text.sendKeys(TEXT_COMMENTS);

        WebElement number = driver.findElement(By.xpath("//input[@id='int']"));
        number.sendKeys("777");

        WebElement save = driver.findElement(By.xpath("//button[normalize-space()='Save']"));
        ProjectUtils.click(driver, save);

        WebElement resultEdit = driver.findElement(By.xpath("//tr//td[3]"));
        Assert.assertEquals(resultEdit.getText(), TEXT_COMMENTS);
    }

    public void setValue(WebDriver driver, String title, String text, int num, double decimal) {

        WebElement titleField = driver.findElement(By.xpath("//input[@name='entity_form_data[string]']"));
        titleField.clear();
        titleField.sendKeys(title);

        WebElement textPlaceholder = driver.findElement(By.xpath("//textarea[@name='entity_form_data[text]']"));
        textPlaceholder.clear();
        textPlaceholder.sendKeys(text);

        WebElement fieldInt = driver.findElement(By.xpath("//input[@name='entity_form_data[int]']"));
        fieldInt.clear();
        fieldInt.sendKeys(String.valueOf(num));

        WebElement fieldDecimal = driver.findElement(By.xpath("//input[@name='entity_form_data[decimal]']"));
        fieldDecimal.clear();
        fieldDecimal.sendKeys(String.valueOf(decimal));

        WebElement saveBtn = driver.findElement(By.id("pa-entity-form-save-btn"));
        ProjectUtils.click(driver, saveBtn);

        WebElement listBtn = driver.findElement(By.xpath("//ul[@role='tablist']//i[contains(text(),'list')]"));
        listBtn.click();
    }

    @Test
    public void newRecord(){
        WebDriver driver = getDriver();

        WebElement tab = driver.findElement(By.xpath("//p[contains(text(),'Calendar')]"));
        ProjectUtils.click(driver, tab);

        WebElement createNewFolder = driver.findElement(By.xpath("//i[contains(text(),'create_new_folder')]"));
        createNewFolder.click();

        setValue(driver, TITLE_FIELD, "test_1", 342, 0);

        getWebDriverWait().until(driver1 -> driver.findElement(By.xpath("//tr[@data-index]")).isDisplayed());
    }

    @Test(dependsOnMethods = "newRecord")
    public void editRecord() {
        WebDriver driver = getDriver();

        WebElement tab = driver.findElement(By.xpath("//p[contains(text(),'Calendar')]"));
        ProjectUtils.click(driver, tab);

        WebElement listBtn = driver.findElement(By.xpath("//ul[@role='tablist']//i[contains(text(),'list')]"));
        listBtn.click();

        WebElement dropdown = driver.findElement(By.xpath("//div[@class='dropdown pull-left']"));
        dropdown.click();

        WebElement editBtn = driver.findElement(By.xpath("//a[contains(text(),'edit')]"));
        ProjectUtils.click(driver, editBtn);

        setValue(driver, TITLE_FIELD_NEW, "test test test", 256, 0.1);

        WebElement nameString = driver.findElement(By.xpath(String.format("//div[contains(text(),'%s')]" , TITLE_FIELD_NEW)));
        Assert.assertEquals(nameString.getText(), TITLE_FIELD_NEW);

        WebElement nameText = driver.findElement(By.xpath("//div[contains(text(),'test test test')]"));
        Assert.assertEquals(nameText.getText(), "test test test");

        WebElement intField = driver.findElement(By.xpath("//div[contains(text(),'256')]"));
        Assert.assertEquals(intField.getText(), "256");

        WebElement decimalField = driver.findElement(By.xpath("//div[contains(text(),'0.1')]"));
        Assert.assertEquals(decimalField.getText(), "0.1");
    }

    @Test(dependsOnMethods = {"newRecord" , "editRecord"})
    public void deleteRecord() {
        WebDriver driver = getDriver();

        WebElement tab = driver.findElement(By.xpath("//p[contains(text(),'Calendar')]"));
        ProjectUtils.click(driver, tab);

        WebElement listBtn = driver.findElement(By.xpath("//ul[@role='tablist']//i[contains(text(),'list')]"));
        listBtn.click();

        WebElement dropdownDelete = driver.findElement(By.xpath("//div[@class='dropdown pull-left']"));
        dropdownDelete.click();

        WebElement deleteBtn = driver.findElement(By.xpath("//a[contains(text(),'delete')]"));
        ProjectUtils.click(driver,deleteBtn);

        WebElement RecycleBin = driver.findElement(By.xpath("//i[contains(text(),'delete_outline')]"));
        RecycleBin.click();

        WebElement deleteRecord = driver.findElement(By.xpath(String.format("//b[contains(text(), '%s')]", TITLE_FIELD_NEW)));
        getWebDriverWait().until(driver1 -> deleteRecord.isDisplayed());
    }
}