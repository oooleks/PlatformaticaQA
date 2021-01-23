package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import runner.ProjectUtils;

public abstract class BaseEditPage<TablePage> extends BasePage {

    @FindBy(css = "button[id*='save']")
    protected WebElement saveButton;

    @FindBy(css = "button[id*='draft']")
    protected WebElement saveDraftButton;

    @FindBy(xpath = "//button[text() = 'Cancel']")
    protected WebElement cancelButton;

    public BaseEditPage(WebDriver driver) {
        super(driver);
    }

    protected abstract TablePage createPage();

    public TablePage clickSaveButton() {
        ProjectUtils.scroll(getDriver(), saveButton);
        ProjectUtils.click(getWait(), saveButton);
        return createPage();
    }

    public TablePage clickSaveDraftButton() {
        ProjectUtils.scroll(getDriver(), saveButton);
        ProjectUtils.click(getWait(), saveDraftButton);
        return createPage();
    }

    public ErrorPage clickSaveButtonErrorExpected() {
        ProjectUtils.scroll(getDriver(), saveButton);
        ProjectUtils.click(getWait(), saveButton);
        return new ErrorPage(getDriver());
    }

    public ErrorPage clickSaveDraftButtonErrorExpected() {
        ProjectUtils.scroll(getDriver(), saveButton);
        ProjectUtils.click(getWait(), saveDraftButton);
        return new ErrorPage(getDriver());
    }

    public TablePage clickCancelButton() {
        ProjectUtils.scroll(getDriver(), cancelButton);
        ProjectUtils.click(getWait(), cancelButton);
        return createPage();
    }
}