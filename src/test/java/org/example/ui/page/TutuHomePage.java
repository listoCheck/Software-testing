package org.example.ui.page;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.example.ui.TutuUiTestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.TimeoutException;

public class TutuHomePage {
    private static final String CONSENT_LABEL = "Даю согласие на обработку персональных данных и рекламные рассылки";
    private static final By IDEAS_HEADING = By.xpath(
        "//*[self::h1 or self::h2 or self::div or self::span][normalize-space()='Идеи для поездок']");
    private static final By HELP_LINK = By.xpath("(//a[contains(@href, '/2read/')])[1]");
    private static final By DESTINATION_FIELD = By.xpath(
        "(//input[@data-ti='input' and @type='text' and not(@name='userEmail')])[1]");
    private static final By EMAIL_FIELD = By.xpath("//input[contains(@aria-label, 'Электронная почта') and @name='userEmail']");
    private static final By SUBSCRIBE_BUTTON = By.xpath(
        "(//button[normalize-space()='Подписаться' or contains(@aria-label, 'Подписаться')])[last()]");
    private static final By CONSENT_CHECKBOX = By.xpath("//input[contains(@aria-label, '" + CONSENT_LABEL + "')]");

    private final TutuUiTestBase base;

    public TutuHomePage(TutuUiTestBase base) {
        this.base = base;
    }

    public void open() {
        base.openHomePage();
    }

    public WebElement serviceLink(String name, String hrefFragment) {
        return base.waitPresent(By.xpath(
            "//a[contains(@href, '" + hrefFragment + "') and .//*[normalize-space()='" + name + "']]"));
    }

    public List<List<String>> mainServices() {
        return List.of(
            List.of("Авиабилеты", "avia.tutu.ru"),
            List.of("Ж/д билеты", "/poezda/"),
            List.of("Автобусы", "bus.tutu.ru"),
            List.of("Отели", "hotel.tutu.ru"),
            List.of("Электрички", "/prigorod/")
        );
    }

    public WebElement ideasHeading() {
        return base.waitPresent(IDEAS_HEADING);
    }

    public void openHelpSection() {
        WebElement link = base.waitPresent(HELP_LINK);
        base.scrollIntoView(link);
        String href = link.getAttribute("href");
        try {
            link.click();
        } catch (WebDriverException ignored) {
            if (href != null && !href.isBlank()) {
                base.openUrl(href);
            } else {
                throw ignored;
            }
        }
    }

    public String helpSectionHref() {
        return base.waitPresent(HELP_LINK).getAttribute("href");
    }

    public void enterHotelDestination(String destination) {
        WebElement field = base.waitPresent(DESTINATION_FIELD);
        base.scrollIntoView(field);
        field.click();
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        field.sendKeys(destination);
    }

    public String hotelDestinationValue() {
        WebElement field = base.waitPresent(DESTINATION_FIELD);
        return field.getDomProperty("value");
    }

    public WebElement emailField() {
        return base.waitPresent(EMAIL_FIELD);
    }

    public void enterEmail(String value) {
        WebElement field = emailField();
        base.scrollIntoView(field);
        field.click();
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        field.sendKeys(Keys.DELETE);
        field.sendKeys(value);
    }

    public void enterInvalidEmail(String value) {
        enterEmail(value);
    }

    public void submitSubscription() {
        base.scrollIntoView(subscribeButton());
        subscribeButton().click();
    }

    public WebElement subscribeButton() {
        return base.waitPresent(SUBSCRIBE_BUTTON);
    }

    public WebElement consentCheckbox() {
        return base.waitPresent(CONSENT_CHECKBOX);
    }

    public void acceptSubscriptionConsent() {
        WebElement checkbox = consentCheckbox();
        base.scrollIntoView(checkbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void clearSubscriptionConsent() {
        WebElement checkbox = consentCheckbox();
        base.scrollIntoView(checkbox);
        if (checkbox.isSelected()) {
            checkbox.click();
        }
    }

    private static final By FLIGHT_TAB = By.xpath(
        "//a[contains(@href, 'avia') and .//*[normalize-space()='Авиабилеты']] | " +
        "//*[self::a or self::button or self::div or self::span][normalize-space()='Авиабилеты']");
    private static final By FLIGHT_ORIGIN = By.xpath(
        "(//input[@type='text' and (" +
            "contains(@placeholder, 'Откуда') or contains(@aria-label, 'Откуда') or contains(@name, 'from')" +
        ")])[1]");
    private static final By FLIGHT_DESTINATION = By.xpath(
        "(//input[@type='text' and (" +
            "contains(@placeholder, 'Куда') or contains(@aria-label, 'Куда') or contains(@name, 'to')" +
        ")])[1]");
    private static final By DEPARTURE_DATE = By.xpath(
        "(//input[" +
            "contains(@placeholder, 'Когда') or contains(@placeholder, 'Туда') or " +
            "contains(@aria-label, 'Дата') or contains(@name, 'date')" +
        "])[1]");
    private static final By SEARCH_BUTTON = By.xpath(
        "(//button[contains(normalize-space(.), 'Найти билеты') or contains(normalize-space(.), 'Найти')])[1]");
    private static final By PASSENGER_SELECTOR = By.xpath(
        "(//*[self::button or self::div or self::span][contains(normalize-space(.), 'Пассажир')])[1]");
    private static final By PASSENGER_DIALOG = By.xpath(
        "(//*[@role='dialog' or contains(@class, 'passenger') or contains(@class, 'traveller')])[1]");
    private static final By APPLY_PASSENGERS_BUTTON = By.xpath(
        "(//button[contains(normalize-space(.), 'Готово') or contains(normalize-space(.), 'Применить')])[last()]");
    private static final By VALIDATION_ERROR = By.xpath(
        "//*[contains(@class, 'error') or contains(@data-ti, 'error')]");

    public void clickFlightTab() {
        base.clickWhenReady(FLIGHT_TAB);
    }

    public void enterFlightOrigin(String city) {
        typeAndSelectSuggestion(FLIGHT_ORIGIN, city);
    }

    public void enterFlightDestination(String city) {
        typeAndSelectSuggestion(FLIGHT_DESTINATION, city);
    }

    public String getFlightOriginValue() {
        return base.waitPresent(FLIGHT_ORIGIN).getDomProperty("value");
    }

    public String getFlightDestinationValue() {
        return base.waitPresent(FLIGHT_DESTINATION).getDomProperty("value");
    }

    public void selectDepartureDate(LocalDate date) {
        WebElement field = base.waitPresent(DEPARTURE_DATE);
        base.scrollIntoView(field);
        field.click();
        String isoDate = formatDateForDatepicker(date);
        List<By> dateOptions = List.of(
            By.xpath("//*[@data-date='" + isoDate + "']"),
            By.xpath("//button[@data-date='" + isoDate + "']"),
            By.xpath("//td[@data-date='" + isoDate + "']"),
            By.xpath("//*[contains(@aria-label, '" + date.format(DateTimeFormatter.ofPattern("d MMMM", java.util.Locale.forLanguageTag("ru"))) + "')]")
        );

        for (By option : dateOptions) {
            List<WebElement> elements = base.findElements(option);
            if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
                base.scrollIntoView(elements.get(0));
                elements.get(0).click();
                return;
            }
        }

        setInputValue(field, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

    public String getDepartureDateValue() {
        return base.waitPresent(DEPARTURE_DATE).getDomProperty("value");
    }

    public void openPassengerSelector() {
        base.clickWhenReady(PASSENGER_SELECTOR);
        try {
            base.waitVisible(PASSENGER_DIALOG);
        } catch (Exception ignored) {
        }
    }

    public void setPassengersCount(int adults, int children, int infants) {
        setPassengerType("adult", "взросл", adults);
        setPassengerType("child", "реб", children);
        setPassengerType("infant", "млад", infants);
    }

    private void setPassengerType(String type, String labelFragment, int count) {
        int increments = Math.max(0, count - ("adult".equals(type) ? 1 : 0));
        if (increments == 0) {
            return;
        }

        By incrementButton = By.xpath(
            "(" +
                "//button[@data-type='" + type + "' and (" +
                    "contains(@class, 'increment') or contains(@aria-label, '+')" +
                ")] | " +
                "//*[contains(translate(normalize-space(.), 'АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ', 'абвгдеёжзийклмнопрстуфхцчшщъыьэюя'), '" + labelFragment + "')]" +
                "/following::button[" +
                    "contains(@aria-label, '+') or contains(@data-ti, 'plus') or contains(@class, 'increment')" +
                "][1]" +
            ")[1]");

        for (int i = 0; i < increments; i++) {
            base.clickWhenReady(incrementButton);
        }
    }

    public void applyPassengerSelection() {
        List<WebElement> buttons = base.findElements(APPLY_PASSENGERS_BUTTON);
        if (!buttons.isEmpty() && buttons.get(0).isDisplayed()) {
            base.scrollIntoView(buttons.get(0));
            buttons.get(0).click();
        }
    }

    public String getPassengerDisplayText() {
        return base.waitPresent(PASSENGER_SELECTOR).getText();
    }

    public void clickSearchButton() {
        WebElement button = searchButton();
        base.scrollIntoView(button);
        button.click();
    }

    public WebElement searchButton() {
        return base.waitPresent(SEARCH_BUTTON);
    }

    public boolean isSearchButtonEnabled() {
        return base.waitPresent(SEARCH_BUTTON).isEnabled();
    }

    public boolean isValidationErrorDisplayed() {
        return base.findElements(VALIDATION_ERROR).size() > 0;
    }

    private String formatDateForDatepicker(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    private void typeAndSelectSuggestion(By locator, String value) {
        WebElement field = base.waitPresent(locator);
        base.scrollIntoView(field);
        field.click();
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        field.sendKeys(Keys.DELETE);
        field.sendKeys(value);

        List<By> suggestionLocators = List.of(
            By.xpath("(//*[@role='listbox']//*[self::li or self::div or self::button])[1]"),
            By.xpath("(//*[contains(@class, 'suggest') or contains(@class, 'autocomplete')]//*[self::li or self::div or self::button])[1]")
        );

        for (By suggestionLocator : suggestionLocators) {
            try {
                WebElement suggestion = base.waitVisible(suggestionLocator);
                base.scrollIntoView(suggestion);
                suggestion.click();
                return;
            } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException ignored) {
            }
        }
    }

    private void setInputValue(WebElement field, String value) {
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        field.sendKeys(Keys.DELETE);
        field.sendKeys(value);
        field.sendKeys(Keys.TAB);
    }
}
