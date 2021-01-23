package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ErrorPage extends BasePage {

    public final String ERROR_MESSAGE = "error saving entity";

    @FindBy(tagName = "body")
    private WebElement errorMessage;

    public ErrorPage(WebDriver driver) {
        super(driver);
    }

    public String getErrorMessage() {
        return errorMessage.getText().toLowerCase();
    }

}