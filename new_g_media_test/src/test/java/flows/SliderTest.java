package flows;

import config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.ErrorUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

public class SliderTest {
    private static final String errorPage = "404";
    private static final By xpathSlides = By.xpath(".//app-rush-slide");
    private static final By xpathA = By.xpath(".//a");

    private static List<String> errors;
    private static WebDriver driver;
    private static String globalURL;

    @BeforeTest
    public void setup() throws IOException {
        Config.loadConfig();
        globalURL = Config.globalURL;
        errors = new LinkedList<>();
        driver = new ChromeDriver();
        driver.get(Config.url);
        driver.get(globalURL);
    }

    @Test
    public void slidesTest() {
        checkSlides(getCurrentSlides());
        checkErrors();
    }

    @AfterTest
    public void close() {
        driver.quit();
    }

    private List<WebElement> getCurrentSlides() {
        return new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(d -> d.findElements(xpathSlides));
    }

    private void checkSlides(List<WebElement> slides) {
        clearPreviousErrors();
        for (int i = 0; i < slides.size(); i++) {
            try {
                checkSlide(slides.get(i), i + 1);
            } catch (StaleElementReferenceException e) {
                checkSlides(getCurrentSlides());
                break;
            }

            List<WebElement> newSlides = getCurrentSlides();
            if(newSlides.size() != slides.size()) {
                checkSlides(newSlides);
                break;
            }
        }
    }

    private void checkSlide(WebElement slide, int numberSlide) {
        List<WebElement> a = slide.findElements(xpathA);
        if(a.isEmpty()) {
            errors.add("Tag A is missing in slide #" + numberSlide);
            return;
        }

        String href = a.get(0).getAttribute("href").replace(globalURL, "");
        if(href.equals(errorPage)) {
            errors.add("Link in slide #" + numberSlide + " leads to a " + errorPage + " page");
        }
    }

    private void checkErrors() {
        Assert.fail(ErrorUtils.errorsToString(errors));
        clearPreviousErrors();
    }

    private void clearPreviousErrors() {
        errors.clear();
    }
}
