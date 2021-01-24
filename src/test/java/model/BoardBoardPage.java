package model;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class BoardBoardPage extends BasePage {

    @FindBy(xpath = "//div[@class = 'kanban-item']/div[2]")
    private WebElement boardRow;

    @FindBy(xpath  = "//div[@data-id='Pending']//div[@class='kanban-item']")
    private List<WebElement> pendingCardItems;

    @FindBy(xpath  = "//div[1]/main[@class='kanban-drag']")
    private WebElement pendingKanbanItem;

    @FindBy(xpath  = "//div[2]/main[@class='kanban-drag']")
    private WebElement onTrackKanbanItem;

    @FindBy(xpath  = "//div[3]/main[@class='kanban-drag']")
    private WebElement doneKanbanItem;

    @FindBy(xpath = "//a[contains(@href, '31')]/i[text()='dashboard']")
    private  WebElement boardButton;

    @FindBy(xpath = "//i[text() = 'create_new_folder']")
    private WebElement buttonNew;

    @FindBy(xpath = "//a[contains(@href, '31')]/i[text()='list']")
    private WebElement listButton;

    public BoardBoardPage(WebDriver driver) {
        super(driver);
    }

    public String getPendingText(){
        return boardRow.getText();
    }

    public int getPendingItemsCount(){
        return pendingCardItems.size();
    }

    public void clickBoardButton() {
        boardButton.click();
    }

    public BoardEditPage clickNewFolder() {
        buttonNew.click();
        return new BoardEditPage(getDriver());
    }

    public BoardListPage clickListButton() {
        listButton.click();
        return new BoardListPage(getDriver());
    }

    public BoardBoardPage moveFromPedingToOntrack() {
        getActions().dragAndDrop(pendingCardItems.get(0), onTrackKanbanItem).build().perform();
        return this;
    }
}


