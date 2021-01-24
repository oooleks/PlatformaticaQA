import model.Fields1Page;
import model.Main1Page;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;
import runner.type.Run;
import runner.type.RunType;

import java.util.UUID;

@Run(run = RunType.Multiple)
public class EntityFields1Test extends BaseTest {

    private static final String TITLE = UUID.randomUUID().toString();
    private static final String COMMENT = "NEW RECORD";
    private static final String INT_ = "11";
    private static final String NEW_TITLE = UUID.randomUUID().toString();
    private static final String NEW_COMMENT = "EDIT RECORD";
    private static final String NEW_INT_ = "1996";

    @Test
    public void createRecord() {

        Fields1Page fieldsPage1 = new Main1Page(getDriver())
                .clickFields()
                .clickCreateNewFolder()
                .sendKeys(TITLE,COMMENT, INT_)
                .clickSaveBtn();

        Assert.assertEquals(fieldsPage1.getRowCount(), 1);
        Assert.assertEquals(fieldsPage1.getTitleText(), TITLE);
        Assert.assertEquals(fieldsPage1.getCommentsText(), COMMENT);
        Assert.assertEquals(fieldsPage1.getInt_Text(), INT_);
    }

    @Test(dependsOnMethods = "createRecord")
    public void editRecord() {

        Fields1Page fields1Page = new Main1Page(getDriver())
                .clickFields()
                .clickEditRecord()
                .sendKeys(NEW_TITLE, NEW_COMMENT, NEW_INT_)
                .clickSaveBtn();

        Assert.assertEquals(fields1Page.getRowCount(), 1);
        Assert.assertEquals(fields1Page.getTitleText(), NEW_TITLE);
        Assert.assertEquals(fields1Page.getCommentsText(), NEW_COMMENT);
        Assert.assertEquals(fields1Page.getInt_Text(), NEW_INT_);
    }

    @Test(dependsOnMethods = "editRecord")
    public void deleteRecord() {

        Fields1Page fields1Page = new Main1Page(getDriver())
                .clickFields()
                .clickDeleteRecord();

        Assert.assertEquals(fields1Page.getRowCount(), 0);
    }
}
