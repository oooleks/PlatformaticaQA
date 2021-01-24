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

@Profile(profile = ProfileType.MARKETPLACE)
@Run(run = RunType.Multiple)
public class AdminConstantsTest extends BaseTest {

    private WebDriver driver;
    private String app_name;

    private Boolean isUnableCreateApp() {
        return getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//body"))).getText().equals("Unable to create instance");
    }

    private String[] getEntityValues() {
        String name = RandomStringUtils.randomAlphanumeric(6, 10).toLowerCase();
        return new String[] {name, name, String.format("https://%s.eteam.work", name), "admin", "2", "1", "English"};
    }

    private WebElement getCompany(String value) {
        By company_name_loc = By.xpath
                (String.format("//td[contains(text(),'Company %s')]/following-sibling::td/child::input[@type='text']", value));
        return getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(company_name_loc));
    }

    private void goToConstantsList() {
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//a[@id='navbarDropdownProfile']"))).click();
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//a[contains(text(),'Configuration')]"))).click();
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//i[contains(text(),'miscellaneous_services')]/parent::a"))).click();
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable
                (By.xpath("//span[contains(text(),'List constants')]"))).click();
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]")));
    }

    private void commandInCMD(WebDriver driver, String command) {
        WebElement cmd = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//textarea[@id='pa-cli-cmd']")));
        cmd.click();
        cmd.sendKeys(command);
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable
                (By.xpath("//button[@id='pa-cli-cmd-enter']"))).click();
        getWebDriverWait().until(ExpectedConditions.stalenessOf(driver.findElement
                (By.xpath("//textarea[@id='pa-cli-cmd']"))));
    }

    @Test
    public void createApplicationTest() throws InterruptedException {
        driver = getDriver();

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
            ProjectUtils.inputKeys(driver, app_name, entity_values[0]);
            getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                    (By.xpath("//button[@id='pa-entity-form-save-btn']"))).click();
        } while (isUnableCreateApp());

        String congrats = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]//h3[1]"))).getText();
        Assert.assertEquals(congrats, "Congratulations! Your instance was successfully created");

        app_name = entity_values[0];

        final String admin_password = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]//h4[2]/b"))).getText();

        driver.get(String.format("https://%s.eteam.work", entity_values[0]));
        WebElement login_element = driver.findElement(By.xpath("//input[@name='login_name']"));
        login_element.sendKeys("admin");
        WebElement pasw_element = driver.findElement(By.xpath("//input[@name='password']"));
        pasw_element.sendKeys(admin_password);
        driver.findElement(By.xpath("//button[contains(text(),'Sign in')]")).click();
    }

    @Test (dependsOnMethods = "createApplicationTest")
    public void createConstants() {
        driver.get(String.format("https://%s.eteam.work", app_name));

        goToConstantsList();
        String company_name_1 = ProjectUtils.createUUID();
        commandInCMD(driver, String.format("create constant \"Company Name\" = \"%s\"", company_name_1));
        commandInCMD(driver, "create constant \"Company Email\" = \"contact@company.com\"");
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//button[@type='submit']"))).click();
        goToConstantsList();

        Assert.assertEquals(getCompany("Name").getAttribute("value"), company_name_1);
        Assert.assertEquals(getCompany("Email").getAttribute("value"), "contact@company.com");
    }

    @Test (dependsOnMethods = "createConstants")
    public void editConstant() {
        driver.get(String.format("https://%s.eteam.work", app_name));

        goToConstantsList();
        String company_name_2 = ProjectUtils.createUUID();
        getCompany("Name").clear();
        getCompany("Name").sendKeys(company_name_2);
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//button[@type='submit']"))).click();
        goToConstantsList();
        Assert.assertEquals(String.valueOf(getCompany("Name").getAttribute("value")), company_name_2);
    }

    @Test (dependsOnMethods = "editConstant")
    public void deleteConstants() {
        driver.get(String.format("https://%s.eteam.work", app_name));

        goToConstantsList();
        commandInCMD(driver, "delete constant \"Company Name\"");
        commandInCMD(driver, "delete constant \"Company Email\"");

        WebElement constant_table = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]")));
        Assert.assertTrue(constant_table.getText().isEmpty());
    }
}