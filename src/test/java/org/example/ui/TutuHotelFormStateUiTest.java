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
        String destinationValueBeforeEmailInput = homePage.hotelDestinationValue();
        homePage.enterEmail("artem@example.com");
        String destinationValueAfterEmailInput = homePage.hotelDestinationValue();

        assertFalse(destinationValueBeforeEmailInput.isBlank());
        assertEquals(destinationValueBeforeEmailInput, destinationValueAfterEmailInput);
        assertFalse(homePage.emailField().getDomProperty("value").isBlank());
    }
}
