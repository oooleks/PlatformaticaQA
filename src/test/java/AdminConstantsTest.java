import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.type.Profile;
import runner.type.ProfileType;

import java.util.concurrent.TimeUnit;

@Profile(profile = ProfileType.ADMIN)
public class AdminConstantsTest extends BaseTest {

    private WebDriverWait getWait(int timeoutSecond) {
        return new WebDriverWait(getDriver(), timeoutSecond);
    }

    private WebElement getCompany(String value) {
        By company_name_loc = By.xpath("//td[text()='Company " + value + "']/following-sibling::td/child::input[@type='text']");
        return getWait(2).until(ExpectedConditions.visibilityOfElementLocated(company_name_loc));
    }

    private void goToConstantsList() {
        getWait(2).until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//a[@id='navbarDropdownProfile']"))).click();
        getWait(2).until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//a[text()='Configuration']"))).click();
        getWait(2).until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//i[text()='miscellaneous_services']/parent::a"))).click();
        getWait(2).until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//span[text()='List constants']"))).click();
        getWait(2).until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]")));
    }

    private void commandInCMD(WebDriver driver, String command) {
        WebElement cmd = getWait(1).until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//textarea[@id='pa-cli-cmd']")));
        cmd.click();
        cmd.sendKeys(command);
        getWait(1).until(ExpectedConditions.elementToBeClickable
                (By.xpath("//button[@id='pa-cli-cmd-enter']"))).click();
        getWait(2).until(ExpectedConditions.stalenessOf(driver.findElement
                (By.xpath("//textarea[@id='pa-cli-cmd']"))));
    }

    @Test
    public void adminConstants() {
        WebDriver driver = getDriver();
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

        goToConstantsList();
        if (driver.findElements(By.xpath("//td[contains(text(),'Company')]")).size() != 0) {
            commandInCMD(driver,"delete constant \"Company Name\"");
            commandInCMD(driver,"delete constant \"Company Email\"");
        }

        commandInCMD(driver,"create constant \"Company Name\" = \"Platformatica\"");
        commandInCMD(driver,"create constant \"Company Email\" = \"contact@company.com\"");

        Assert.assertEquals(getCompany("Name").getAttribute("value"),"Platformatica");
        Assert.assertEquals(getCompany("Email").getAttribute("value"),"contact@company.com");

        getCompany("Name").clear();
        getCompany("Name").sendKeys("Platformatica 2");
        getWait(1).until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//button[@type='submit']"))).click();
        goToConstantsList();
        Assert.assertEquals(getCompany("Name").getAttribute("value"),"Platformatica 2");

        commandInCMD(driver,"delete constant \"Company Name\"");
        commandInCMD(driver,"delete constant \"Company Email\"");
        Assert.assertEquals((driver.findElements(By.xpath("//td[contains(text(),'Company')]")).size() == 0), true);
    }
}