import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;

import java.util.List;

public class EntityLoop1Test extends BaseTest {

    private WebDriverWait getWait(int timeoutSecond) {
        return new WebDriverWait(getDriver(), timeoutSecond);
    }

    @Test
    public void loop1Stops() throws InterruptedException {

        WebDriver driver = getDriver();
        driver.get("https://ref.eteam.work/");

        ProjectUtils.login(driver, "user2@tester.com", "5of75zUQFq");

        WebElement loop_1 = driver.findElement(By.xpath("//p[contains(text(),'Loop 1')]"));
        ProjectUtils.click(driver, loop_1);

        String entity_name = driver.findElement(By.xpath("//h3[@class='card-title']")).getText();
        Assert.assertEquals(entity_name, "Loop 1");

        final int number_1 = 1;
        final int number_2 = 0;

        // Create Loop with F1 = 1
        WebElement new_loop = driver.findElement(By.xpath("//i[text()='create_new_folder']"));
        new_loop.click();
        WebElement f1_element = driver.findElement(By.name("entity_form_data[f1]"));
        f1_element.sendKeys(String.valueOf(number_1));

        // Waite until 1000
        boolean loop_end = false;
        while (!loop_end) {
            Thread.sleep(3000);
            System.out.println("F1: " + f1_element.getAttribute("value"));
            if (Integer.parseInt(f1_element.getAttribute("value")) >= 10) { // up to 10
                loop_end = true;
            }
        }
        WebElement save_button = driver.findElement(By.id("pa-entity-form-save-btn"));
        save_button.click();
        Thread.sleep(2000);

        // validation of record
        List<WebElement> record_rows_we = driver.findElements(By.cssSelector("tbody > tr"));
        if (record_rows_we.size() == 0) {
            Assert.fail("No \"Loop 1\" entity records found after creating one record");
        }

        int num_of_all_records = Integer.parseInt(getWait(1).until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//span[@class='pagination-info']")))
                .getText().split(" ")[5]);

        int rows_per_page;
        WebElement rows_per_page_we = driver.findElement(By.cssSelector("span.page-size"));
        if (rows_per_page_we.isDisplayed()) {
            rows_per_page_we.click();
            Thread.sleep(500);
            driver.findElement(By.linkText("10")).click();
            int page_to_go_num = (int)Math.ceil(num_of_all_records / (double)10);
            WebElement page_number = driver.findElement
                    (By.cssSelector("a[aria-label='to page " + page_to_go_num + "']"));
            page_number.click();
            Thread.sleep(500);
        }
        WebElement actions_button_of_created_record =
                driver.findElement(By.xpath("//tr[@data-index='" + (num_of_all_records - 1) + "']/td/div/button"));
        actions_button_of_created_record.click();
        Thread.sleep(500);
        driver.findElement(By.linkText("edit")).click();
        Thread.sleep(2000);
        save_button.click();

        // cleanup, delete created record
//        String recordTitleXpath = String.format("//div[contains(text(), '%s')]", title);
//
//        driver.findElement(By.xpath(String.format("%s/../../..//button", recordTitleXpath))).click();
//        String delBtnXpath = String.format("%s/../../..//a[contains(@href, 'delete')]", recordTitleXpath);
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(delBtnXpath))).click();
//        Thread.sleep(2000);

    }
}
