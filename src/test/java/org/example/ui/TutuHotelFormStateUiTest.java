package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("ui")
class TutuHotelFormStateUiTest extends TutuUiTestBase {

    @Test
    void shouldKeepHotelDestinationAfterInteractingWithAnotherForm() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();
        homePage.enterHotelDestination("Казань");
        homePage.enterEmail("qa@example.com");

        wait.until(driver -> "Казань".equals(homePage.hotelDestinationValue()));

        assertEquals("Казань", homePage.hotelDestinationValue());
        assertFalse(homePage.emailField().getDomProperty("value").isBlank());
    }
}
