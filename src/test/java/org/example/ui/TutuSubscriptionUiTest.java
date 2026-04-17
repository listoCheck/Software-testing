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
    void shouldRejectInvalidEmailByHtml5Validation() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();
        homePage.enterInvalidEmail("invalid-email");
        homePage.clearSubscriptionConsent();
        homePage.submitSubscription();

        WebElement emailField = homePage.emailField();
        assertEquals("invalid-email", emailField.getDomProperty("value"));
        assertTrue(homePage.subscribeButton().isDisplayed());
        assertFalse(homePage.consentCheckbox().isSelected());
    }

    @Test
    void shouldAcceptInValidEmailWhenConsentChecked() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();
        homePage.enterEmail("adasdsaddsagdsgdsagggdaad@asdsadasdsdfetvretv.sfddsdsbdsds");
        homePage.acceptSubscriptionConsent();
        homePage.submitSubscription();

        WebElement emailField = homePage.emailField();
        assertEquals("adasdsaddsagdsgdsagggdaad@asdsadasdsdfetvretv.sfddsdsbdsds", emailField.getDomProperty("value"));
        assertTrue(isHtml5Valid(emailField));
        assertTrue(homePage.subscribeButton().getAttribute("aria-label").contains("Подписаться"));
        assertTrue(homePage.consentCheckbox().isSelected());
    }

    @Test
    void shouldAcceptValidEmailWhenConsentChecked() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();
        homePage.enterEmail("amuz@amuz.com");
        homePage.acceptSubscriptionConsent();
        homePage.submitSubscription();

        WebElement emailField = homePage.emailField();
        assertEquals("amuz@amuz.com", emailField.getDomProperty("value"));
        assertTrue(isHtml5Valid(emailField));
        assertTrue(homePage.subscribeButton().getAttribute("aria-label").contains("Подписаться"));
        assertTrue(homePage.consentCheckbox().isSelected());
    }
}
