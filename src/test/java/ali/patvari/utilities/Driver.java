package ali.patvari.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

public class Driver {

    private Driver(){}

    private static InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    public static WebDriver getDriver(){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*", "--window-size=1920,1080", "--headless", "--disable-gpu");
        if(driverPool.get() == null){

            String browserType="";


            if(System.getProperty("browser")!=null){
                browserType=System.getProperty("browser");
            }else {
                browserType=ConfigurationReader.getProperty("browser");
            }

            System.out.println("******************************");
            System.out.println("*******"+browserType+"***********");
            System.out.println("******************************");

            switch (browserType){
                case "remote-chrome":
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--remote-allow-origins=*", "--window-size=1920,1080");
                    options.setCapability("selenoid:options", new HashMap<String, Object>() {{
                        put("sessionTimeout", "10m");
                        put("enableVideo", true);
                    }});

                    try {
                        RemoteWebDriver remoteDriver = new RemoteWebDriver(new URL("http://0.0.0.0:3444/wd/hub"), options);
                        driverPool.set(remoteDriver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case "chrome":
                    driverPool.set(new ChromeDriver(chromeOptions));
                    driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;
                case "firefox":
                    driverPool.set(new FirefoxDriver());
                    driverPool.get().manage().window().maximize();
                    driverPool.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    break;
            }

        }

        return driverPool.get();

    }

    public static void closeDriver(){
        if (driverPool.get()!=null){

            driverPool.get().quit();

            driverPool.remove();
        }
    }

}
