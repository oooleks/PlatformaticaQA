import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import runner.BaseTest;

public class EntityFieldsTest extends BaseTest {

    @Test
    public void newRecord() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.get("https://ref.eteam.work/");

        ProjectUtils.login(driver, "user1@tester.com", "ah1QNmgkEO");
        Thread.sleep(3000);

    }
}
