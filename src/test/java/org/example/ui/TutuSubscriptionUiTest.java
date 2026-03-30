package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

@Tag("ui")
class TutuSubscriptionUiTest extends TutuUiTestBase {

    @Test
    void shouldAllowUsingSubscriptionControls() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();
        homePage.enterInvalidEmail("invalid-email");
        homePage.acceptSubscriptionConsent();

        WebElement emailField = homePage.emailField();
        assertEquals("invalid-email", emailField.getDomProperty("value"));
        assertTrue(homePage.subscribeButton().getAttribute("aria-label").contains("Подписаться"));
        assertTrue(homePage.consentCheckbox().isSelected());
        assertFalse(emailField.getDomProperty("value").isBlank());
    }
}
