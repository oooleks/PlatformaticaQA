import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectUtils;

import java.util.List;
import java.util.Objects;

public class EntityTagTest extends BaseTest {

    @Test
    public void editTest() {

        WebDriver driver = getDriver();
        driver.get("https://ref.eteam.work");

        ProjectUtils.loginProcedure(driver);

        WebElement sidebar = driver.findElement(By.xpath("//div[contains(@class, 'sidebar-wrapper')]"));
        Actions builder = new Actions(driver);
        builder.moveToElement(sidebar).perform();
        WebElement tag = driver.findElement(By.xpath("//li/a/p[contains(text(), 'Tag')]"));
        builder.moveToElement(tag).build().perform();
        tag.click();

        String tagRecordText = createTagEntity(driver);
        String newTagText = createTag(driver);

        tagEntity(driver, tagRecordText).click();

        By newTagLocator = By.xpath("//label[contains(text(), '" + newTagText + "')]");
        WebElement newTag = driver.findElement(newTagLocator);
        builder.moveToElement(newTag).perform();
        newTag.click();
        ProjectUtils.click(driver, assignButton(driver, builder));

        Assert.assertEquals(assignedTag(driver, newTagText).size(), 1);
        tagEntity(driver, tagRecordText).click();
        ProjectUtils.click(driver, assignButton(driver, builder));
        Assert.assertEquals(assignedTag(driver, newTagText).size(), 0);
    }

    @Ignore
    @Test
    public void viewTest() throws InterruptedException {

        WebDriver driver = getDriver();

        Actions builder = new Actions(driver);
        sideBar(driver, builder);
        WebElement tag = driver.findElement(By.xpath("//li/a/p[contains(text(), 'Tag')]"));
        builder.moveToElement(tag).build().perform();
        tag.click();

        String tagRecordText = createTagEntity(driver);
        String newTagText = createTag(driver);

        tagEntity(driver, tagRecordText).click();

        By newTagLocator = By.xpath("//label[contains(text(), '" + newTagText + "')]");
        WebElement newTag = driver.findElement(newTagLocator);
        builder.moveToElement(newTag).perform();
        newTag.click();
        ProjectUtils.click(driver, assignButton(driver, builder));
        By blueTagLocator = By.xpath("//table[@id='pa-all-entities-table']//div[contains(text(), '"+ tagRecordText +"')]/../../../td[2]/span");
        WebElement blueTag = driver.findElement(blueTagLocator);
        Assert.assertEquals(blueTag.getText(), newTagText);

        // Check tag record on dashboard
        sideBar(driver, builder);
        WebElement dashboard = driver.findElement(By.xpath("//li/a/p[contains(text(), 'Dashboard')]"));
        builder.moveToElement(dashboard).build().perform();
        //dashboard.click();
        ProjectUtils.click(driver, dashboard);

        By selectTagButtonLocator = By.xpath("//button[@class='dropdown-toggle btn btn-link']");
        WebElement selectTagButton = driver.findElement(selectTagButtonLocator);
        ProjectUtils.click(driver, selectTagButton);
        String currentText = null;
        WebElement lastElement;
        By lastItemLocator = By.xpath("//ul[@class='dropdown-menu inner show']/li[last()]/a");
        while (!Objects.equals(currentText, (lastElement = getDriver().findElement(lastItemLocator)).getText())) {
            ProjectUtils.scroll(getDriver(), lastElement);
            currentText = lastElement.getText();
            getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(lastItemLocator));
        }
        ProjectUtils.click(driver, lastElement);

        By goButtonLocator = By.xpath("//button[@class='btn btn-warning pa-go-btn']");
        WebElement goButton = driver.findElement(goButtonLocator);
        ProjectUtils.click(driver, goButton);

        By tagRecordsLocator = By.xpath("//table[@id='pa-all-entities-table']//tr/td[6]//div");
        WebElement tagRecords = driver.findElement(tagRecordsLocator);
        ProjectUtils.click(driver, tagRecords);
        WebElement tagString = driver.findElement(By.xpath("//label[text() = 'String']/following::span[1]"));
        Assert.assertEquals(tagString.getText(), tagRecordText);
    }

    private static WebElement sideBar(WebDriver driver, Actions builder) {
        WebElement sidebar = driver.findElement(By.xpath("//div[contains(@class, 'sidebar-wrapper')]"));
        builder.moveToElement(sidebar).perform();
        return sidebar;
    }

    private static WebElement assignButton(WebDriver driver, Actions builder) {
        WebElement assignButton =  driver.findElement(By.xpath("//button[@value='assign']"));
        builder.moveByOffset(10,261).moveToElement(assignButton).perform();
        return assignButton;
    }

    private static List<WebElement> assignedTag(WebDriver driver, String newTagText) {

        By assignedTagLocator = By.xpath("//span[@class='pa-tag'][contains(text(), '" + newTagText + "')]");
        return driver.findElements(assignedTagLocator);
    }

    private static String createTagEntity(WebDriver driver) {
        By createTagIconLocator = By.xpath("//div[@class='card-icon']");
        WebElement createTagIcon = driver.findElement(createTagIconLocator);
        createTagIcon.click();
        WebElement stringInput = driver.findElement(By.xpath("//input[@id='string']"));
        String randomTag = RandomStringUtils.randomNumeric(10);
        stringInput.sendKeys(randomTag);
        WebElement saveButton = driver.findElement(By.xpath("//button[@id='pa-entity-form-save-btn']"));
        saveButton.click();
        return randomTag;
    }

    private static String createTag(WebDriver driver) {
        By tagIconLocator = By.xpath("//li/a[contains(@href, 'index.php?action=action_list&list_type=table&entity_id=42&taggable=1')]");
        WebElement tagIcon = driver.findElement(tagIconLocator);
        tagIcon.click();

        By newTagInputLocator = By.xpath("//input[@name='new_tag']");
        WebElement newTagInput = driver.findElement(newTagInputLocator);
        By newTagButtonLocator = By.xpath("//button[contains(text(), 'New tag')]");
        WebElement newTagButton = driver.findElement(newTagButtonLocator);

        Actions builder = new Actions(driver);
        builder.moveToElement(newTagButton).perform();

        String randomTag = RandomStringUtils.randomNumeric(5);
        newTagInput.sendKeys(randomTag);
        newTagButton.click();
        return randomTag;
    }

    private static WebElement tagEntity(WebDriver driver, String tagRecordText) {
        By tagEntityLocator = By.xpath("//tr//td/a/div[contains(text(), '" + tagRecordText + "')]/../../../td[1]/div");
        return driver.findElement(tagEntityLocator);
    }
}
