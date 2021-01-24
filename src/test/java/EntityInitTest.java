import model.InitEditPage;
import model.InitPage;
import model.MainPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.type.Run;
import runner.type.RunType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Run(run = RunType.Multiple)
public class EntityInitTest extends BaseTest {

    private static final String USER_DEFAULT = "User 1 Demo";
    private static final List<String> DEFAULT_VALUES_TABLE = new ArrayList<>(Arrays.asList("New String", "New Text", "2", "3.14", "01/01/2020", "31/12/2020 23:59:59", "1", "Two"));
    private static final List<String> DEFAULT_VALUES_VIEW = new ArrayList<>(Arrays.asList("New String", "New Text", "2", "3.14", "01/01/2020", "31/12/2020 23:59:59", "Two"));
    private static final String[] DEFAULT_VALUES_EDIT = {"New String", "New Text", "2", "3.14", "01/01/2020", "31/12/2020 23:59:59", "Two".toUpperCase()};

    @Test
    public void createDefaultInit() {

        InitPage initPage = new MainPage(getDriver())
                .clickMenuInit()
                .clickNewFolder()
                .clickSaveButton();

        Assert.assertEquals(initPage.getRowCount(), 1);
        Assert.assertTrue(initPage.getDefaultUser().equals(USER_DEFAULT));
        Assert.assertTrue(initPage.getRow(0).equals(DEFAULT_VALUES_TABLE));

    }

    @Test(dependsOnMethods = {"createDefaultInit"})
    public void checkDefaultInitInViewMode() {

        Assert.assertEquals(new MainPage(getDriver())
                .clickMenuInit()
                .viewRow()
                .getValues(), DEFAULT_VALUES_VIEW);

    }

    @Test(dependsOnMethods = {"createDefaultInit"})
    public void checkDefaultValuesInEditMode() {

        InitEditPage initEditPage = new MainPage(getDriver())
                .clickMenuInit()
                .editRow(0);

        final String[] valuesCheck = {
                initEditPage.getStringValue(),
                initEditPage.getTextDataValue(),
                initEditPage.getIntData(),
                initEditPage.decimalDataValue(),
                initEditPage.dateDataValue(),
                initEditPage.dateTimeDataValue(),
                initEditPage.listDataCheck(),
        };

        Assert.assertEquals(Arrays.toString(valuesCheck), Arrays.toString(DEFAULT_VALUES_EDIT));
        Assert.assertEquals(initEditPage.userDataText(), USER_DEFAULT.toUpperCase());
        Assert.assertTrue(initEditPage.switchCheck());
        Assert.assertEquals(initEditPage.listDataCheck(), DEFAULT_VALUES_EDIT[DEFAULT_VALUES_EDIT.length - 1].toUpperCase());
    }


    @Test(dependsOnMethods = {"createDefaultInit"})
    public void deleteRecord() {

        InitPage initPage = new MainPage(getDriver())
                .clickMenuInit()
                .deleteRow();

        Assert.assertEquals(initPage.getRowCount(), 0);
    }
}
