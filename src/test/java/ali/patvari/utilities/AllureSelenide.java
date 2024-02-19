package ali.patvari.utilities;

import io.qameta.allure.*;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;


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

    @Attachment(value = "{attachName}", type = "text/plain")
    private String attachAsText(String attachName, String message) {
        return message;
    }

    public void browserConsoleLogs() {
        LogEntries logEntries = Driver.getDriver().manage().logs().get(LogType.BROWSER);

        String allLogs = logEntries.getAll().stream()
                .map(logEntry -> new java.util.Date(logEntry.getTimestamp()) + " " + logEntry.getLevel() + " " + logEntry.getMessage())
                .collect(Collectors.joining("\n"));

        attachAsText("Browser console logs", allLogs);

    }

    @Attachment(value = "Video", type = "text/html", fileExtension = ".html")
    public String addVideo() {

        return "<html><body><video width='100%' height='100%' controls autoplay><source src='"
                + getVideoUrl()
                + "' type='video/mp4'></video></body></html>";

    }

    private URL getVideoUrl() {

        String videoUrl = "http://localhost:8080/video/" + ((RemoteWebDriver) Driver.getDriver()).getSessionId().toString() + ".mp4";

        try {
            return new URL(videoUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;

    }

}
