package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.stream.Collectors;

public final class ChevronPage extends BaseTablePage<ChevronPage, ChevronEditPage> {

    public ChevronPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected ChevronEditPage createEditPage() {
        return new ChevronEditPage(getDriver());
    }

    @Override
    public List<String> getRow(int rowNumber) {
        return getRows().get(rowNumber).findElements(By.tagName("td")).stream()
                .map(WebElement::getText).collect(Collectors.toList()).subList(1, 7);

    }

    public ChevronPage clickViewButton(String xpath) {
        getDriver().findElement(By.xpath("//button[@data-toggle=\"dropdown\"]")).click();
        getDriver().findElement(By.xpath(xpath)).click();
        return new ChevronPage(getDriver());
    }

    public ChevronPage getColumn() {
        List<WebElement> list = getDriver().findElements(By.xpath("//span[@class='pa-view-field']"));
        Assert.assertEquals(list.size(), 6 );
        return this;
     }

}