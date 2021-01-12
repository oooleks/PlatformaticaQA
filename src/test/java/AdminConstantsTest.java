import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.type.Profile;
import runner.type.ProfileType;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Profile(profile = ProfileType.MARKETPLACE)
public class AdminConstantsTest extends BaseTest {

    private static final By COMPANY_NAME = By.xpath("//td[contains(text(),'Company Name')]");
    private static final By COMPANY_EMAIL = By.xpath("//td[contains(text(),'Company Email')]");

    private String getRandomText() {
        return UUID.randomUUID().toString();
    }

    private WebElement getCompany(String value) {
        By company_name_loc = By.xpath("//td[contains(text(),'Company " + value + "')]/following-sibling::td/child::input[@type='text']");
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
    public void adminConstants() {
        WebDriver driver = getDriver();
//        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        goToConstantsList();
        if (driver.findElements(COMPANY_NAME).size() != 0) {
            commandInCMD(driver, "delete constant \"Company Name\"");
        }
        if (driver.findElements(COMPANY_EMAIL).size() != 0) {
            commandInCMD(driver, "delete constant \"Company Email\"");
        }
        String company_name_1 = getRandomText();
        commandInCMD(driver, "create constant \"Company Name\" = \"" + company_name_1 + "\"");
        commandInCMD(driver, "create constant \"Company Email\" = \"contact@company.com\"");

        Assert.assertEquals(getCompany("Name").getAttribute("value"), company_name_1);
        Assert.assertEquals(getCompany("Email").getAttribute("value"), "contact@company.com");

        String company_name_2 = getRandomText();
        getCompany("Name").clear();
        getCompany("Name").sendKeys(company_name_2);
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//button[@type='submit']"))).click();
        goToConstantsList();
        Assert.assertEquals(String.valueOf(getCompany("Name").getAttribute("value")), company_name_2);

        commandInCMD(driver, "delete constant \"Company Name\"");
        commandInCMD(driver, "delete constant \"Company Email\"");
        Assert.assertTrue(driver.findElements(COMPANY_NAME).size() == 0);
        Assert.assertTrue(driver.findElements(COMPANY_EMAIL).size() == 0);
//ToDo
//        WebElement constants_table = getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated
//                (By.xpath("//div[contains(@class,'card-body')]")));
//        Assert.assertTrue(constants_table.getText().isEmpty());
    }
}