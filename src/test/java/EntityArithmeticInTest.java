import model.ArithmeticInlinePage;
import model.MainPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.type.Run;
import runner.type.RunType;

import java.util.Arrays;
import java.util.List;

@Run(run = RunType.Multiple)
public class EntityArithmeticInTest extends BaseTest {

    private static final String NUM_1 = "20";
    private static final String NUM_2 = "5";
    private static final String SUM = "25";
    private static final String SUB = "15";
    private static final String MUL = "100";
    private static final String DIV = "4";
    private static final String NUMERIC_CHAR ="5";
    private static final String ALPHABETIC_CHAR ="t";
    private static final String ERROR_MESSAGE ="error saving entity";

    @DataProvider(name = "positiveTestData")
    private Object[][] testData1() {
        return new Object[][] {
                {"8", "2", "10", "6", "16", "4"},
                {"8", "-2", "6", "10", "-16", "-4"},
                {"-8", "2", "-6", "-10", "-16", "-4"},
                {"-8", "-2", "-10", "-6", "16", "4"}
        };
    }

    @DataProvider(name = "negativeTestData")
    private Object[][] testData2() {
        return new Object[][] {
                {NUMERIC_CHAR, ALPHABETIC_CHAR},
                {ALPHABETIC_CHAR, NUMERIC_CHAR}
        };
    }

    @Test
    public void createRecordTest() {

        final List<String> expectedValues = Arrays.asList(NUM_1, NUM_2, SUM, SUB, MUL, DIV);

        ArithmeticInlinePage arithmeticInlinePage = new MainPage(getDriver())
                .clickMenuArithmeticInline()
                .clickNewFolder()
                .fillF1F2(NUM_1, NUM_2)
                .waitSumToBe(SUM)
                .waitSubToBe(SUB)
                .waitMulToBe(MUL)
                .waitDivToBe(DIV)
                .clickSaveButton();

        Assert.assertEquals(arithmeticInlinePage.getRowCount(), 1);
        Assert.assertEquals(arithmeticInlinePage.getRow(0), expectedValues);
        Assert.assertEquals(arithmeticInlinePage.getRowEntityIcon(0).getAttribute("class"),
                "fa fa-check-square-o");
    }

    @Test(dependsOnMethods = "createRecordTest")
    public void deleteRecordTest() {

        ArithmeticInlinePage arithmeticInlinePage = new MainPage(getDriver())
                .clickMenuArithmeticInline()
                .deleteRow();

        Assert.assertEquals(arithmeticInlinePage.getRowCount(), 0);
        Assert.assertEquals(arithmeticInlinePage.clickRecycleBin().getDeletedEntityContent(),
                (String.format("F1: %sF2: %sSUM: %sSUB: %sMUL: %sDIV: %s", NUM_1, NUM_2, SUM, SUB, MUL, DIV)));
    }

    @Test(dependsOnMethods = "deleteRecordTest", dataProvider = "positiveTestData")
    public void parametrizedCreateRecordTest(String num_1, String num_2, String sum, String sub, String mul, String div) {

        final List<String> expectedValues = Arrays.asList(num_1, num_2, sum, sub, mul, div);

        ArithmeticInlinePage arithmeticInlinePage = new MainPage(getDriver())
                .clickMenuArithmeticInline();
        int rowCount = arithmeticInlinePage.getRowCount();

        arithmeticInlinePage
                .clickNewFolder()
                .fillF1F2(num_1, num_2)
                .waitSumToBe(sum)
                .waitSubToBe(sub)
                .waitMulToBe(mul)
                .waitDivToBe(div)
                .clickSaveButton();

        Assert.assertEquals(arithmeticInlinePage.getRowCount(), rowCount + 1);
        Assert.assertEquals(arithmeticInlinePage.getRow(rowCount), expectedValues);
    }

    @Test(dependsOnMethods = "parametrizedCreateRecordTest", dataProvider = "negativeTestData")
    public void invalidEntryTest(String f1Value, String f2Value) {

        Assert.assertEquals(new MainPage(getDriver())
                .clickMenuArithmeticInline()
                .clickNewFolder()
                .fillF1F2(f1Value, f2Value)
                .clickSaveButtonErrorExpected()
                .getErrorMessage(), ERROR_MESSAGE);
    }
}
