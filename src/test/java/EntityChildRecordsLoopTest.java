import com.beust.jcommander.Strings;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectUtils;
import runner.type.Run;
import runner.type.RunType;

import java.util.List;
import java.util.Random;


@Run(run = RunType.Multiple)
public class EntityChildRecordsLoopTest extends BaseTest {

    private String startBalance = "1";
    private String value9 = "6";
    private int endBalanceD;

    private int numbersOfLines = 9;
    private double sumNumber = 0;
    private double[] firstValuesPassed = {0.00, 10.50, 11.00, 12.00, 13.00, 14.00, 1.00, 1.00, 2.50, 0.0};

    private void addingRowsByClickingOnGreenPlus(int n) {
        WebDriver driver = getDriver();
        WebElement greenPlus = driver.findElement(By.xpath("//button[@data-table_id='68']"));
        for (int i = 1; i <= n; i++) {
            ProjectUtils.click(driver, greenPlus);
        }
    }

    private int randomIntGeneration(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private void createNewChildLoopEmptyRecord() {
        WebDriver driver = getDriver();
        WebElement childRecordsLoop = driver.findElement(By.xpath("//p[contains (text(), 'Child records loop')]/parent::a"));
        ProjectUtils.click(driver, childRecordsLoop);
        WebElement createNew = driver.findElement(By.xpath("//i[contains(text(), 'create_new_folder')]/ancestor::a"));
        ProjectUtils.click(driver, createNew);
        addingRowsByClickingOnGreenPlus(numbersOfLines);
    }

    private void fillData(String xpath, String valueSend) {
        WebDriver driver = getDriver();
        WebElement line = driver.findElement(By.xpath(xpath));
        line.clear();
        line.sendKeys(valueSend);
        getWebDriverWait().until(d -> !Strings.isStringEmpty(line.getAttribute("value"))
                && !line.getAttribute("value").equals("0"));
    }

    private void deleteRows(int rowNumber) {
        WebDriver driver = getDriver();
        WebElement deleteLine = findElementUsingText("//i[@data-row=", String.valueOf(rowNumber), "  and contains(text(), 'clear')]");
        ProjectUtils.click(driver, deleteLine);
        getWebDriverWait().until(d -> !deleteLine.isDisplayed());
    }

    private WebElement findElementUsingText(String firstPartOfXpath, String rowDeleted, String lastPartOfXpath) {
        WebDriver driver = getDriver();
        return driver.findElement(By.xpath(firstPartOfXpath + rowDeleted + lastPartOfXpath));
    }

    private void goToChildLoop() {
        WebDriver driver = getDriver();
        WebElement childRecord = driver.findElement(By.xpath("//p[contains(text(), 'Child records loop')]/ancestor::a"));
        ProjectUtils.click(driver, childRecord);
        WebElement recordMenu = driver.findElement(By.xpath("//button[@class='btn btn-round btn-sm btn-primary dropdown-toggle']"));
        ProjectUtils.click(driver, recordMenu);
    }

    @Test
    public void checkStartEndBalanceBeforeSave() {

        WebDriver driver = getDriver();
        createNewChildLoopEmptyRecord();

        driver.findElement(By.xpath("//input[@id='start_balance']")).sendKeys(startBalance);

        WebElement endBalance = driver.findElement(By.xpath("//input[@id='end_balance']"));

        getWebDriverWait().until(d -> endBalance.getAttribute("value").equals(startBalance));

        sumNumber += Integer.parseInt(startBalance);

        for (int i = 0; i < firstValuesPassed.length; i++) {
            sumNumber += firstValuesPassed[i];
        }

        for (int i = 1; i < 10; i++) {
            fillData(String.format("//tr//textarea[@id='t-68-r-%d-amount']", i), String.valueOf(firstValuesPassed[i]));
        }

        WebElement lastLine = driver.findElement(By.xpath("//tr//textarea[@id='t-68-r-9-amount']"));

        getWebDriverWait().until(d -> lastLine.getAttribute("value").equals(String.valueOf(firstValuesPassed[9])));

        List<WebElement> tableLines = driver.findElements(By.xpath("//textarea[@class='pa-entity-table-textarea pa-table-field t-68-amount']"));
        Assert.assertEquals(tableLines.size(), firstValuesPassed.length - 1);

        getWebDriverWait().until(d -> endBalance.getAttribute("value").equals(String.valueOf((int)sumNumber)));

        deleteRows(4);
        deleteRows(6);

        final double sum = sumNumber - firstValuesPassed[4] - firstValuesPassed[6];
        getWebDriverWait().until(d -> endBalance.getAttribute("value").equals(String.valueOf((int)(sum))));

        addingRowsByClickingOnGreenPlus(randomIntGeneration(1, 5));

        final double endBalanceDigit = sum + Integer.parseInt(value9);
        endBalanceD =  (int) endBalanceDigit;

        tableLines.get(firstValuesPassed.length - 2).clear();
        tableLines.get(firstValuesPassed.length - 2).sendKeys(value9);

        getWebDriverWait().until(d -> (endBalance.getAttribute("value").equals(String.valueOf( (int) endBalanceDigit))));

        WebElement saveBtn = driver.findElement(By.xpath("//button[@id='pa-entity-form-save-btn']"));
        ProjectUtils.click(driver, saveBtn);

        String partOfXpath = Integer.toString((int) endBalanceDigit);

        getWebDriverWait().until(d -> driver.findElement(By.xpath("//div[contains(text(),'" + partOfXpath + "')]")).isDisplayed());
    }

    @Test(dependsOnMethods = "checkStartEndBalanceBeforeSave")
    public void checkStartEndBalanceInViewMode() {

        WebDriver driver = getDriver();
        goToChildLoop();
        WebElement viewFunction = driver.findElement(By.xpath("//a[text() = 'view']"));
        ProjectUtils.click(driver, viewFunction);

        String[] valuesArr = {startBalance + ".00", endBalanceD + ".00"};

        List<WebElement> startEndBalance = driver.findElements(By.xpath("//div/span[@class='pa-view-field']"));
        for (int i = 0; i < startEndBalance.size(); i++) {
            Assert.assertEquals(startEndBalance.get(i).getText(), valuesArr[i]);
        }
    }

    @Test(dependsOnMethods = "checkStartEndBalanceBeforeSave")
    public void checkStartEndBalanceInEditMode() {
        WebDriver driver = getDriver();
        goToChildLoop();
        WebElement editFunction = driver.findElement(By.xpath("//a[text() = 'edit']"));
        ProjectUtils.click(driver, editFunction);

        WebElement startBalanceField = driver.findElement(By.xpath("//input[@id='start_balance']"));
        WebElement endBalance = driver.findElement(By.xpath("//input[@id='end_balance']"));
        Assert.assertEquals(startBalanceField.getAttribute("value"), startBalance);
        Assert.assertEquals(endBalance.getAttribute("value"), String.valueOf(endBalanceD));
    }
}
