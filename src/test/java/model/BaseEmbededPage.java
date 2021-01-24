package model;

import com.beust.jcommander.Strings;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseEmbededPage<TablePage> extends MainPage {

    private static final String TBODY_XPATH = "//table[@class='pa-entity-table']/tbody";
    private static final String DATA_ROW = "data-row";

    private static final By BY_XPATH_TDS = By.xpath("//td");
    private static final By BY_XPATH_DELETE_X = By.xpath("//td[@class='pa-row-delete-btn-col']/div/i");

    @FindBy (xpath = TBODY_XPATH)
    private WebElement body;

    @FindBy(xpath = TBODY_XPATH + "/tr[starts-with(@id,'add-row-')]/td[@class='pa-add-row-btn-col']/button")
    private WebElement buttonNewEmbeded;

    @FindBy(xpath = TBODY_XPATH + "/tr[starts-with(@id,'row-') and @data-row > '0']")
    private List<WebElement> trs;

    protected List<WebElement> getRows() {
        return trs;
    }

    public BaseEmbededPage(WebDriver driver) {
        super(driver);
    }

    public TablePage clickNewEmbededRow() {
        buttonNewEmbeded.click();
        return (TablePage) this;
    }

    public int getRowCount() {
        if (Strings.isStringEmpty(body.getText())) {
            return 0;
        } else {
            return getRows().size();
        }
    }

    public List<String> getRow(int rowNumber) {
        List<String> result = new ArrayList<>();
        List<WebElement> cells = getRows().get(rowNumber).findElements(BY_XPATH_TDS);

        result.add(cells.get(1).findElement(By.tagName("input")).getAttribute(DATA_ROW));
        for (int i = 2; i < cells.size()-2; i++ ) {
            result.add(cells.get(i).getText());
        }

        return result;
    }

    public TablePage deleteRow(int rowNumber) {
        trs.get(rowNumber).findElement(BY_XPATH_DELETE_X).click();
        return (TablePage) this;
    }

    public TablePage deleteRow() {
        return deleteRow(getRows().size() - 1);
    }

    public String getLineNumber(int rowNumber){
        return getRow(rowNumber).get(0);
    }
}
