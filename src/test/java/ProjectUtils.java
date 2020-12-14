import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public abstract class ProjectUtils {

    public static void login(WebDriver driver, String login, String password) {
        WebElement loginElement = driver.findElement(By.xpath("//input[@name='login_name']"));
        loginElement.sendKeys(login);

        WebElement passwordElement = driver.findElement(By.xpath("//input[@name='password']"));
        passwordElement.sendKeys(password);

        WebElement button = driver.findElement(By.xpath("//button[text()='Sign in']"));
        button.click();
    }

    /*
     *  The method helps to avoid - Element is not clickable at point (x,x). Other element would receive the click
     */
    public static void click(WebDriver driver, WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click()", element);
    }

    public static void mouse_over(WebDriver driver, WebElement element) {
        Actions builder = new Actions(driver);
        builder.moveToElement(element).build().perform();
    }

}
