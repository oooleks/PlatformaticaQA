import model.Chain1Page;
import model.MainPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import runner.BaseTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityEventsChain1Test extends BaseTest {
    @Test
    public void createNewRecord(){

        List<String> expectedValues = new ArrayList<>(Arrays. asList(
                "1", "2", "4", "8", "16", "32", "64", "128", "256", "512"));

        MainPage mainPage = new MainPage(getDriver());
        mainPage.clickMenuEventsChain1();

        Chain1Page chain1Page = new Chain1Page(getDriver());
        chain1Page.clickNewFolder()
                  .inputInitialValue()
                  .clickSaveButton();

        Assert.assertEquals(chain1Page.getRowCount(),1);
        Assert.assertEquals(chain1Page.getRow(0),expectedValues);

    }



}
