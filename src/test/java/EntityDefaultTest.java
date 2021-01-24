import model.DefaultEditPage;
import model.DefaultPage;
import model.MainPage;
import model.RecycleBinPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectUtils;
import runner.type.Run;
import runner.type.RunType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Run(run = RunType.Multiple)

public class EntityDefaultTest extends BaseTest {

    private class FieldValues {
        String lineNumber;
        String fieldString;
        String fieldText;
        String fieldInt;
        String fieldDecimal;
        String fieldDate;
        String fieldDateTime;
        String fieldUser;

        private FieldValues(String lineNumber, String fieldString, String fieldText, String fieldInt, String fieldDecimal, String fieldDate,
                            String fieldDateTime, String fieldUser) {
            this.lineNumber = lineNumber;
            this.fieldString = fieldString;
            this.fieldText = fieldText;
            this.fieldInt = fieldInt;
            this.fieldDecimal = fieldDecimal;
            this.fieldDate = fieldDate;
            this.fieldDateTime = fieldDateTime;
            this.fieldUser = fieldUser;
        }
    }

    private static final By BY_EMBEDD_STRING = By.xpath("//td/textarea[@id='t-11-r-1-string']");
    private static final By BY_EMBEDD_TEXT = By.xpath("//td/textarea[@id='t-11-r-1-text']");
    private static final By BY_EMBEDD_INT = By.xpath("//td/textarea[@id='t-11-r-1-int']");
    private static final By BY_EMBEDD_DECIMAL = By.xpath("//td/textarea[@id='t-11-r-1-decimal']");
    private static final By BY_EMBEDD_DATE = By.id("t-11-r-1-date");
    private static final By BY_EMBEDD_DATETIME = By.id("t-11-r-1-datetime");
    private static final By BY_EMBEDD_USER = By.xpath("//select[@id='t-11-r-1-user']/option[@value='0']");
    private static final By BY_SAVE_BUTTON = By.xpath("//button[.='Save']");
    private static final By BY_RECORD_HAMBURGER_MENU = By.xpath("//button[contains(@data-toggle, 'dropdown')] ");
    private static final By BY_EMBEDD_DROPDOWN = By.xpath("//select[@id='t-11-r-1-user']");
    private static final By BY_VIEW = By.xpath("//a[text() = 'view']");

    private final FieldValues defaultValues = new FieldValues(
            null,
            "DEFAULT STRING VALUE",
            "DEFAULT TEXT VALUE",
            "55",
            "110.32",
            "01/01/1970",
            "01/01/1970 00:00:00",
            "USER 1 DEMO");

    private final FieldValues defaultEmbeDValues = new FieldValues(
            "1",
            "Default String",
            "Default text",
            "77",
            "153.17",
            "",
            "",
            "Not selected");

    private final FieldValues changedDefaultValues = new FieldValues(
            null,
            "Changed default String",
            "Changed default Text",
             String.valueOf((int) (Math.random() * 100)),
             "33.33", //String.valueOf((int) (Math.random()*20000) / 100.0),
            "01/01/2021",
            "01/01/2021 12:34:56",
            "user115@tester.com");

    private final FieldValues changedEmbedDValues = new FieldValues(
            "1",
            "Changed EmbedD String",
            "Changed EmbedD Text",
            String.valueOf((int) (Math.random() * 100)),
            "55.55",   //String.valueOf((int) (Math.random()*20000) / 100.0 after a bug will be fixed),
            "12/12/2020",
            "12/12/2020 00:22:22",
            "User 4");

    private final FieldValues newValues = new FieldValues(
            null,
            UUID.randomUUID().toString(),
            "Some random text as Edited Text Value",
             String.valueOf((int) (Math.random() * 100)),
            "77.77",    //String.valueOf((int) (Math.random()*20000) / 100.0 after a bug will be fixed),
            "30/12/2020",
            "30/12/2020 12:34:56",
            "user100@tester.com");

    private final List<String> DEFAULT_VALUES = new ArrayList<>(Arrays.asList(defaultValues.fieldString, defaultValues.fieldText,
            defaultValues.fieldInt, defaultValues.fieldDecimal,
            defaultValues.fieldDate, defaultValues.fieldDateTime, defaultValues.fieldUser));

    private final List<String> NEW_VALUES = new ArrayList<>(Arrays.asList("", newValues.fieldString, newValues.fieldText,
                  newValues.fieldInt, newValues.fieldDecimal,
                  newValues.fieldDate, newValues.fieldDateTime, "", "", newValues.fieldUser, "menu"));

    private final String[] CHANGED_DEFAULT_VALUES = {changedDefaultValues.fieldString,
                   changedDefaultValues.fieldText, changedDefaultValues.fieldInt, changedDefaultValues.fieldDecimal,
                   changedDefaultValues.fieldDate, changedDefaultValues.fieldDateTime};

    private final String[] CHANGED_EMBEDD_VALUES = {changedEmbedDValues.lineNumber, changedEmbedDValues.fieldString,
                   changedEmbedDValues.fieldText, changedEmbedDValues.fieldInt, changedEmbedDValues.fieldDecimal,
                   changedEmbedDValues.fieldDate, changedEmbedDValues.fieldDateTime, null, null, changedEmbedDValues.fieldUser};

    private void assertAndReplace(WebDriver driver, By by, String oldValue, String newValue, boolean isEmbedD ) {
        WebElement element = driver.findElement(by);
        if (isEmbedD) {
            Assert.assertEquals(element.getText(), oldValue);
            element.click();
        } else {
            Assert.assertEquals(element.getAttribute("value"), oldValue);
        }
        element.clear();
        element.sendKeys(newValue);
        element.sendKeys("\t");
    }

    private void assertAndReplaceFieldUser(WebDriver driver, String oldValue, String newValue, By byUser, By bySelect ){
        WebElement fieldUser = driver.findElement(byUser);
        Assert.assertEquals(fieldUser.getText(), oldValue);
        Select userSelect = new Select(driver.findElement(bySelect));
        userSelect.selectByVisibleText(newValue);
    }

    private void selectFromRecordMenu (WebDriver driver, By byFunction) {

        driver.findElement(BY_RECORD_HAMBURGER_MENU).click();

        WebElement viewFunction = driver.findElement(byFunction);
        ProjectUtils.click(driver, viewFunction);
    }

    private void assertRecordValues(WebDriver driver, String xpath, String[] changed_default_values) {
        List<WebElement> rows = driver.findElements(By.xpath(xpath));
        for (int i = 0; i < changed_default_values.length; i++) {
            if (changed_default_values[i] != null) {
                Assert.assertEquals(rows.get(i).getText(), changed_default_values[i]);
            }
        }
    }

    @Test
    public void checkDefaultValuesAndUpdate() {

        WebDriver driver = getDriver();

        MainPage mainPage = new MainPage(getDriver());
        DefaultEditPage defaultEditPage = mainPage
                .clickMenuDefault()
                .clickNewFolder();

        Assert.assertEquals(defaultEditPage.toList(), DEFAULT_VALUES);


        defaultEditPage.sendKeys(changedDefaultValues.fieldString, changedDefaultValues.fieldText, changedDefaultValues.fieldInt,
                changedDefaultValues.fieldDecimal, changedDefaultValues.fieldDate, changedDefaultValues.fieldDateTime, changedDefaultValues.fieldUser);

        WebElement createEmbedD = driver.findElement(By.xpath("//button[@data-table_id='11']"));
        ProjectUtils.click(driver, createEmbedD);

        WebElement lineNumber = driver.findElement(By.xpath("//input[@id='t-undefined-r-1-_line_number']"));
        Assert.assertEquals(lineNumber.getAttribute("data-row"), changedEmbedDValues.lineNumber);

        assertAndReplace(driver, BY_EMBEDD_STRING, defaultEmbeDValues.fieldString, changedEmbedDValues.fieldString, false);
        assertAndReplace(driver, BY_EMBEDD_TEXT, defaultEmbeDValues.fieldText, changedEmbedDValues.fieldText, false);
        assertAndReplace(driver, BY_EMBEDD_INT, defaultEmbeDValues.fieldInt, changedEmbedDValues.fieldInt, false);
        assertAndReplace(driver, BY_EMBEDD_DECIMAL, defaultEmbeDValues.fieldDecimal, changedEmbedDValues.fieldDecimal, false);
        assertAndReplace(driver, BY_EMBEDD_DATE, defaultEmbeDValues.fieldDate, changedEmbedDValues.fieldDate, true);
        assertAndReplace(driver, BY_EMBEDD_DATETIME, defaultEmbeDValues.fieldDateTime, changedEmbedDValues.fieldDateTime, true);
        assertAndReplaceFieldUser(driver,  defaultEmbeDValues.fieldUser,changedEmbedDValues.fieldUser, BY_EMBEDD_USER, BY_EMBEDD_DROPDOWN);

        WebElement saveBtn = driver.findElement(BY_SAVE_BUTTON);
        ProjectUtils.click(driver, saveBtn);

        selectFromRecordMenu(driver, BY_VIEW);

        assertRecordValues(driver, "//span[@class='pa-view-field']", CHANGED_DEFAULT_VALUES);

        WebElement fieldUser = driver.findElement(By.xpath("//div[@class='form-group']/p"));
        Assert.assertEquals(fieldUser.getText(), changedDefaultValues.fieldUser);

        assertRecordValues(driver, "//table/tbody/tr/td", CHANGED_EMBEDD_VALUES);
    }

    @Test (dependsOnMethods = "checkDefaultValuesAndUpdate")
    public void deleteRecord() {

        MainPage mainPage  = new MainPage(getDriver());
        DefaultPage defaultPage = mainPage.clickMenuDefault();

        defaultPage.deleteRow();

        Assert.assertEquals(defaultPage.getRowCount(), 0);

        RecycleBinPage recycleBinPage = mainPage.clickRecycleBin();

        Assert.assertEquals(recycleBinPage.getRowCount(), 1);
        Assert.assertEquals(recycleBinPage.getFirstCellValue(0), changedDefaultValues.fieldString);

        recycleBinPage.clickDeletePermanently(0);
        Assert.assertEquals(recycleBinPage.getRowCount(), 0);
    }

    @ Test (dependsOnMethods = "deleteRecord")
    public void editExistingRecord() {

        MainPage mainPage = new MainPage(getDriver());
        DefaultPage defaultPage = mainPage
                .clickMenuDefault()
                .clickNewFolder()
                .clickSaveButton();

        DefaultEditPage defaultEditPage = defaultPage.editRow(0);

        defaultEditPage.sendKeys(newValues.fieldString, newValues.fieldText, newValues.fieldInt,
                newValues.fieldDecimal, newValues.fieldDate, newValues.fieldDateTime, newValues.fieldUser);

        defaultPage = defaultEditPage.clickSaveButton();
        Assert.assertEquals(defaultPage.getRowCount(), 1);

        Assert.assertEquals(defaultPage.getRow(0, "//td"), NEW_VALUES);
    }
}