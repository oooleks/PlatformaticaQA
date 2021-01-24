import model.MainPage;
import model.PlatformFuncEditPage;
import model.PlatformFuncPage;
import model.RecycleBinPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.type.Run;
import runner.type.RunType;

import java.util.Arrays;
import java.util.List;

@Run(run = RunType.Multiple)
public class EntityPlatformFunctionsTest extends BaseTest {

    private static final String POSITIVE_INT = "15";
    private static final String POSITIVE_INT_2 = "25";
    private static final String INVALID_LAST_INT = "1a";
    private static final String TEST_TEXT = "Test";
    private static final String TEST_TEXT_10 = RandomStringUtils.randomAlphabetic(10);
    private static final String TEST_TEXT_120 = RandomStringUtils.randomAlphanumeric(120);
    private static final String CONSTANT = "contact@company.com";
    private static final String CUSTOM_CONSTANT = "john.doe@mail.com";
    private static final String DEFAULT_CONSTANT = "contact@company.com";
    private static final String MIN_INT = "-2147483648";
    private static final String MAX_INT = "2147483647";
    private static final String OUT_OF_RANGE_MIN_INT = "-2147483649";
    private static final String ERROR_MESSAGE = "error saving entity";

    @Test
    public void createRecordTest() {

        List<String> expectedValues = Arrays.asList(POSITIVE_INT, TEST_TEXT, DEFAULT_CONSTANT);

        PlatformFuncPage platformFuncPage = new MainPage(getDriver())
                .clickMenuPlatformFunctions()
                .clickNewFolder()
                .fillValues(POSITIVE_INT, TEST_TEXT)
                .clickSaveButton();

        Assert.assertEquals(platformFuncPage.getRowCount(), 1);
        Assert.assertEquals(platformFuncPage.getRow(0), expectedValues);
        Assert.assertEquals(platformFuncPage.getRowEntityIcon(0).getAttribute("class"),
                "fa fa-check-square-o");
    }

    @Test(dependsOnMethods = "createRecordTest")
    public void editRecordTest() {

        List<String> expectedValues = Arrays.asList(POSITIVE_INT_2, TEST_TEXT_10, CUSTOM_CONSTANT);

        PlatformFuncPage platformFuncPage = new MainPage(getDriver())
                .clickMenuPlatformFunctions()
                .editRow()
                .fillValues(POSITIVE_INT_2, TEST_TEXT_10, CUSTOM_CONSTANT)
                .clickSaveButton();

        Assert.assertEquals(platformFuncPage.getRowCount(), 1);
        Assert.assertEquals(platformFuncPage.getRow(0), expectedValues);
        Assert.assertEquals(platformFuncPage.getRowEntityIcon(0).getAttribute("class"),
                "fa fa-check-square-o");
    }

    @Test(dependsOnMethods = "editRecordTest")
    public void deleteRecordTest() {
        PlatformFuncPage platformFuncPage = new MainPage(getDriver())
                .clickMenuPlatformFunctions()
                .deleteRow();

        Assert.assertEquals(platformFuncPage.getRowCount(), 0);

        RecycleBinPage recycleBinPage = platformFuncPage.clickRecycleBin();
        Assert.assertEquals(recycleBinPage.getRowCount(), 1);
        Assert.assertTrue(recycleBinPage.getDeletedEntityContent().contains(POSITIVE_INT_2));
        Assert.assertTrue(recycleBinPage.getDeletedEntityContent().contains(TEST_TEXT_10));
        Assert.assertTrue(recycleBinPage.getDeletedEntityContent().contains(CUSTOM_CONSTANT));
    }

    @Test(dependsOnMethods = "deleteRecordTest")
    public void entityMainLogicTest() {

        final String RECORD_1_LAST_INT = "-1";
        final String RECORD_2_LAST_INT = "0";
        final String RECORD_3_LAST_INT = "1";

        final String RECORD_1_LAST_STRING = "Test text";
        final String RECORD_2_LAST_STRING = String.format("%s suffix", RECORD_1_LAST_STRING);
        final String RECORD_3_LAST_STRING = String.format("%s suffix suffix", RECORD_1_LAST_STRING);

        List<String> firstRecordExpectedValues = Arrays.asList(RECORD_1_LAST_INT, RECORD_1_LAST_STRING, DEFAULT_CONSTANT);
        List<String> secondRecordExpectedValues = Arrays.asList(RECORD_2_LAST_INT, RECORD_2_LAST_STRING, DEFAULT_CONSTANT);
        List<String> thirdRecordExpectedValues = Arrays.asList(RECORD_3_LAST_INT, RECORD_3_LAST_STRING, DEFAULT_CONSTANT);

        PlatformFuncPage platformFuncPage = new MainPage(getDriver())
                .clickMenuPlatformFunctions()
                .clickNewFolder()
                .fillValues(RECORD_1_LAST_INT, RECORD_1_LAST_STRING)
                .clickSaveButton();

        Assert.assertEquals(platformFuncPage.getRowCount(), 1);
        Assert.assertEquals(platformFuncPage.getRow(0), firstRecordExpectedValues);

        PlatformFuncEditPage platformFuncEditPage = platformFuncPage.clickNewFolder();

        Assert.assertEquals(platformFuncEditPage.getLastInt(), RECORD_2_LAST_INT);
        Assert.assertEquals(platformFuncEditPage.getLastString(), RECORD_2_LAST_STRING);

        platformFuncPage = platformFuncEditPage.clickSaveButton();

        Assert.assertEquals(platformFuncPage.getRowCount(), 2);
        Assert.assertEquals(platformFuncPage.getRow(0), firstRecordExpectedValues);
        Assert.assertEquals(platformFuncPage.getRow(1), secondRecordExpectedValues);

        platformFuncEditPage = platformFuncPage.clickNewFolder();

        Assert.assertEquals(platformFuncEditPage.getLastInt(), RECORD_3_LAST_INT);
        Assert.assertEquals(platformFuncEditPage.getLastString(), RECORD_3_LAST_STRING);

        platformFuncPage = platformFuncEditPage.clickSaveButton();

        Assert.assertEquals(platformFuncPage.getRowCount(), 3);
        Assert.assertEquals(platformFuncPage.getRow(0), firstRecordExpectedValues);
        Assert.assertEquals(platformFuncPage.getRow(1), secondRecordExpectedValues);
        Assert.assertEquals(platformFuncPage.getRow(2), thirdRecordExpectedValues);

        platformFuncPage.deleteRow(0).deleteRow(0).deleteRow(0);
    }

    @Test(dependsOnMethods = "entityMainLogicTest")
    public void createCancelTest() {

        PlatformFuncEditPage platformFuncEditPage = new MainPage(getDriver())
                .clickMenuPlatformFunctions()
                .clickNewFolder();
        final String LAST_INT = platformFuncEditPage.getLastInt();

        PlatformFuncPage platformFuncPage = platformFuncEditPage.clickCancelButton();
        Assert.assertEquals(platformFuncPage.getRowCount(), 0);
        Assert.assertEquals(platformFuncPage.clickNewFolder().getLastInt(), LAST_INT);
    }

    @Test(dependsOnMethods = "createCancelTest")
    public void createDraftTest() {

        List<String> expectedValues = Arrays.asList(POSITIVE_INT, TEST_TEXT, CONSTANT);

        PlatformFuncPage platformFuncPage = new MainPage(getDriver())
                .clickMenuPlatformFunctions()
                .clickNewFolder()
                .fillValues(POSITIVE_INT, TEST_TEXT)
                .clickSaveDraftButton();

        Assert.assertEquals(platformFuncPage.getRowCount(), 1);
        Assert.assertEquals(platformFuncPage.getRow(0), expectedValues);
        Assert.assertEquals(platformFuncPage.getRowEntityIcon(0).getAttribute("class"), "fa fa-pencil");

        platformFuncPage.deleteRow();
    }

    @Test(dependsOnMethods = "createDraftTest")
    public void viewRecordLongStringTest() {

        List<String> expectedValues = Arrays.asList(POSITIVE_INT, TEST_TEXT_120, CONSTANT);

        Assert.assertEquals(new MainPage(getDriver())
                .clickMenuPlatformFunctions()
                .clickNewFolder()
                .fillValues(POSITIVE_INT, TEST_TEXT_120)
                .clickSaveButton()
                .getRow(0), expectedValues);

        Assert.assertEquals(new MainPage(getDriver())
                .clickMenuPlatformFunctions()
                .viewRow()
                .getValues(), expectedValues);
    }

    @Test(dependsOnMethods = "viewRecordLongStringTest")
    public void invalidLastIntTest() {

        Assert.assertEquals(new MainPage(getDriver())
                .clickMenuPlatformFunctions()
                .clickNewFolder()
                .fillValues(INVALID_LAST_INT, TEST_TEXT)
                .clickSaveButtonErrorExpected()
                .getErrorMessage(), ERROR_MESSAGE);
    }

    @Test(dependsOnMethods = "invalidLastIntTest")
    public void maxBoundaryIntTest() {

        List<String> expectedValues = Arrays.asList(MAX_INT, TEST_TEXT, CONSTANT);

        PlatformFuncPage platformFuncPage = new MainPage(getDriver())
                .clickMenuPlatformFunctions()
                .clickNewFolder()
                .fillValues(MAX_INT, TEST_TEXT)
                .clickSaveButton();

        Assert.assertEquals(platformFuncPage.getRow(1), expectedValues);

        Assert.assertEquals(platformFuncPage
                .clickNewFolder()
                .clickSaveButtonErrorExpected()
                .getErrorMessage(), ERROR_MESSAGE);
    }

    @Test(dependsOnMethods = "maxBoundaryIntTest")
    public void minBoundaryIntTest() {

        List<String> expectedValues = Arrays.asList(MIN_INT, TEST_TEXT, CONSTANT);

        PlatformFuncPage platformFuncPage = new MainPage(getDriver())
                .clickMenuPlatformFunctions()
                .clickNewFolder()
                .fillValues(MIN_INT, TEST_TEXT)
                .clickSaveButton();

        Assert.assertEquals(platformFuncPage.getRow(2), expectedValues);

        Assert.assertEquals(platformFuncPage
                .clickNewFolder()
                .fillLastInt(OUT_OF_RANGE_MIN_INT)
                .clickSaveButtonErrorExpected()
                .getErrorMessage(), ERROR_MESSAGE);
    }
}
