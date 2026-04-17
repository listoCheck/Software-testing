package org.example.ui;

import java.time.Duration;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class TutuUiTestBase {
    protected static final String LIVE_URL = "https://www.tutu.ru/";
    protected static final String SNAPSHOT_URL = Paths.get("docs/tasks/lab3/site/main.html")
        .toAbsolutePath()
        .toUri()
        .toString();
    protected static final String BASE_URL = System.getProperty("ui.baseUrl", SNAPSHOT_URL);

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setUp() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        driver = switch (browser) {
            case "firefox" -> createFirefoxDriver(headless);
            case "chrome" -> createChromeDriver(headless);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };

        driver.manage().window().setSize(new Dimension(1600, 1200));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void openHomePage() {
        driver.get(BASE_URL);
        wait.until(driver -> "complete".equals(
            ((JavascriptExecutor) driver).executeScript("return document.readyState;")));
        waitForAnyVisible(List.of(
            By.xpath("//*[self::a or self::button or self::div or self::span][normalize-space()='Авиабилеты']"),
            By.xpath("//*[self::a or self::button or self::div or self::span][normalize-space()='Отели']"),
            By.xpath("//*[self::h1 or self::h2 or self::div or self::span][contains(normalize-space(), 'Путешествуйте')]"),
            By.xpath("//*[self::h1 or self::h2 or self::div or self::span][normalize-space()='Идеи для поездок']"),
            By.xpath("//input[contains(@placeholder, 'Город') or contains(@aria-label, 'Город') or contains(@aria-label, 'Электронная почта')]")
        ));
        dismissCookieBannerIfPresent();
    }

    public WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public List<WebElement> waitVisibleAll(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    public List<WebElement> waitVisibleMatching(By locator, Predicate<List<WebElement>> predicate) {
        return wait.until(driver -> {
            List<WebElement> visibleElements = driver.findElements(locator).stream()
                .filter(WebElement::isDisplayed)
                .toList();
            return predicate.test(visibleElements) ? visibleElements : null;
        });
    }

    public void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
    }

    public void clickWhenReady(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollIntoView(element);
        element.click();
    }

    public boolean isHtml5Valid(WebElement element) {
        return Boolean.TRUE.equals(
            ((JavascriptExecutor) driver).executeScript("return arguments[0].checkValidity();", element));
    }

    public WebElement waitForAnyVisible(List<By> locators) {
        return wait.until(driver -> {
            for (By locator : locators) {
                List<WebElement> elements = driver.findElements(locator);
                for (WebElement element : elements) {
                    try {
                        if (element.isDisplayed()) {
                            return element;
                        }
                    } catch (StaleElementReferenceException ignored) {
                        // Try the next locator when the DOM is re-rendered during page hydration.
                    }
                }
            }
            return null;
        });
    }

    public void waitUntilUrlContains(String expectedFragment) {
        wait.until(driver -> driver.getCurrentUrl().contains(expectedFragment));
    }

    private WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=ru-RU");
        options.addArguments("--window-size=1600,1200");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        if (headless) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }

    private WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("intl.accept_languages", "ru-RU,ru");
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    private void dismissCookieBannerIfPresent() {
        List<By> candidates = List.of(
            By.xpath("//button[contains(normalize-space(.), 'Принять')]"),
            By.xpath("//button[contains(normalize-space(.), 'Понятно')]"),
            By.xpath("//button[contains(normalize-space(.), 'Соглас')]")
        );

        for (By locator : candidates) {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                scrollIntoView(elements.get(0));
                elements.get(0).click();
                return;
            }
        }
    }
}
