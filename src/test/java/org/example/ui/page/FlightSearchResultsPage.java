package org.example.ui.page;

import java.util.List;

import org.example.ui.TutuUiTestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class FlightSearchResultsPage {
    private static final By AVIA_SECTION = By.xpath(
        "//*[self::h1 or self::h2 or self::div or self::span or self::a or self::button]" +
            "[contains(normalize-space(.), 'Авиабилеты') or contains(normalize-space(.), 'билет')]");
    private static final By SEARCH_ORIGIN = By.xpath(
        "(//input[@type='text' and (" +
            "contains(@placeholder, 'Откуда') or contains(@aria-label, 'Откуда') or contains(@name, 'from')" +
        ")])[1]");
    private static final By SEARCH_DESTINATION = By.xpath(
        "(//input[@type='text' and (" +
            "contains(@placeholder, 'Куда') or contains(@aria-label, 'Куда') or contains(@name, 'to')" +
        ")])[1]");
    private static final By SEARCH_DATE = By.xpath(
        "(//input[" +
            "contains(@placeholder, 'Когда') or contains(@placeholder, 'Туда') or " +
            "contains(@aria-label, 'Дата') or contains(@name, 'date')" +
        "])[1]");
    private static final By SELECT_TICKET_BUTTONS = By.xpath(
        "//button[contains(normalize-space(.), 'Выбрать билет') or " +
            "contains(normalize-space(.), 'Выбрать') or contains(normalize-space(.), 'Купить')]");

    private final TutuUiTestBase base;

    public FlightSearchResultsPage(TutuUiTestBase base) {
        this.base = base;
    }

    public boolean isLoaded() {
        return !base.findElements(SEARCH_ORIGIN).isEmpty()
            && !base.findElements(SEARCH_DESTINATION).isEmpty()
            && !base.findElements(SEARCH_DATE).isEmpty()
            && !base.findElements(AVIA_SECTION).isEmpty();
    }

    public String searchOriginValue() {
        return base.waitPresent(SEARCH_ORIGIN).getDomProperty("value");
    }

    public String searchDestinationValue() {
        return base.waitPresent(SEARCH_DESTINATION).getDomProperty("value");
    }

    public String departureDateValue() {
        return base.waitPresent(SEARCH_DATE).getDomProperty("value");
    }

    public boolean hasSelectableTickets() {
        return !base.findElements(SELECT_TICKET_BUTTONS).isEmpty();
    }

    public WebElement firstSelectTicketButton() {
        List<WebElement> buttons = base.findElements(SELECT_TICKET_BUTTONS);
        return buttons.isEmpty() ? null : buttons.get(0);
    }

    public void clickFirstSelectTicketButton() {
        WebElement button = firstSelectTicketButton();
        if (button != null) {
            base.scrollIntoView(button);
            button.click();
        }
    }
}
