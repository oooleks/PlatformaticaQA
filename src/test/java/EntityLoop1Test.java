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

    private int allRecords(WebDriver driver) {
        return Integer.parseInt(getWait(1).until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//span[@class='pagination-info']")))
                .getText().split(" ")[5]);
    }

    private void actionForLastRecord(WebDriver driver) throws InterruptedException {
        List<WebElement> record_rows_we = driver.findElements(By.cssSelector("tbody > tr"));
        if (record_rows_we.size() == 0) {
            Assert.fail("No \"Loop 1\" entity records found after creating one record");
        }

        WebElement rows_per_page_we = driver.findElement(By.cssSelector("span.page-size"));
        if (rows_per_page_we.isDisplayed()) {
            rows_per_page_we.click();
            Thread.sleep(500);
            driver.findElement(By.linkText("10")).click();
            int page_to_go_num = (int)Math.ceil(allRecords(driver) / (double)10);
            WebElement page_number = driver.findElement
                    (By.cssSelector("a[aria-label='to page " + page_to_go_num + "']"));
            page_number.click();
            Thread.sleep(500);
        }

        WebElement actions_button_of_created_record =
                driver.findElement(By.xpath("//tr[@data-index='" + (allRecords(driver) - 1) + "']/td/div/button"));
        actions_button_of_created_record.click();
        Thread.sleep(500);
    }

    private void waitUntilStop(WebElement f1_element, int target_num) throws InterruptedException {
        if (target_num == 1000) {
            while (Integer.parseInt(f1_element.getAttribute("value")) < target_num) {
                Thread.sleep(3000);
                // Debug - to delete
                System.out.println("F1: " + f1_element.getAttribute("value"));
            }
        } else {
            if (target_num == 999) {
                while (Integer.parseInt(f1_element.getAttribute("value")) < 999) {
                    Thread.sleep(3000);
                    // Debug - to delete
                    System.out.println("F1: " + f1_element.getAttribute("value"));
                }
            } else {
                Assert.fail("Wrong target number provided to initiate loop (accepted values: 1000 or 999)");
            }
        }
    }

    private void assertLoopValues(WebDriver driver, int value, String mode) throws InterruptedException {
        if (mode.equals("view")) {
            driver.findElement(By.linkText(mode)).click();
            Thread.sleep(500);
            int f1 = Integer.parseInt(driver.findElement
                    (By.xpath("//label[(text()='F1')]/following-sibling::div/child::div/child::span")).getText());
            // Debug - to delete
            System.out.println(f1);
            Assert.assertEquals(value, f1);

            int f2 = Integer.parseInt(driver.findElement
                    (By.xpath("//label[(text()='F2')]/following-sibling::div/child::div/child::span")).getText());
            // Debug - to delete
            System.out.println(f2);
            value += 1;
            Assert.assertEquals(value, f2);

            int f3 = Integer.parseInt(driver.findElement
                    (By.xpath("//label[(text()='F3')]/following-sibling::div/child::div/child::span")).getText());
            // Debug - to delete
            System.out.println(f3);
            value += 1;
            Assert.assertEquals(value, f3);
            driver.navigate().back();
        } else {
            if (mode.equals("edit")) {
                driver.findElement(By.linkText(mode)).click();
                Thread.sleep(500);
                int f1 = Integer.parseInt(driver.findElement
                        (By.xpath("//div[@id='_field_container-f1']/child::span/child::input")).getAttribute("value"));
                // Debug - to delete
                System.out.println("Edit F1: " + f1);
                Assert.assertEquals(value, f1);

                int f2 = Integer.parseInt(driver.findElement
                        (By.xpath("//div[@id='_field_container-f2']/child::span/child::input")).getAttribute("value"));
                // Debug - to delete
                System.out.println("Edit F2: " + f2);
                value += 1;
                Assert.assertEquals(value, f2);

                int f3 = Integer.parseInt(driver.findElement
                        (By.xpath("//div[@id='_field_container-f3']/child::span/child::input")).getAttribute("value"));
                // Debug - to delete
                System.out.println("Edit F3: " + f3);
                value += 1;
                Assert.assertEquals(value, f3);
                ProjectUtils.click(driver, driver.findElement(By.xpath("//button[text()='Cancel']/parent::a")));
            } else {
                Assert.fail("Wrong mode provided to assert loop values (accepted values: view or edit)");
            }
        }
    }

    @Test
    public void loop1Stops() throws InterruptedException {

        WebDriver driver = getDriver();
        driver.get("https://ref.eteam.work/");

        ProjectUtils.login(driver, "user5@tester.com", "hby0INEHw3");

        WebElement loop_1 = driver.findElement(By.xpath("//p[contains(text(),'Loop 1')]"));
        ProjectUtils.click(driver, loop_1);

        String entity_name = driver.findElement(By.xpath("//h3[@class='card-title']")).getText();
        Assert.assertEquals(entity_name, "Loop 1");

        final int number_1 = 1;
        final int number_2 = 0;

        // Create Loop with F1 = 1
        WebElement new_loop = driver.findElement(By.xpath("//i[text()='create_new_folder']"));
        new_loop.click();
        WebElement f1_element = driver.findElement(By.xpath("//div[@id='_field_container-f1']/child::span/child::input"));
        f1_element.sendKeys(String.valueOf(number_1));

        waitUntilStop(f1_element, 1000);
        Thread.sleep(1000);

        // Debug - to delete
        System.out.println("Loop stopped and F1 = " + f1_element.getAttribute("value"));
        // 5. Verify that F1 reached 1000 and the loop stopped.
//        Assert.assertEquals("1000", f1_element.getAttribute("value"));

        WebElement save_button = driver.findElement(By.xpath("//button[@id='pa-entity-form-save-btn']"));
        save_button.click();
        Thread.sleep(1000);

        // 7. Open the record in view mode
        actionForLastRecord(driver);
        assertLoopValues(driver, 1000, "view");

        // 9. Open the record in Edit mode and start F1 from 0
        actionForLastRecord(driver);
        driver.findElement(By.linkText("edit")).click();
        Thread.sleep(500);
        WebElement f1_edit = driver.findElement(By.xpath("//div[@id='_field_container-f1']/child::span/child::input"));
        f1_edit.clear();
        f1_edit.sendKeys(String.valueOf(number_2));
        waitUntilStop(f1_edit, 999);
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[@id='pa-entity-form-save-btn']")).click();
        Thread.sleep(2000);

        // 13. Open the record in View mode.
        // 14. Verify that all values stay as expected: F1 = 999; F2 = 1000; F3 = 1001.
        actionForLastRecord(driver);
        assertLoopValues(driver, 999, "view");

        // 15. Open the record in Edit mode.
        // 16. Verify that all values stay as expected: F1 = 999; F2 = 1000; F3 = 1001.
        actionForLastRecord(driver);
        assertLoopValues(driver, 999, "edit");

        // Debug - to delete
        Thread.sleep(3000);

        // cleanup, delete created record
        actionForLastRecord(driver);
        driver.findElement(By.linkText("delete")).click();

//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(delBtnXpath))).click();
        // Debug - to delete
        Thread.sleep(5000);
    }
}
