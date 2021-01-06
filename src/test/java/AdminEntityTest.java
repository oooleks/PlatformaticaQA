import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.ProjectUtils;
import runner.type.Profile;
import runner.type.ProfileType;
import runner.type.Run;
import runner.type.RunType;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Profile(profile = ProfileType.ADMIN)
@Run(run = RunType.Multiple)
public class AdminEntityTest extends BaseTest {

    private static final String[] FIELD_TYPE = {"string", "text", "int", "decimal", "date", "datetime", "file", "user"};

    private String getRandomText() {
        return UUID.randomUUID().toString();
    }

    private int getRandomInteger() {
        Random r = new Random();
        return r.nextInt(Integer.MAX_VALUE);
    }

    private Map getEntityRecord(){
        double random_double = getRandomInteger();
        while (random_double % 10 == 0) {
            random_double = getRandomInteger();
        }
        double finalRandom_double = random_double * 0.01;
        Map<Integer, String> entity_record = new HashMap<Integer, String>() {{
            put(1, getRandomText());
            put(2, getRandomText());
            put(3, String.valueOf(getRandomInteger()));
            put(4, String.format("%.2f", finalRandom_double));
        }};
        return entity_record;
    }

    private final String entity_name = getRandomText();
    private final Map<Integer, String> entity_record = getEntityRecord();

    private void selectRecordAction (WebDriver driver, String action) {
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//tr[@data-index='0']/td/div/button"))).click();
        driver.findElement(By.linkText(action)).click();
    }

    private void goToConfiguration() {
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//a[@id='navbarDropdownProfile']"))).click();
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//a[text()='Configuration']"))).click();
    }

    private void goToEntities() {
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//div[contains(@class,'card-body')]")));
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//i[text()='dynamic_feed']/parent::a"))).click();
    }

    private void commandInCMD(WebDriver driver, String command) {
        WebElement cmd = getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//textarea[@id='pa-cli-cmd']")));
        cmd.click();
        cmd.sendKeys(command);
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable
                (By.xpath("//button[@id='pa-cli-cmd-enter']"))).click();
        getWebDriverWait().until(ExpectedConditions.stalenessOf(driver.findElement
                (By.xpath("//textarea[@id='pa-cli-cmd']"))));
    }

    private void createFieldForEntity(WebDriver driver, int label, String type) throws InterruptedException {
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//h3[contains(text(),'Fields')]")));
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//i[contains(text(),'create_new_folder')]"))).click();
        ProjectUtils.sendKeys(getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//input[@id='pa-adm-new-fields-label']"))), label);
        driver.findElement(By.xpath("//div[@class='filter-option-inner-inner']")).click();
        ProjectUtils.click(driver, getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//span[text()='" + type + "']/preceding-sibling::span/.."))));
        driver.findElement(By.xpath("//button[@id='pa-adm-create-fields-btn']")).click();
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//h3[contains(text(),'Fields')]")));
    }

    private void assertEntityRecords (Map expected_element){
        List<WebElement> actual_entity_record = getWebDriverWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy
                (By.xpath("//table[@id='pa-all-entities-table']//a/div")));
        for (int i = 0; i < entity_record.size(); i++){
            String actual_value = String.valueOf(actual_entity_record.get(i).getText());
            Assert.assertEquals(actual_value, String.valueOf(expected_element.get(i+1)));
        }
    }

    @Test
    public void adminNewEntityTest() throws InterruptedException {
        WebDriver driver = getDriver();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        goToConfiguration();
        goToEntities();
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//span[text()='New...']"))).click();

        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//input[@id='pa-adm-new-entity-label']"))).sendKeys(entity_name);
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//button[@id='pa-adm-create-entity-btn']"))).click();
        Assert.assertEquals(getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//ul[@class='navbar-nav']/div"))).getText(), entity_name);

        for (int i = 0; i < FIELD_TYPE.length; i++) {
            createFieldForEntity(driver, i + 1, FIELD_TYPE[i]);
        }

        List<WebElement> field_label_element = driver.findElements
                (By.xpath("//tbody//td[1]"));
        List<WebElement> field_type_element = driver.findElements
                (By.xpath("//tbody//td[3]"));
        for (int i = 0; i < field_label_element.size(); i++) {
            Assert.assertEquals(field_label_element.get(i).getText(), String.valueOf(i + 1));
            Assert.assertEquals(field_type_element.get(i).getText(), FIELD_TYPE[i]);
        }
    }

    @Test (dependsOnMethods = {"adminNewEntityTest"})
    public void adminNewRecordsTest() {
        WebDriver driver = getDriver();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//p[contains(text(),'" + entity_name + "')]/preceding-sibling::i/parent::a"))).click();
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//i[contains(text(),'create_new_folder')]"))).click();
        for (int i = 1; i < entity_record.size() + 1; i++){
            if (i == 2) {
                getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//textarea[@id='" + i + "']"))).sendKeys(entity_record.get(i));
            } else {
                getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//input[@id='" + i + "']"))).sendKeys(entity_record.get(i));
            }
        }
        ProjectUtils.click(driver, getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//button[@id='pa-entity-form-save-btn']"))));
        assertEntityRecords (entity_record);
    }

    @Test (dependsOnMethods = {"adminNewEntityTest", "adminNewRecordsTest"})
    public void adminEntityRecordActionsTest() {
        WebDriver driver = getDriver();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);

        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//p[contains(text(),'" + entity_name + "')]/preceding-sibling::i/parent::a"))).click();
        selectRecordAction(driver, "view");
        List<WebElement> actual_entity_record = driver.findElements(By.xpath("//label/following-sibling::div//span"));
        for (int i = 0; i < entity_record.size(); i++){
            Assert.assertEquals(actual_entity_record.get(i).getText(), entity_record.get(i + 1));
        }
        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//p[contains(text(),'" + entity_name + "')]/preceding-sibling::i/parent::a"))).click();

        Map<Integer, String> edited_entity_record = getEntityRecord();
        selectRecordAction(driver, "edit");
        for (int i = 1; i < edited_entity_record.size() + 1; i++){
            if (i == 2) {
                getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//textarea[@id='" + i + "']"))).clear();
                getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//textarea[@id='" + i + "']"))).sendKeys(edited_entity_record.get(i));
            } else {
                getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//input[@id='" + i + "']"))).clear();
                getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                        (By.xpath("//input[@id='" + i + "']"))).sendKeys(edited_entity_record.get(i));
            }
        }
        ProjectUtils.click(driver, getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated
                (By.xpath("//button[@id='pa-entity-form-save-btn']"))));
        assertEntityRecords (edited_entity_record);

        selectRecordAction(driver, "delete");
        Assert.assertTrue(driver.findElements(By.xpath("//tr[@data-index='0']")).size() == 0);

        goToConfiguration();
        commandInCMD(driver, "delete entity \"" + entity_name + "\"");
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable
                (By.xpath("//a[@class='simple-text logo-normal']/preceding::div[@class='navbar-minimize']"))).click();
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable
                (By.xpath("//a[@class='simple-text logo-normal']"))).click();
        Assert.assertTrue(driver.findElements
                (By.xpath("//p[contains(text(),'" + entity_name + "')]/preceding-sibling::i/parent::a")).size() == 0);
    }
}