import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;

public class EntityFieldsTest extends BaseTest {

    @Test
    public void newRecord() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://ref.eteam.work/");

        ProjectUtils.login(driver, "user1@tester.com", "ah1QNmgkEO");

        WebElement tab = driver.findElement(By.xpath("//li[@id='pa-menu-item-45']/a"));
        tab.click();
        WebElement newRecord = driver.findElement(By.xpath("//i[text()='create_new_folder']"));
        newRecord.click();

        final String title = TestUtils.getUUID();
        final String comment = "simple text";
        final int number = 10;

        WebElement titleElement = driver.findElement(By.xpath("//input[@name='entity_form_data[title]']"));
        titleElement.sendKeys(title);
        WebElement commentElement = driver.findElement(By.xpath("//textarea[@name='entity_form_data[comments]']"));
        commentElement.sendKeys(comment);
        WebElement numberElement = driver.findElement(By.xpath("//input[@name='entity_form_data[int]']"));
        numberElement.sendKeys(String.valueOf(number));

        WebElement submit = driver.findElement(By.id("pa-entity-form-save-btn"));
        ProjectUtils.click(driver, submit);

        // validation of record
        driver.findElement(By.xpath(String.format("//div[contains(text(), '%s')]", title)));
//        driver.findElement(By.xpath("//div[contains(text(), '" + title + "')]"));
        System.out.println("Title created: " + title);

        String record_title = driver.findElement(By.xpath("/html/body/div/div[2]/div[1]/div/div/div/div/div/div/div/div[2]/div[1]/div[2]/div[2]/table/tbody/tr[7]/td[2]/a/div")).getText();
//        System.out.println(record_title);
        Assert.assertEquals(record_title, title);

//        add delete
//        driver.findElement(By.xpath("//*[@id=\"pa-all-entities-table\"]
        Thread.sleep(2000);

    }
}
