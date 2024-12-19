import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Booking {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private static final By USER_PROFILE_ICON = By.cssSelector("div[data-testid='header-profile-button']");
    private static final By SEARCH_BOX = By.id("ss");
    private static final By SEARCH_BUTTON = By.cssSelector("button[type='submit']");
    private static final By HOTEL_NAME = By.xpath("//span[contains(text(), 'Tolip Hotel Alexandria')]");
    private static final By SEE_AVAILABILITY_BUTTON = By.xpath("//button[contains(text(),'See availability')]");
    private static final By RESERVE_BUTTON = By.xpath("//button[contains(text(), \"I'll reserve\")]");

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void bookingTest() {
        driver.get("https://www.booking.com");

        // Wait for user to sign in manually
        waitForVisibility(USER_PROFILE_ICON);
        System.out.println("User signed in, starting the hotel search.");

        // Search for a hotel
        sendKeysToElement(SEARCH_BOX, "Alexandria");
        clickElement(SEARCH_BUTTON);

        // Highlight and select hotel
        clickElementWithHighlight(HOTEL_NAME);

        // Highlight and click on See Availability
        clickElementWithHighlight(SEE_AVAILABILITY_BUTTON);

        // Verify check-in and check-out dates
        String checkInDate = "1 October 2025";
        String checkOutDate = "14 October 2025";
        verifyTextInPageSource(checkInDate, "Check-in date is not displayed correctly");
        verifyTextInPageSource(checkOutDate, "Check-out date is not displayed correctly");

        // Highlight and reserve room
        clickElementWithHighlight(RESERVE_BUTTON);

        // Verify hotel name
        String hotelName = "Tolip Hotel Alexandria";
        verifyTextInPageSource(hotelName, "Hotel name is incorrect on reservation page!");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    // Utility Methods
    private void waitForVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void clickElement(By locator) {
        waitForVisibility(locator);
        driver.findElement(locator).click();
    }

    private void sendKeysToElement(By locator, String text) {
        waitForVisibility(locator);
        driver.findElement(locator).sendKeys(text);
    }

    private void clickElementWithHighlight(By locator) {
        WebElement element = driver.findElement(locator);
        highlightElement(element);
        element.click();
    }

    private void highlightElement(WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].style.border='3px solid red'", element);
    }

    private void verifyTextInPageSource(String text, String errorMessage) {
        Assert.assertTrue(driver.getPageSource().contains(text), errorMessage);
    }
}
