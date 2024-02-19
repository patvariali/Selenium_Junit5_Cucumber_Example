package ali.patvari.utilities;

import io.qameta.allure.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


public class AllureSelenide {

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] captureScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Page source", type = "text/html")
    public String capturePageSourceWithStyles(WebDriver driver) {
        if (driver instanceof JavascriptExecutor) {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            String cssStyles = (String) jsExecutor.executeScript(
                    "return Array.from(document.styleSheets).flatMap(sheet => " +
                            "Array.from(sheet.cssRules || []).map(rule => rule.cssText)).join('\\n')");
            String pageSource = driver.getPageSource();
            String styleTag = "<style>" + cssStyles + "</style>";
            // Вставляю стили в конце head
            pageSource = pageSource.replace("</head>", styleTag + "</head>");
            return pageSource;
        } else {
            System.out.println("Driver does not support JavaScript execution.");
            return driver.getPageSource();
        }
    }

}
