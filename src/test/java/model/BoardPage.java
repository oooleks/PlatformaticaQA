package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class BoardPage extends BaseTablePage<BoardPage, BoardEditPage> {

    @FindBy(xpath = "//div[@class = 'kanban-item']/div[2]")
    private WebElement boardRow;

    @FindBy(xpath  = "//div[@data-id='Pending']//div[@class='kanban-item']")
    private List<WebElement> pendingCardItems;

    public BoardPage(WebDriver driver) {
        super(driver);
    }

    @Override
    protected BoardEditPage createEditPage () {
        return new BoardEditPage(getDriver());
    }

    public String getPendingText(){
        return boardRow.getText();
    }

    public int pendingCardItemsCount(){
        return pendingCardItems.size();
    }
}



