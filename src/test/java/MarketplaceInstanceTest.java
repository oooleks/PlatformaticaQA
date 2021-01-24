import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectUtils;
import runner.type.Profile;
import runner.type.ProfileType;
import runner.type.Run;
import runner.type.RunType;

import java.util.List;

@Profile(profile = ProfileType.MARKETPLACE)
@Run(run = RunType.Multiple)
public class MarketplaceInstanceTest extends BaseTest {

    private String[] app_values = new String[7];

    private Boolean isUnableCreateApp() {
      return getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
              (By.xpath("//body"))).getText().equals("Unable to create instance");
    }

    private String[] getInstanceValues() {
        String name = RandomStringUtils.randomAlphanumeric(6, 10).toLowerCase();
        return new String[] {name, name, String.format("https://%s.eteam.work", name), "admin", "2", "1", "English"};
    }

    private String[] createInstance(WebDriver driver) throws InterruptedException {
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//i[contains(text(),'create_new_folder')]"))).click();
        String[] instance_values;
        do {
            instance_values = getInstanceValues();
            if (isUnableCreateApp()) {
                driver.navigate().back();
            }
            WebElement app_name = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                    (By.xpath("//input[@id='name']")));
            ProjectUtils.inputKeys(driver, app_name, instance_values[0]);
            getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                    (By.xpath("//button[@id='pa-entity-form-save-btn']"))).click();
        } while (isUnableCreateApp());
        return instance_values;
    }

    private void actionsClick(WebDriver driver, int record_index, String mode) {
        ProjectUtils.click(driver, getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath(String.format("//tr[@data-index='%d']/td/div/button", record_index)))));
        ProjectUtils.click(driver, getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath(String.format("//a[contains(text(),'%s')]", mode)))));
    }

    private void assertInstanceValues (String[] instance_values) {
        List<WebElement> actual_instance_record = getWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy
                (By.xpath("//table[@id='pa-all-entities-table']//a/div")));
        for (int i = 0; i < instance_values.length; i++){
            String actual_value = String.valueOf(actual_instance_record.get(i).getText());
            Assert.assertEquals(actual_value, instance_values[i]);
        }
    }

    private void resetAccount(WebDriver driver) {
        ProjectUtils.click(driver, driver.findElement(By.id("navbarDropdownProfile")));
        ProjectUtils.click(driver, driver.findElement(By.xpath("//a[contains(text(), 'Reset')]")));

        WebElement constant_table = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]")));
        Assert.assertTrue(constant_table.getText().isEmpty());
    }

    @Test
    public void instanceCancelTest() throws InterruptedException {
        WebDriver driver = getDriver();

        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//i[contains(text(),'create_new_folder')]"))).click();
        WebElement app_name = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//input[@id='name']")));
        ProjectUtils.inputKeys(driver, app_name, getInstanceValues()[0]);
        driver.findElement(By.xpath("//button[contains(text(),'Cancel')]")).click();

        WebElement constant_table = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]")));
        Assert.assertTrue(constant_table.getText().isEmpty());
    }

    @Test (dependsOnMethods = "instanceCancelTest")
    public void instanceDraftTest() throws InterruptedException {
        WebDriver driver = getDriver();

        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//i[contains(text(),'create_new_folder')]"))).click();
        WebElement app_name = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//input[@id='name']")));
        app_values = getInstanceValues();
        ProjectUtils.inputKeys(driver, app_name, app_values[0]);
        driver.findElement(By.xpath("//button[@id='pa-entity-form-draft-btn']")).click();

        assertInstanceValues(app_values);
        Assert.assertEquals(driver.findElement(By.xpath("//tr/td/i")).getAttribute("class"), "fa fa-pencil");
        resetAccount(driver);
    }

    @Test
    public void instanceUniquenessTest() throws InterruptedException {
        WebDriver driver = getDriver();

        app_values = createInstance(driver);

        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//i[contains(text(),'create_new_folder')]"))).click();
        WebElement subdomain = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//input[@id='subdomain']")));
        ProjectUtils.inputKeys(driver, subdomain, app_values[0]);
        String field_note = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//label[@id='_field-note-subdomain']"))).getText();

        Assert.assertEquals(field_note, "sorry, this subdomain is already taken, please try another name");
        resetAccount(driver);
    }

    @Test
    public void instancePasswordTest() throws InterruptedException {
        WebDriver driver = getDriver();

        createInstance(driver);
        String congrats = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[@class='card-body ']//h3[1]"))).getText();
        Assert.assertEquals(congrats, "Congratulations! Your instance was successfully created");

        Assert.assertFalse(getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]//h4[2]/b"))).getText().isEmpty());
        driver.navigate().refresh();
        Assert.assertEquals(getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]//h4[2]/b"))).getText(), "[[notfound]]");
        resetAccount(driver);
    }

    @Test (dependsOnMethods = {"instanceUniquenessTest", "instancePasswordTest"})
    public void instanceCreateTest() throws InterruptedException {
        WebDriver driver = getDriver();

        app_values = createInstance(driver);
        assertInstanceValues(app_values);
        String congrats = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[@class='card-body ']//h3[1]"))).getText();
        Assert.assertEquals(congrats, "Congratulations! Your instance was successfully created");
    }

    @Test (dependsOnMethods = "instanceCreateTest")
    public void instanceViewTest() {
        WebDriver driver = getDriver();

        actionsClick(driver, 0, "view");
        List<WebElement> instance_elements = driver.findElements
                (By.xpath("//div[@class='card-body']//span"));
        for (int i = 0; i < 4; i++) {
            Assert.assertEquals(instance_elements.get(i).getText(), app_values[i]);
        }
    }

    @Test (dependsOnMethods = "instanceViewTest")
    public void instanceDeleteTest() {
        WebDriver driver = getDriver();

        actionsClick(driver, 0, "delete");
        WebElement record_table = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]")));
        Assert.assertTrue(record_table.getText().isEmpty());
    }
}
