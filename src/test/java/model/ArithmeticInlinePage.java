package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class ArithmeticInlinePage extends BaseTablePage<ArithmeticInlinePage, ArithmeticInlineEditPage>{

    @FindBy(css = "input#sum")
    private WebElement sum;

    @FindBy(css = "input#sub")
    private WebElement sub;

    @FindBy(css = "input#mul")
    private WebElement mul;

    @FindBy(css = "input#div")
    private WebElement div;

    public ArithmeticInlinePage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected ArithmeticInlineEditPage createEditPage() {
        return new ArithmeticInlineEditPage(getDriver());
    }

    @Override
    public List<String> getRow(int rowNumber) {
        return getRows().get(rowNumber).findElements(By.tagName("td")).stream()
                .map(WebElement::getText).collect(Collectors.toList()).subList(1, 7);
    }
}
