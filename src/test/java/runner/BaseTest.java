package runner;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseTest {

    // Selenium Grid
    public static final String HUB_URL = "http://localhost:4444/wd/hub";

    // check if remote driver is running on 4444 port
    private static boolean remoteWebDriver = false;
    static {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(HUB_URL + "/status").openConnection();
            try {
                con.setRequestMethod("GET");
                remoteWebDriver = con.getResponseCode() == HttpURLConnection.HTTP_OK;
                // finally section will always execute -> disconnect connection
            } finally {
                con.disconnect();
            }
        } catch (IOException ignore) {
            // code if error occurs
        }

        if (!remoteWebDriver) {
            WebDriverManager.chromedriver().setup();
        }
    }

    private WebDriver driver;

    @BeforeMethod
    protected void setUpAll() {

        if (remoteWebDriver) {
            try {
                this.driver = new RemoteWebDriver(new URL(HUB_URL), DesiredCapabilities.chrome());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.driver = new ChromeDriver();
        }

        this.driver.manage().window().maximize();
    }

//    @AfterMethod
    protected void setDownAll() {
        driver.quit();
    }

    protected WebDriver getDriver() {
        return driver;
    }
}
