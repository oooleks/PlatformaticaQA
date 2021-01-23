
package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public final class PlaceholderPage extends BasePage{

    @FindBy(xpath = "//i[contains(text(),'create_new_folder')]")
    private WebElement newFolderButton;

    public PlaceholderPage(WebDriver driver) {
        super(driver);
    }

    public PlaceholderEditPage createNewRecord(){
        newFolderButton.click();

        return new PlaceholderEditPage(getDriver());
    }
}