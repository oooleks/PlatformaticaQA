import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectUtils;
import runner.type.Run;
import runner.type.RunType;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Run(run = RunType.Multiple)
public class EntityPlatformFunctionsTest extends BaseTest {

    private String[] first_record_values;

    private void actionsClick(WebDriver driver, int record_index, String mode) {
        ProjectUtils.click(driver, getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath(String.format("//tr[@data-index='%d']/td/div/button", record_index)))));
        ProjectUtils.click(driver, getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.linkText(mode))));
    }

    private String[] getValues(WebDriver driver, String mode) {
        String[] values = new String[3];
        List<WebElement> function_elements = driver.findElements
                (By.xpath("//input[contains(@id,'last')]|//input[@id='constant']"));
        for (int i = 0; i < values.length; i++) {
            values[i] = function_elements.get(i).getAttribute("value");
        }
        if (mode.equals("new") || mode.equals("edit")) {
            driver.findElement(By.xpath("//button[@id='pa-entity-form-save-btn']")).click();
        } else {
            driver.findElement(By.xpath("//button[@id='pa-entity-form-draft-btn']")).click();
        }
        return values;
    }

    private String[] getRecordValues(WebDriver driver, String mode) {
        ProjectUtils.click(driver, driver.findElement(By.xpath("//p[contains(text(), 'Platform functions')]")));
        switch (mode) {
            case "cancel":
                getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//i[text()='create_new_folder']"))).click();
                driver.findElement(By.xpath("//button[contains(text(), 'Cancel')]")).click();
                return null;
            case "new":
                getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//i[text()='create_new_folder']"))).click();
                return getValues(driver, mode);
            case "draft":
                getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//i[text()='create_new_folder']"))).click();
                return getValues(driver, mode);
            case "view":
                String[] record_values = new String[3];
                actionsClick(driver, 1, mode);
                List<WebElement> record_elements = driver.findElements
                        (By.xpath("//label[contains(text(),'Last')]/following-sibling::div//span" +
                                "|//label[text()='Constant']/following-sibling::div//span"));
                for (int i = 0; i < record_values.length; i++) {
                    record_values[i] = record_elements.get(i).getText();
                }
                return record_values;
            case "edit":
                actionsClick(driver, 1, mode);
                return getValues(driver, mode);
            default:
                throw new IllegalArgumentException(String.format("Invalid mode %s", mode));
        }
    }

    private void assertRecordValues(String[] actual_values, String[] expected_values, String mode){
        if (mode.equals("view")) {
            Assert.assertEquals(actual_values[0], String.valueOf(Integer.parseInt(expected_values[0]) + 1));
            Assert.assertEquals(String.format(" %s", actual_values[1]),  String.format("%s suffix", expected_values[1]));
            Assert.assertEquals(actual_values[2], expected_values[2]);
        } else {
            Assert.assertEquals(actual_values[0], String.valueOf(Integer.parseInt(expected_values[0]) + 1));
            Assert.assertEquals(actual_values[1], String.format("%s suffix", expected_values[1]));
            Assert.assertEquals(actual_values[2], expected_values[2]);
        }
    }

    @Test
    public void functionCancelTest() {
        WebDriver driver = getDriver();

        getRecordValues(driver, "cancel");
        WebElement constant_table = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//div[contains(@class, 'card-body')]")));
        Assert.assertTrue(constant_table.getText().isEmpty());
    }

    @Test (dependsOnMethods = "functionCancelTest")
    public void functionDraftTest() {
        WebDriver driver = getDriver();

        first_record_values = getRecordValues(driver, "draft");
        String[] second_record_values = getRecordValues(driver, "draft");
        assertRecordValues(second_record_values, first_record_values, "draft");
        Assert.assertEquals(driver.findElement(By.xpath("//tr[1]//i")).getAttribute("class"), "fa fa-pencil");
        Assert.assertEquals(driver.findElement(By.xpath("//tr[2]//i")).getAttribute("class"), "fa fa-pencil");

        ProjectUtils.click(driver, driver.findElement(By.id("navbarDropdownProfile")));
        ProjectUtils.click(driver, driver.findElement(By.xpath("//a[contains(text(), 'Reset')]")));
    }

    @Test (dependsOnMethods = {"functionCancelTest", "functionDraftTest"})
    public void functionCreateTest() {
        WebDriver driver = getDriver();

        first_record_values = getRecordValues(driver, "new");
        String[] second_record_values = getRecordValues(driver, "new");
        assertRecordValues(second_record_values, first_record_values, "new");
    }

    @Test (dependsOnMethods = {"functionCancelTest", "functionDraftTest", "functionCreateTest"})
    public void functionViewTest() {
        WebDriver driver = getDriver();

        String[] view_record_values = getRecordValues(driver, "view");
        assertRecordValues(view_record_values, first_record_values, "view");
    }

    @Test (dependsOnMethods = {"functionCancelTest", "functionDraftTest", "functionCreateTest", "functionViewTest"})
    public void functionEditTest() {
        WebDriver driver = getDriver();

        String[] edit_record_values = getRecordValues(driver, "edit");
        assertRecordValues(edit_record_values, first_record_values, "edit");
    }

    @Test (dependsOnMethods =
            {"functionCancelTest", "functionDraftTest", "functionCreateTest", "functionViewTest", "functionEditTest"})
    public void functionDeleteTest() {
        WebDriver driver = getDriver();

        ProjectUtils.click(driver, driver.findElement(By.xpath("//p[contains(text(), 'Platform functions')]")));
        for (int i = 1; i < 3; i++) {
            actionsClick(driver, 0, "delete");
        }
        WebElement record_table = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]")));
        Assert.assertTrue(record_table.getText().isEmpty());
    }
}