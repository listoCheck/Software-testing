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
        clickHotelsFormTab();
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
        "(" +
            // ссылка на avia (любой href с 'avia')
            "//a[contains(@href,'avia')] | " +
            // таб с role=tab
            "//*[@role='tab'][" +
            "  contains(normalize-space(),'Авиа') or " +
            "  .//*[contains(normalize-space(),'Авиа')]] | " +
            // любой элемент с коротким авиа-текстом (contains, не точное совпадение)
            "//*[self::button or self::li or self::label or " +
            "    self::a or self::div or self::span][" +
            "  (contains(normalize-space(),'Авиабилеты') or " +
            "   (contains(normalize-space(),'Авиа') and string-length(normalize-space())<=15)) or " +
            "  .//*[contains(normalize-space(),'Авиабилеты') or " +
            "       (contains(normalize-space(),'Авиа') and string-length(normalize-space(.))<=15)]]" +
        ")[1]");
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
            "contains(@placeholder, 'Дата') or contains(@placeholder, 'дата') or " +
            "contains(@aria-label, 'Дата') or contains(@aria-label, 'Когда') or " +
            "contains(@name, 'date') or contains(@name, 'when') or " +
            "contains(@data-ti, 'date') or @type='date'" +
        "])[1]");
    private static final By SEARCH_BUTTON = By.xpath(
        "(//button[" +
            "contains(normalize-space(.), 'Найти авиабилеты') or " +
            "contains(normalize-space(.), 'Найти билеты') or " +
            "contains(normalize-space(.), 'Найти')" +
        "])[1]");
    private static final By PASSENGER_SELECTOR = By.xpath(
        "(//*[contains(@class,'passenger-selector') or @id='pax-btn'])[1]");
    private static final By PASSENGER_DIALOG = By.xpath(
        "(//*[@role='dialog' or contains(@class, 'passenger') or contains(@class, 'traveller')])[1]");
    private static final By APPLY_PASSENGERS_BUTTON = By.xpath(
        "(//button[contains(normalize-space(.), 'Готово') or contains(normalize-space(.), 'Применить')])[last()]");
    private static final By VALIDATION_ERROR = By.xpath(
        "//*[contains(@class, 'error') or contains(@data-ti, 'error')]");

    private static final By HOTELS_FORM_TAB = By.xpath(
        "(//a[contains(@href,'hotel')] | " +
        "//*[self::button or self::div or self::li or self::span or self::label]" +
        "[normalize-space()='Отели' or normalize-space()='Отель'])[1]");

    private static final By TRAIN_FORM_TAB = By.xpath(
        "(//a[contains(@href,'/poezda/')] | " +
        "//*[self::button or self::div or self::li or self::span or self::label]" +
        "[normalize-space()='Ж/д билеты' or normalize-space()='Поезда' or normalize-space()='ЖД'])[1]");

    private static final By SWAP_DIRECTION_BUTTON = By.xpath(
        "(//button[@data-ti='changeDirection' or @data-ti='swap' or " +
        "contains(@class,'swap') or contains(@class,'Swap') or " +
        "contains(@class,'exchange') or contains(@class,'Exchange') or " +
        "contains(@class,'reverse') or contains(@class,'Switch') or " +
        "contains(translate(@aria-label,'ПОМЕНЯТЬ','поменять'),'поменять') or " +
        "contains(translate(@aria-label,'ОБМЕНЯТЬ','обменять'),'обменять')])[1]");

    private static final By ONE_WAY_OPTION = By.xpath(
        "(//*[normalize-space()='В одну сторону' or " +
        "(@type='radio' and (@value='one_way' or @value='oneway' or @value='OW'))])[1]");

    private static final By ROUND_TRIP_OPTION = By.xpath(
        "(//*[normalize-space()='Туда-обратно' or " +
        "(@type='radio' and (@value='round_trip' or @value='roundtrip' or @value='RT'))])[1]");

    private static final By RETURN_DATE_FIELD = By.xpath(
        "(//input[contains(@placeholder,'Обратно') or contains(@aria-label,'Обратно') or " +
        "contains(@name,'back') or contains(@name,'return') or " +
        "contains(@placeholder,'Return') or contains(@data-ti,'return')])[1]");

    private static final By DATEPICKER_VISIBLE = By.xpath(
        "(//*[contains(@class,'calendar') or contains(@class,'Calendar') or " +
        "contains(@class,'datepicker') or contains(@class,'Datepicker') or " +
        "(@role='dialog' and .//*[contains(@class,'day') or @data-date])])[1]");

    private static final By DATEPICKER_NEXT_MONTH = By.xpath(
        "(//*[contains(@class,'calendar') or contains(@class,'Calendar') or " +
        "contains(@class,'datepicker') or contains(@class,'Datepicker') or @role='dialog']" +
        "//button[contains(@class,'next') or contains(@class,'Next') or " +
        "contains(@class,'forward') or contains(@class,'Forward') or " +
        "contains(@class,'right') or contains(@class,'Right') or " +
        "contains(@aria-label,'следующий') or contains(@aria-label,'вперёд') or " +
        "contains(@aria-label,'вперед') or normalize-space()='>'])[1]");

    private static final By DATEPICKER_MONTH_TITLE = By.xpath(
        "(//*[contains(@class,'month') and (contains(@class,'title') or " +
        "contains(@class,'header') or contains(@class,'label') or " +
        "contains(@class,'name') or contains(@class,'caption') or " +
        "contains(@class,'heading'))])[1]");

    private static final By HOTEL_CHECKIN_FIELD = By.xpath(
        "(//input[contains(@placeholder,'Заезд') or contains(@aria-label,'Заезд') or " +
        "contains(@name,'checkin') or contains(@name,'check_in') or " +
        "contains(@data-ti,'checkin')])[1]");

    private static final By HOTEL_GUESTS_WIDGET = By.xpath(
        "(//*[self::button or self::div or self::input or self::span]" +
        "[contains(translate(normalize-space(.),'ГОСТЬ','гость'),'гост') or " +
        "contains(@aria-label,'гост') or @data-ti='guests' or " +
        "contains(translate(normalize-space(.),'КОМНАТ','комнат'),'комнат')])[1]");

    private static final By IDEAS_SECTION_HEADING_LOC = By.xpath(
        "//*[self::h2 or self::h3 or self::h4 or self::div or self::span]" +
        "[contains(normalize-space(),'Идеи для поездок')]");

    private static final By IDEAS_SECTION_CARDS = By.xpath(
        "//*[contains(normalize-space(),'Идеи для поездок')]" +
        "/following::a[contains(@href,'/')][.//img or " +
        "contains(@class,'card') or contains(@class,'item') or " +
        "contains(@class,'place') or contains(@class,'destination') or " +
        "contains(@class,'tour')][position()<=12]");

    private static final By FIELD_WITH_ERROR = By.xpath(
        "(//*[@aria-invalid='true' or contains(@class,'error') or " +
        "contains(@class,'Error') or contains(@class,'invalid') or " +
        "contains(@data-ti,'error')])[self::input or " +
        "not(self::button)][1]");

    public void clickHotelsFormTab() {
        base.clickWhenReady(HOTELS_FORM_TAB);
    }

    public void clickTrainFormTab() {
        base.clickWhenReady(TRAIN_FORM_TAB);
    }

    public boolean isPassengerSelectorVisible() {
        List<WebElement> els = base.findElements(PASSENGER_SELECTOR);
        if (els.isEmpty()) return false;
        try { return els.get(0).isDisplayed(); } catch (Exception e) { return false; }
    }

    public boolean isHotelGuestsWidgetVisible() {
        List<WebElement> els = base.findElements(HOTEL_GUESTS_WIDGET);
        if (els.isEmpty()) return false;
        try { return els.get(0).isDisplayed(); } catch (Exception e) { return false; }
    }

    public void clickSwapDirectionButton() {
        base.clickWhenReady(SWAP_DIRECTION_BUTTON);
    }

    public void selectRoundTrip() {
        List<WebElement> opts = base.findElements(ROUND_TRIP_OPTION);
        if (!opts.isEmpty()) {
            base.scrollIntoView(opts.get(0));
            opts.get(0).click();
        }
    }

    public void selectOneWay() {
        List<WebElement> opts = base.findElements(ONE_WAY_OPTION);
        if (!opts.isEmpty()) {
            base.scrollIntoView(opts.get(0));
            opts.get(0).click();
        }
    }

    public boolean isReturnDateFieldVisible() {
        List<WebElement> els = base.findElements(RETURN_DATE_FIELD);
        if (els.isEmpty()) return false;
        try { return els.get(0).isDisplayed(); } catch (Exception e) { return false; }
    }

    public void clickDepartureDate() {
        WebElement field = base.waitPresent(DEPARTURE_DATE);
        base.scrollIntoView(field);
        field.click();
    }

    public boolean isDatepickerVisible() {
        List<WebElement> els = base.findElements(DATEPICKER_VISIBLE);
        if (els.isEmpty()) return false;
        try { return els.get(0).isDisplayed(); } catch (Exception e) { return false; }
    }

    public String getDatepickerMonthTitle() {
        try {
            return base.waitVisible(DATEPICKER_MONTH_TITLE).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public void clickDatepickerNextMonth() {
        base.clickWhenReady(DATEPICKER_NEXT_MONTH);
    }

    public void clickFirstAvailableDayInDatepicker() {
        By availableDays = By.xpath(
            "//*[@data-date and not(contains(@class,'disabled')) and " +
            "not(contains(@class,'past')) and not(contains(@class,'prev-month')) and " +
            "not(contains(@class,'next-month'))]");
        List<WebElement> days = base.findElements(availableDays);
        for (WebElement day : days) {
            try {
                if (day.isDisplayed()) {
                    base.scrollIntoView(day);
                    day.click();
                    return;
                }
            } catch (Exception ignored) {}
        }
    }

    public String getPassengerButtonText() {
        return base.waitPresent(PASSENGER_SELECTOR).getText().trim();
    }

    public void clickIncrementAdults() {
        By adultPlusBtn = By.xpath(
            "(//*[contains(translate(normalize-space(.)," +
            "'АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ','абвгдеёжзийклмнопрстуфхцчшщъыьэюя'),'взросл')]" +
            "/following::button[contains(@aria-label,'+') or contains(@data-ti,'plus') or " +
            "contains(@class,'increment') or normalize-space()='+'][1])[1]");
        base.clickWhenReady(adultPlusBtn);
    }

    public void scrollToIdeasSection() {
        List<WebElement> headings = base.findElements(IDEAS_SECTION_HEADING_LOC);
        if (!headings.isEmpty()) {
            base.scrollIntoView(headings.get(0));
        } else {
            base.scrollToFraction(0.65);
        }
    }

    public WebElement ideasSectionHeading() {
        return base.waitPresent(IDEAS_SECTION_HEADING_LOC);
    }

    public List<WebElement> getIdeasCards() {
        return base.findElements(IDEAS_SECTION_CARDS);
    }

    public boolean isAnyFieldInErrorState() {
        return !base.findElements(FIELD_WITH_ERROR).isEmpty();
    }

    public boolean isOnLandingPage() {
        String url = base.currentUrl();
        boolean isSnapshot = url.startsWith("file://") && url.contains("main.html");
        boolean isLive = url.contains("tutu.ru") && !url.contains("/avia/")
            && !url.contains("/poezda/") && !url.contains("/bus/");
        return isSnapshot || isLive;
    }

    public void clickHotelCheckinField() {
        List<WebElement> fields = base.findElements(HOTEL_CHECKIN_FIELD);
        if (!fields.isEmpty()) {
            base.scrollIntoView(fields.get(0));
            fields.get(0).click();
        }
    }

    public String getHotelCheckinValue() {
        List<WebElement> fields = base.findElements(HOTEL_CHECKIN_FIELD);
        if (fields.isEmpty()) return "";
        return fields.get(0).getDomProperty("value");
    }

    public void clickFlightTab() {
        base.clickWhenReady(FLIGHT_TAB);
    }

    public boolean isFlightTabActive() {
        List<WebElement> tabs = base.findElements(FLIGHT_TAB);
        if (tabs.isEmpty()) {
            return false;
        }

        WebElement tab = tabs.get(0);
        String ariaSelected = tab.getAttribute("aria-selected");
        String className = tab.getAttribute("class");
        return "true".equalsIgnoreCase(ariaSelected)
            || (className != null && className.toLowerCase().contains("active"));
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
        setPassengerType("child", "дет", children);
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
