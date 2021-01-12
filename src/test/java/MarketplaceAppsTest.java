import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectUtils;
import runner.type.Profile;
import runner.type.ProfileType;
import runner.type.Run;
import runner.type.RunType;

import java.util.*;

@Profile(profile = ProfileType.MARKETPLACE)
@Run(run = RunType.Multiple)
public class MarketplaceAppsTest extends BaseTest {

    private Boolean isUnableCreateApp() {
      return getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
              (By.xpath("//body"))).getText().equals("Unable to create instance");
    }

    private final String[] getEntityValues() {
        String name = RandomStringUtils.randomAlphanumeric(6, 10).toLowerCase();
        return new String[] {name, name, String.format("https://%s.eteam.work", name), "admin", "2", "1", "English"};
    }

    private void assertInstanceValues (String[] entity_values) {
        List<WebElement> actual_entity_record = getWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy
                (By.xpath("//table[@id='pa-all-entities-table']//a/div")));
        for (int i = 0; i < entity_values.length; i++){
            String actual_value = String.valueOf(actual_entity_record.get(i).getText());
            Assert.assertEquals(actual_value, entity_values[i]);
        }
    }

    @Test
    public void newInstanceTest(ITestContext context) throws InterruptedException {
        WebDriver driver = getDriver();

        WebElement instance_table = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]")));
        Assert.assertTrue(instance_table.getText().isEmpty());

        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//i[contains(text(),'create_new_folder')]"))).click();
        String[] entity_values;
        do {
            entity_values = getEntityValues();
            if (isUnableCreateApp()) {
                driver.navigate().back();
            }
            WebElement app_name = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                    (By.xpath("//input[@id='name']")));
            app_name.clear();
            ProjectUtils.inputKeys(driver, app_name, entity_values[0]);
            getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                    (By.xpath("//button[@id='pa-entity-form-save-btn']"))).click();
        }
        while (isUnableCreateApp());

        String congrats = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]//h3[1]"))).getText();
        Assert.assertEquals(congrats, "Congratulations! Your instance was successfully created");

        String username = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]//h4[1]/b"))).getText();
        Assert.assertEquals(username, "admin");

        String admin_password = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]//h4[2]/b"))).getText();
        //debug
        System.out.println("Password: " + admin_password);
        System.out.println(String.format("https://%s.eteam.work", entity_values[0]));

        context.setAttribute("password", admin_password);
        context.setAttribute("app_name", entity_values[0]);
//        String admin_password = (String) context.getAttribute("password");
//        String app_name = (String) context.getAttribute("app_name");
        assertInstanceValues(entity_values);

        driver.get(String.format("https://%s.eteam.work", entity_values[0]));

//        WebElement login_element = driver.findElement(By.xpath("//input[@name='login_name']"));
////        WebElement login_element = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='login_name']")));
////        ProjectUtils.inputKeys(driver, login_element, "admin");
//        login_element.sendKeys("admin");
//        WebElement pasw_element = driver.findElement(By.xpath("//input[@name='password']"));
//        pasw_element.sendKeys(admin_password);
////        ProjectUtils.inputKeys(driver, pasw_element, admin_password);
//        driver.findElement(By.xpath("//button[contains(text(),'Sign in')]")).click();
    }

//    @Test (dependsOnMethods = "adminNewEntityTest")
//    public void adminAppLoginTest(ITestContext context) throws InterruptedException {
//        WebDriver driver = getDriver();
//        driver.get(String.format("https://%s.eteam.work", NAME));
//
//        WebElement loginElement = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='login_name']")));
//        ProjectUtils.inputKeys(driver, loginElement, "admin");
////        loginElement.sendKeys("admin");
//        WebElement pasElement = driver.findElement(By.xpath("//input[@name='password']"));
//        ProjectUtils.inputKeys(driver, pasElement, String.valueOf(context.getAttribute("myOwnAttribute")));
////        pasElement.sendKeys(String.valueOf(context.getAttribute("myOwnAttribute")));
//        driver.findElement(By.xpath("//button[text()='Sign in']")).click();
//    }

}
