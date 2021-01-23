package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public final class FieldsPage extends BaseTablePage<FieldsPage, FieldsEditPage> {

    public FieldsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected FieldsEditPage createEditPage() {
        return new FieldsEditPage(getDriver());
    }

    @Override
    public List<String> getRow(int rowNumber) {
        return getRows().get(rowNumber).findElements(By.tagName("td")).stream()
                .map(WebElement::getText).collect(Collectors.toList()).subList(1, 10);
    }

    public String getTitle(int rowNumber) {
        return getRow(rowNumber).get(0);
    }

    public String getDecimal(int rowNumber) {
        return getRow(rowNumber).get(3);
    }

}
