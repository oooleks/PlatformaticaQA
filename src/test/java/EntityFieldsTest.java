import model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.type.Run;
import runner.type.RunType;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static runner.ProjectUtils.createUUID;

@Run(run = RunType.Multiple)
public class EntityFieldsTest extends BaseTest {

    private static final String TITLE = createUUID();
    private static final String COMMENTS = RandomStringUtils.randomAlphanumeric(25);
    private static final String INT = Integer.toString(ThreadLocalRandom.current().nextInt(100, 200));
    private static final String DECIMAL = "12.34";
    private static final String DECIMAL_ENDS_ZERO = "1.10";
    private static final String ALL_ZERO_AFTER_DECIMAL = "1.00";
    private static final String DATE = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    private static final String DATE_TIME = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    private static final String NEW_TITLE = String.format("%s_EditTextAllNew", createUUID());
    private static final String NEW_COMMENTS = "New comment text for edit test";
    private static final String NEW_INT = Integer.toString(ThreadLocalRandom.current().nextInt(300, 400));
    private static final String NEW_DECIMAL = "128.01";
    private static final String NEW_DATE = "25/10/2018";
    private static final String NEW_DATE_TIME = "25/10/2018 08:22:05";
    private static final String INVALID_ENTRY = "a";

    private String randomUser = null;
    private String currentUser = null;

    @Test
    public void createRecordTest() {

        currentUser = new MainPage(getDriver()).getCurrentUser();
        final List<String> expectedValues = Arrays.asList(TITLE, COMMENTS, INT, DECIMAL, DATE, DATE_TIME, "", currentUser, "");

        FieldsPage fieldsPage = new FieldsPage(getDriver())
                .clickMenuFields()
                .clickNewFolder()
                .fillTitle(TITLE)
                .fillComments(COMMENTS)
                .fillInt(INT)
                .fillDecimal(DECIMAL)
                .fillDate(DATE)
                .fillDateTime(DATE_TIME)
                .selectUser(currentUser)
                .clickSaveButton();

        Assert.assertEquals(fieldsPage.getRowCount(), 1);
        Assert.assertEquals(fieldsPage.getRow(0), expectedValues);
        Assert.assertEquals(fieldsPage.getRowEntityIcon(0).getAttribute("class"), "fa fa-check-square-o");
    }

    @Test(dependsOnMethods = "deleteRecordTest")
    public void createDraftTest() {

        final List<String> expectedValues = Arrays.asList(TITLE, COMMENTS, "0", "0", "", "", "", currentUser, "");

        FieldsPage fieldsPage = new FieldsPage(getDriver())
                .clickMenuFields()
                .clickNewFolder()
                .fillTitle(TITLE)
                .fillComments(COMMENTS)
                .selectUser(currentUser)
                .clickSaveDraftButton();

        Assert.assertEquals(fieldsPage.getRowCount(), 1);
        Assert.assertEquals(fieldsPage.getRow(0), expectedValues);
        Assert.assertEquals(fieldsPage.getRowEntityIcon(0).getAttribute("class"), "fa fa-pencil");
    }

    @Test(dependsOnMethods = "createRecordTest")
    public void editRecordTest() {

        String[] expectedValues = {NEW_TITLE, NEW_COMMENTS, NEW_INT, NEW_DECIMAL, NEW_DATE, NEW_DATE_TIME, "", randomUser, ""};

        MainPage mainPage = new MainPage(getDriver());
        FieldsEditPage fieldsEditsPage = mainPage.clickMenuFields().editRow(0);

        randomUser = expectedValues[7] = fieldsEditsPage.getRandomUser();
        FieldsPage fieldsPage = fieldsEditsPage
                .fillTitle(NEW_TITLE)
                .fillComments(NEW_COMMENTS)
                .fillInt(NEW_INT)
                .fillDecimal(NEW_DECIMAL)
                .fillDate(NEW_DATE)
                .fillDateTime(NEW_DATE_TIME)
                .selectUser(randomUser)
                .clickSaveButton();

        Assert.assertEquals(fieldsPage.getRowCount(), 1);
        Assert.assertEquals(fieldsPage.getRow(0), Arrays.asList(expectedValues));
        Assert.assertEquals(fieldsPage.getRowEntityIcon(0).getAttribute("class"), "fa fa-check-square-o");
    }

    @Test(dependsOnMethods = "editRecordTest")
    public void deleteRecordTest() {

        FieldsPage fieldsPage = new FieldsPage(getDriver());
        final String recordTitle = fieldsPage.clickMenuFields().getTitle(0);
        fieldsPage.deleteRow(0);

        Assert.assertEquals(fieldsPage.getRowCount(), 0, "Record has not been deleted");

        RecycleBinPage recycleBinPage = fieldsPage.clickRecycleBin();

        Assert.assertEquals(recycleBinPage.getRowCount(), 1);
        Assert.assertTrue(recycleBinPage.getDeletedEntityContent().contains(recordTitle));
    }

    @Test(dependsOnMethods = "createDraftTest")
    public void deleteDraftTest() {

        FieldsPage fieldsPage = new FieldsPage(getDriver());
        final String entityTitle = fieldsPage.clickMenuFields().getTitle(0);
        fieldsPage.deleteRow(0);

        Assert.assertEquals(fieldsPage.getRowCount(), 0, "Draft has not been deleted");

        RecycleBinPage recycleBinPage = fieldsPage.clickRecycleBin();

        Assert.assertEquals(recycleBinPage.getRowCount(), 2);
        Assert.assertTrue(recycleBinPage.getDeletedEntityContent().contains(entityTitle));
    }

    @Test
    public void invalidIntEntryCreateTest() {

        ErrorPage errorPage = new FieldsPage(getDriver())
                .clickMenuFields()
                .clickNewFolder()
                .fillInt(INVALID_ENTRY)
                .clickSaveButtonErrorExpected();

        Assert.assertEquals(errorPage.getErrorMessage(), errorPage.ERROR_MESSAGE);
    }

    @Test
    public void invalidDecimalEntryCreateTest() {

        ErrorPage errorPage = new FieldsPage(getDriver())
                .clickMenuFields()
                .clickNewFolder()
                .fillDecimal(INVALID_ENTRY)
                .clickSaveButtonErrorExpected();

        Assert.assertEquals(errorPage.getErrorMessage(), errorPage.ERROR_MESSAGE);
    }

    @Ignore
    @Test(dependsOnMethods = "deleteDraftTest")
    public void entityDecimalEndsZeroTest() {

        FieldsPage fieldsPage = new FieldsPage(getDriver())
                .clickMenuFields()
                .clickNewFolder()
                .fillDecimal(DECIMAL_ENDS_ZERO)
                .clickSaveButton();

        Assert.assertEquals(fieldsPage.getDecimal(0), DECIMAL_ENDS_ZERO);

        fieldsPage.editRow(0)
                .fillDecimal(ALL_ZERO_AFTER_DECIMAL)
                .clickSaveButton();

        Assert.assertEquals(fieldsPage.getDecimal(0), ALL_ZERO_AFTER_DECIMAL);
    }
}
