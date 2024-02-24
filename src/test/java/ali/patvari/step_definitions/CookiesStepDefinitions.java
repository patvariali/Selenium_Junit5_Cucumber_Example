package ali.patvari.step_definitions;

import ali.patvari.utilities.AuthUtil;
import ali.patvari.utilities.ConfigurationReader;
import ali.patvari.utilities.Driver;
import io.cucumber.java.en.Given;
import org.junit.Assert;
import org.openqa.selenium.By;

public class CookiesStepDefinitions {

    @Given("I should be logged in")
    public void i_should_be_logged_in() {
        Driver.getDriver().get(ConfigurationReader.getProperty("mainPage"));
        AuthUtil.getCookieForSelenium();
        Driver.getDriver().navigate().refresh();
    }

    @Given("User verifies is logged in successfully")
    public void user_verifies_is_logged_in_successfully() {
        Assert.assertTrue(Driver.getDriver().findElement(By.id("userDropdown")).isDisplayed());
    }

}
