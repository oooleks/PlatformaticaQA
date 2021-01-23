package model;

import com.beust.jcommander.Strings;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public final class RecycleBinPage extends MainPage {

    @FindBy(className = "card-body")
    private WebElement body;

    @FindBy(css = "table tbody > tr")
    private List<WebElement> rows;

    public RecycleBinPage(WebDriver driver) {
        super(driver);
    }

    public int getRowCount() {
        if (Strings.isStringEmpty(body.getText())) {
            return 0;
        } else {
            return rows.size();
        }
    }

    public String getDeletedEntityContent(int rowNumber) {
        return rows.get(rowNumber).findElement(By.tagName("td")).getText();
    }

    public String getDeletedEntityContent() {
        return rows.get(0).findElement(By.tagName("td")).getText();
    }

    public String getDeletedImportValue() {
        return rows.get(0).findElement(By.xpath("//td/a/span/b")).getText();
    }

    public String getFirstCellValue( int rowNumber) {
        return rows.get(rowNumber).findElement(By.xpath("//td[1]/a/span[1]/b")).getText();
    }

    public void clickDeletePermanently(int rowNumber){
        rows.get(rowNumber).findElement(By.xpath("//a[contains (text(), 'delete permanently')]")).click();
    }
}
