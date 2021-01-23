package model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class PlatformFuncPage extends BaseTablePage<PlatformFuncPage, PlatformFuncEditPage> {

    public PlatformFuncPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected PlatformFuncEditPage createEditPage() {
        return new PlatformFuncEditPage(getDriver());
    }

    @Override
    public List<String> getRow(int rowNumber) {
        return getRows().get(rowNumber).findElements(By.tagName("td")).stream()
                .map(WebElement::getText).collect(Collectors.toList());
    }
}
