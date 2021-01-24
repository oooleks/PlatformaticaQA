import model.Chain2Page;
import model.ErrorPage;
import model.MainPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.type.Run;
import runner.type.RunType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Run(run = RunType.Multiple)
public class EntityEventsChain2Test extends BaseTest {

    private static final List<String> EXPECTED_VALUES_F1_1 = new ArrayList<>(Arrays. asList(
            "1", "1", "2", "3", "5", "8", "13", "21", "34", "55"));
    private static final List<String> EXPECTED_VALUES_F1_0 = new ArrayList<>(Arrays. asList(
            "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"));
    private static final List<String> EXPECTED_VALUES_UUID = new ArrayList<>(Arrays. asList(
            RandomStringUtils.randomAlphanumeric(10), "text", "text", "text", "text", "text", "text", "text",
            "text", "text"));

    @Test
    public void createNewRecord() {
        final String f1Value = "1";

        Chain2Page chain2Page = new MainPage(getDriver())
                .clickMenuEventsChain2()
                .clickNewFolder()
                .inputF1Value(f1Value)
                .clickSaveButton();

        Assert.assertEquals(chain2Page.getRowCount(), 1);
        Assert.assertEquals(chain2Page.getRow(0), EXPECTED_VALUES_F1_1);
    }

    @Test(dependsOnMethods = "createNewRecord")
    public void verifyRecordView() {
        Assert.assertEquals(new MainPage(getDriver())
                .clickMenuEventsChain2()
                .viewRow()
                .getValues(), EXPECTED_VALUES_F1_1);
    }

    @Test(dependsOnMethods = {"verifyRecordView"})
    public void verifyRecordEdit() {
        Assert.assertEquals(new MainPage(getDriver())
                .clickMenuEventsChain2()
                .editRow()
                .getActualValues(), EXPECTED_VALUES_F1_1);
    }

    @Ignore
    @Test(dependsOnMethods = {"verifyRecordEdit"})
    public void editRecord() {
        final String newF1 = "0";

        Chain2Page chain2Page = new MainPage(getDriver())
                .clickMenuEventsChain2()
                .editRow()
                .editF1Value(newF1, EXPECTED_VALUES_F1_0)
                .clickSaveButton();

        Assert.assertEquals(chain2Page.getRowCount(), 1);
        Assert.assertEquals(chain2Page.getRow(0), EXPECTED_VALUES_F1_0);
    }

    @Ignore
    @Test(dependsOnMethods = {"editRecord"})
    public void editRecordInvalidValues() {

        ErrorPage errorPage = new MainPage(getDriver())
                .clickMenuEventsChain2()
                .editRow()
                .editValues(EXPECTED_VALUES_UUID)
                .clickSaveButtonReturnError();

        Assert.assertEquals(errorPage.getErrorMessage(), "error saving entity");

        Chain2Page chain2Page = Chain2Page.getPage(getDriver());
        Assert.assertEquals(chain2Page.getRowCount(), 1);
        Assert.assertEquals(chain2Page.getRow(0), EXPECTED_VALUES_F1_0);
    }

    @Ignore
    @Test(dependsOnMethods = {"editRecordInvalidValues"})
    public void deleteRecord() {
        Chain2Page chain2Page = new MainPage(getDriver())
                .clickMenuEventsChain2()
                .deleteRow();

        Assert.assertEquals(chain2Page.getRowCount(), 0);
    }
}