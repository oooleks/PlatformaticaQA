import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class ProjectUtils {

    public static void login(WebDriver driver, String login, String password) {
        WebElement loginElement = driver.findElement(By.xpath("//input[@name='login_name']"));
        loginElement.sendKeys(login);

        WebElement passwordElement = driver.findElement(By.xpath("//input[@name='password']"));
        passwordElement.sendKeys(password);

        WebElement button = driver.findElement(By.xpath("//button[text()='Sign in']"));
        button.click();
    }

}
