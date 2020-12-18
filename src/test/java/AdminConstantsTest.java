import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;

public class AdminConstantsTest extends BaseTest {

    private WebDriverWait getWait(int timeoutSecond) {
        return new WebDriverWait(getDriver(), timeoutSecond);
    }

    private void constantsList() {
        getWait(1).until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//a[@id='navbarDropdownProfile']"))).click();
        getWait(1).until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//a[text()='Configuration']"))).click();

        getWait(1).until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//i[text()='miscellaneous_services']/parent::a"))).click();
        getWait(1).until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//span[text()='List constants']"))).click();
    }

    @Test
    public void adminConstants() {
        WebDriver driver = getDriver();
        driver.get("https://999856.eteam.work/");
//user134@tester.com;JtpjhhB34u
        String login = "admin";
        String password = "86ZIvAeptg";

        WebElement loginElement = driver.findElement(By.xpath("//input[@name='login_name']"));
//                getWait(3).until(ExpectedConditions.visibilityOfElementLocated
//                (By.xpath("//input[@name='login_name']")));
        loginElement.sendKeys(login);
        WebElement pasElement = driver.findElement(By.xpath("//input[@name='password']"));
        pasElement.sendKeys(password);
        WebElement button = driver.findElement(By.xpath("//button[text()='Sign in']"));
        button.click();

        constantsList();

        WebElement cmd = getWait(1).until(ExpectedConditions.elementToBeClickable
                (By.xpath("//textarea[@id='pa-cli-cmd']")));

        cmd.click();
        cmd.sendKeys("create constant \"Company Name\" = \"Platformatica\"");
        driver.findElement(By.xpath("//button[@id='pa-cli-cmd-enter']")).click();
        getWait(3).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));

        //        driver.findElement(By.xpath("//td[text()='Company Name']")).click();

        By company_name_loc = By.xpath("//td[text()='Company Name']/following-sibling::td/child::input[type()='text']");
        WebElement company_name = getWait(3).until(ExpectedConditions.visibilityOfElementLocated(company_name_loc));
//        company_name.click();
        Assert.assertEquals("Platformatica", company_name.getAttribute("value"));

        cmd.click();
        cmd.sendKeys("create constant \"Company Email\" = \"contact@company.com\"");
        driver.findElement(By.xpath("//button[@id='pa-cli-cmd-enter']")).click();
        getWait(3).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));

//        By company_email_loc = By.xpath("//td[text()='Company Email']/following-sibling::td/child::input[type()='text']");
//        WebElement company_email = driver.findElement(company_email_loc);
//        Assert.assertEquals("contact@company.com", company_email.getAttribute("value"));
//
//        company_name.clear();
//        company_name.sendKeys("Platformatica 2");
//        driver.findElement(By.xpath("//button[@type='submit']")).click();
//
//        constantsList();
//        Assert.assertEquals("Platformatica 2", company_name.getAttribute("value"));
//
//        cmd.click();
//        cmd.sendKeys("delete constant \"Company Name\"");
//        driver.findElement(By.xpath("//button[@id='pa-cli-cmd-enter']")).click();
//        getWait(3).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));
//
//        cmd.click();
//        cmd.sendKeys("delete constant \"Company Email\"");
//        driver.findElement(By.xpath("//button[@id='pa-cli-cmd-enter']")).click();
//        getWait(3).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));
//
//        Boolean name_notPresent = ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(company_name_loc)).apply(getDriver());
//        Assert.assertTrue(name_notPresent);
//
//        Boolean email_notPresent = ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(company_email_loc)).apply(getDriver());
//        Assert.assertTrue(email_notPresent);
    }
}