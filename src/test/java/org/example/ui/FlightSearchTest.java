package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.example.ui.page.FlightSearchResultsPage;
import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

@Tag("ui")
class FlightSearchTest extends TutuUiTestBase {

    @Test
    void shouldPreserveFlightSearchStateFromHomePageToResults() {
        TutuHomePage homePage = new TutuHomePage(this);
        FlightSearchResultsPage resultsPage = new FlightSearchResultsPage(this);

        String fromCity = "Москва";
        String toCity = "Санкт-Петербург";
        LocalDate departureDate = LocalDate.now().plusDays(7);
        int adults = 2;
        int children = 1;
        int infants = 0;

        homePage.open();
        homePage.clickFlightTab();
        homePage.enterFlightOrigin(fromCity);
        homePage.enterFlightDestination(toCity);
        homePage.selectDepartureDate(departureDate);
        String passengersBeforeChange = homePage.getPassengerDisplayText();
        homePage.openPassengerSelector();
        homePage.setPassengersCount(adults, children, infants);
        homePage.applyPassengerSelection();

        String passengerSummary = homePage.getPassengerDisplayText();
        WebElement searchButton = homePage.searchButton();

        assertEquals(fromCity, homePage.getFlightOriginValue());
        assertEquals(toCity, homePage.getFlightDestinationValue());
        assertEquals(formatDateForDisplay(departureDate), homePage.getDepartureDateValue());
        assertFalse(passengerSummary.isBlank());
        assertNotEquals(passengersBeforeChange, passengerSummary);
        assertTrue(passengerSummary.matches(".*[23].*"),
            "В отображении пассажиров должно быть видно выбранное количество");
        assertTrue(searchButton.isDisplayed());
        assertTrue(searchButton.isEnabled());

        homePage.clickSearchButton();
        waitUntilUrlContains("/avia/");

        assertTrue(resultsPage.isLoaded(), "После поиска должна открыться страница выдачи авиабилетов");
        assertEquals(fromCity, resultsPage.searchOriginValue());
        assertEquals(toCity, resultsPage.searchDestinationValue());
        assertEquals(formatDateForDisplay(departureDate), resultsPage.departureDateValue());

        WebElement firstSelectTicketButton = resultsPage.firstSelectTicketButton();
        assertNotNull(firstSelectTicketButton, "На выдаче должна быть кнопка выбора билета");
        assertTrue(resultsPage.hasSelectableTickets());
        assertTrue(firstSelectTicketButton.isDisplayed());
        assertTrue(firstSelectTicketButton.isEnabled());

        String resultsUrlBeforeSelection = driver.getCurrentUrl();
        int windowsBeforeSelection = driver.getWindowHandles().size();

        resultsPage.clickFirstSelectTicketButton();

        new WebDriverWait(driver, java.time.Duration.ofSeconds(20)).until(d ->
            d.getWindowHandles().size() > windowsBeforeSelection
                || !d.getCurrentUrl().equals(resultsUrlBeforeSelection));

        assertTrue(
            driver.getWindowHandles().size() > windowsBeforeSelection
                || !driver.getCurrentUrl().equals(resultsUrlBeforeSelection),
            "После выбора билета должна начаться навигация к следующему шагу");
    }

    private String formatDateForDisplay(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.forLanguageTag("ru")));
    }
}
