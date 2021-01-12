import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EntityPlatformFunctionsTest extends BaseTest {

    private static final By ACTIONS_BUTTON = By.xpath("//tr[@data-index='0']/td/div/button");

    private String[] getNewEditValues(WebDriver driver) {
        String[] values = new String[3];
        List<WebElement> function_elements = driver.findElements
                (By.xpath("//input[contains(@id,'last')]|//input[@id='constant']"));
        for (int i = 0; i < values.length; i++) {
            values[i] = function_elements.get(i).getAttribute("value");
        }
        driver.findElement(By.xpath("//button[@id='pa-entity-form-save-btn']")).click();
        return values;
    }

    private String[] getRecordValues(WebDriver driver, String mode) {
        WebElement platform_functions = driver.findElement(By.xpath("//p[contains(text(),'Platform functions')]"));
        ProjectUtils.click(driver, platform_functions);
        if (mode.equals("new")){
            getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                    (By.xpath("//i[text()='create_new_folder']"))).click();
            return getNewEditValues(driver);
        } else if (mode.equals("view")) {
            String[] record_values = new String[3];
            getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                    (By.xpath("//tr[@data-index='1']/td/div/button"))).click();
            getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(By.linkText(mode))).click();
            List<WebElement> record_elements = driver.findElements
                    (By.xpath("//label[contains(text(),'Last')]/following-sibling::div//span|//label[text()='Constant']/following-sibling::div//span"));
            for (int i = 0; i < record_values.length; i++) {
                record_values[i] = record_elements.get(i).getText();
            }
            return record_values;
        } else {
            getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                    (By.xpath("//tr[@data-index='1']/td/div/button"))).click();
            getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(By.linkText(mode))).click();
            return getNewEditValues(driver);
        }
    }

    private void assertRecordValues(String[] actual_values, String[] expected_values, String mode){
        if (mode.equals("view")) {
            Assert.assertEquals(actual_values[0], String.valueOf(Integer.parseInt(expected_values[0]) + 1));
            Assert.assertEquals(" " + actual_values[1],  expected_values[1] + " suffix");
            Assert.assertEquals(actual_values[2], expected_values[2]);
        } else {
            Assert.assertEquals(actual_values[0], String.valueOf(Integer.parseInt(expected_values[0]) + 1));
            Assert.assertEquals(actual_values[1],  expected_values[1] + " suffix");
            Assert.assertEquals(actual_values[2], expected_values[2]);
        }
    }

    @Test
    public void platformFunctionsCreateTest() {
        WebDriver driver = getDriver();

        String[] first_record_values = getRecordValues(driver,"new");
        String[] second_record_values = getRecordValues(driver, "new");
        assertRecordValues(second_record_values, first_record_values, "new");

        String[] view_record_values = getRecordValues(driver,"view");
        assertRecordValues(view_record_values, first_record_values,"view");

        String[] edit_record_values = getRecordValues(driver,"edit");
        assertRecordValues(edit_record_values,first_record_values,"new");

        for (int i = 1; i < 3; i++) {
            getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(ACTIONS_BUTTON)).click();
            driver.findElement(By.linkText("delete")).click();
        }

        WebElement record_table = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]")));
        Assert.assertNotNull(record_table);
        Assert.assertTrue(record_table.getText().isEmpty());
    }
}