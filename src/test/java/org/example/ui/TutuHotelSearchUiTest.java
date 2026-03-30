package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("ui")
class TutuHotelSearchUiTest extends TutuUiTestBase {

    @Test
    void shouldAllowEnteringHotelDestination() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();
        homePage.enterHotelDestination("Казань");

        wait.until(driver -> "Казань".equals(homePage.hotelDestinationValue()));
        assertEquals("Казань", homePage.hotelDestinationValue());
    }
}
