import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import runner.BaseTest;

public class EntityFootersTest extends BaseTest {

    @Ignore
    @Test
    public void sumFooter(){
    }

    @Ignore
    @Test
    public void minFooter(){
    }

    @Ignore
    @Test
    public void maxFooter(){
    }


    @Test
    public void countFooter(){

        WebDriver driver = getDriver();
        WebDriverWait wait = getWebDriverWait();

        WebElement footerTab = driver.findElement(By.xpath("//p[contains(text(),'Footers')]"));
        footerTab.click();

        WebElement createNewFooter = driver.findElement(By.xpath("//i[contains(text(),'create_new_folder')]"));
        createNewFooter.click();

        WebElement newRow = driver.findElement(By.xpath("//button[@data-table_id='73']"));
        newRow.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[@id='row-73-1']")));
        newRow.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[@id='row-73-2']")));
        newRow.click();
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//textarea[@id='f-73-control']"),"3"));
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//input[@data-field_name='count_control']"),"3"));

        WebElement deleteRow = driver.findElement(By.xpath("//i[@data-row='3']"));
        deleteRow.click();
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//textarea[@id='f-73-control']"),"2"));
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//input[@data-field_name='count_control']"),"2"));
    }

    @Test
    public void aveFooter(){

        WebDriver driver = getDriver();
        WebDriverWait wait = getWebDriverWait();

        WebElement footerTab = driver.findElement(By.xpath("//p[contains(text(),'Footers')]"));
        footerTab.click();

        WebElement createNewFooter = driver.findElement(By.xpath("//i[contains(text(),'create_new_folder')]"));
        createNewFooter.click();

        WebElement newRow = driver.findElement(By.xpath("//button[@data-table_id='74']"));
        newRow.click();
        WebElement inputInt1 = driver.findElement(By.xpath("//textarea[@id='t-74-r-1-int']"));
        inputInt1.clear();
        inputInt1.sendKeys("100");
        WebElement inputDec1 = driver.findElement(By.xpath("//textarea[@id='t-74-r-1-decimal']"));
        inputDec1.clear();
        inputDec1.sendKeys("2.0");

        newRow.click();
        WebElement inputInt2 = driver.findElement(By.xpath("//textarea[@id='t-74-r-2-int']"));
        inputInt2.clear();
        inputInt2.sendKeys("-1");
        WebElement inputDec2 = driver.findElement(By.xpath("//textarea[@id='t-74-r-2-decimal']"));
        inputDec2.clear();
        inputDec2.sendKeys("10.1");

        newRow.click();
        WebElement inputInt3 = driver.findElement(By.xpath("//textarea[@id='t-74-r-3-int']"));
        inputInt3.clear();
        inputInt3.sendKeys("1");
        WebElement inputDec3 = driver.findElement(By.xpath("//textarea[@id='t-74-r-3-decimal']"));
        inputDec3.clear();
        inputDec3.sendKeys("-1.1");

        WebElement randomClick = driver.findElement(By.xpath("//textarea[@id='t-74-r-3-control']"));
        randomClick.click();

        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//textarea[@id='f-74-control']"),"33.33,3.67"));
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//input[@data-field_name='average_control']"),"33.33,3.67"));

        inputDec3.clear();
        inputDec3.sendKeys("0.5");
        randomClick.click();
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//textarea[@id='f-74-control']"),"33.33,4.2"));
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//input[@data-field_name='average_control']"),"33.33,4.2"));

        WebElement deleteRow = driver.findElement(By.xpath("//i[@data-row='2']"));
        deleteRow.click();
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//textarea[@id='f-74-control']"),"50.5,1.25"));
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//input[@data-field_name='average_control']"),"50.5,1.25"));
    }


    @Test
    public void saveFooter() throws InterruptedException{

        WebDriver driver = getDriver();
        WebDriverWait wait = getWebDriverWait();

        WebElement footerTab = driver.findElement(By.xpath("//p[contains(text(),'Footers')]"));
        footerTab.click();

        WebElement createNewFooter = driver.findElement(By.xpath("//i[contains(text(),'create_new_folder')]"));
        createNewFooter.click();

        WebElement countNewRow = driver.findElement(By.xpath("//button[@data-table_id='73']"));
        countNewRow.click();

        WebElement averageNewRow = driver.findElement(By.xpath("//button[@data-table_id='74']"));
        averageNewRow.click();

        WebElement inputInt1 = driver.findElement(By.xpath("//textarea[@id='t-74-r-1-int']"));
        inputInt1.clear();
        inputInt1.sendKeys("200");
        WebElement inputDec1 = driver.findElement(By.xpath("//textarea[@id='t-74-r-1-decimal']"));
        inputDec1.clear();
        inputDec1.sendKeys("2.2");

        WebElement randomClick = driver.findElement(By.xpath("//textarea[@id='t-74-r-1-control']"));
        randomClick.click();
        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//textarea[@id='f-74-decimal']"),"2.2"));

        randomClick.click();

        wait.until(ExpectedConditions.textToBePresentInElementValue(By.xpath("//textarea[@id='f-74-control']"),"200,2.2"));

        Thread.sleep(2000);
        WebElement saveButton = driver.findElement(By.xpath("//button[@id='pa-entity-form-save-btn']"));
        saveButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//table[@id='pa-all-entities-table']")));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'200,2.2')]")));
    }


    @Ignore
    @Test
    public void viewFooter(){
    }

    @Ignore
    @Test
    public void editFooter(){
    }

    @Ignore
    @Test
    public void deleteFooter(){
    }
}
