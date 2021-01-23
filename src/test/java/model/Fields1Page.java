package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import runner.ProjectUtils;

import java.util.List;

public class Fields1Page extends BasePage {

    @FindBy(xpath = "//i[text() = 'create_new_folder']")
    private WebElement newFolderBtn;

    @FindBy(xpath = "//tbody/tr")
    private List<WebElement> rowList;

    @FindBy(xpath = "//tbody/tr/td")
    private List<WebElement> colList;

    @FindBy(xpath = "//div[@class = 'dropdown pull-left']")
    private WebElement drop_down;

    @FindBy(xpath = "//a[contains(@href, 'edit')]")
    private WebElement edit_Btn;

    @FindBy(xpath = "//a[contains(@href, 'delete')]")
    private WebElement delete_Btn;

    @FindBy(xpath = "//table[@id = 'pa-all-entities-table']/tbody")
    private WebElement table;

    public Fields1Page(WebDriver driver) {
        super(driver);
    }

    public FieldsEdit1Page clickCreateNewFolder() {
        newFolderBtn.click();

        return new FieldsEdit1Page(getDriver());
    }

    public FieldsEdit1Page clickEditRecord(){
        drop_down.click();
        ProjectUtils.click(getDriver(), getWait().until(ExpectedConditions.elementToBeClickable(edit_Btn)));

        return new FieldsEdit1Page(getDriver());
    }

    public Fields1Page clickDeleteRecord(){
        drop_down.click();
        ProjectUtils.click(getDriver(), getWait().until(ExpectedConditions.elementToBeClickable(delete_Btn)));

        return new Fields1Page(getDriver());
    }

    public int getRowCount(){
        return rowList.size();
    }

    public String getTitleText(){
        return table.findElement(By.xpath("//tr/td[2]/a/div")).getText();
    }

    public String getCommentsText(){
        return table.findElement(By.xpath("//tr/td[3]/a/div")).getText();
    }

    public String getInt_Text(){
        return table.findElement(By.xpath("//tr/td[4]/a/div")).getText();
    }

}
