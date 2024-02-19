package ali.patvari.step_definitions;

import ali.patvari.utilities.AllureSelenide;
import ali.patvari.utilities.BrowserUtils;
import ali.patvari.utilities.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;




public class Hooks {

    private final AllureSelenide allureUtils = new AllureSelenide();

    @Before()
    public void setupMethod(){
        System.out.println("---> @Before: RUNNING BEFORE EACH SCENARIO");
    }

    @After()
    public void teardownMethod(Scenario scenario){
        if (scenario.isFailed()) {
            allureUtils.captureScreenshot(Driver.getDriver());
            allureUtils.capturePageSourceWithStyles(Driver.getDriver());
            allureUtils.browserConsoleLogs();
            allureUtils.addVideo();
        }
        System.out.println("---> @After: RUNNING AFTER EACH SCENARIO");

        BrowserUtils.sleep(2);
        Driver.closeDriver();

    }

}
