package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

@Tag("ui")
class TutuLeadCaptureUiTest extends TutuUiTestBase {

    @Test
    void shouldSupportHotelSearchInputAndSubscriptionValidation() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();

        homePage.enterHotelDestination("Казань");
        wait.until(driver -> "Казань".equals(homePage.hotelDestinationValue()));
        assertEquals("Казань", homePage.hotelDestinationValue());

        homePage.enterEmail("invalid-email");
        WebElement emailField = homePage.emailField();
        assertEquals("invalid-email", emailField.getDomProperty("value"));
        assertEquals("Электронная почта", emailField.getAttribute("aria-label"));
        assertTrue(homePage.subscribeButton().isDisplayed());
        assertTrue(homePage.subscribeButton().isEnabled());
        assertTrue(homePage.consentCheckbox().getAttribute("aria-label")
            .contains("Даю согласие на обработку персональных данных"));
        assertFalse(homePage.consentCheckbox().isSelected());
        homePage.acceptSubscriptionConsent();
        assertTrue(homePage.consentCheckbox().isSelected());

        assertTrue(homePage.subscribeButton().getAttribute("aria-label").contains("Подписаться"));
        assertTrue(homePage.privacyLink().getText().contains("Политика обработки персональных данных"));
        assertTrue(homePage.privacyLink().getAttribute("href").endsWith("#"));
        assertNotNull(homePage.privacyLink().getAttribute("data-url"));
        assertTrue(homePage.privacyLink().getAttribute("data-url").contains("/2read/legal_information/legal_personal/"));
    }
}
