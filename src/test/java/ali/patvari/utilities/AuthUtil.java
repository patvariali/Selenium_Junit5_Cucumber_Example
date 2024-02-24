package ali.patvari.utilities;


import io.restassured.http.ContentType;
import org.openqa.selenium.Cookie;

import java.util.Date;

import static io.restassured.RestAssured.given;

public class AuthUtil {

    public static String getCSRFToken() {
        Driver.getDriver().get(ConfigurationReader.getProperty("mainPage"));
        return Driver.getDriver().manage().getCookieNamed("csrftoken").getValue();
    }

    public static String getSessionId() {
        String csrfToken = getCSRFToken();
        return given()
                .contentType(ContentType.MULTIPART)
                .cookie("csrftoken", csrfToken)
                .multiPart("username", "admin")
                .multiPart("password", "adminat")
                .multiPart("next", "/")
                .multiPart("csrfmiddlewaretoken", csrfToken)
                .post("https://at-sandbox.workbench.lanit.ru/login/")
                .then().log().all().extract().cookie("sessionid");
    }

    public static void getCookieForSelenium() {
        Date expDate = new Date();
        expDate.setTime(expDate.getTime() + (1000*1000));
        Cookie cookie = new Cookie("sessionid", getSessionId(), "at-sandbox.workbench.lanit.ru", "/", expDate);

        Driver.getDriver().manage().addCookie(cookie);
    }
}
