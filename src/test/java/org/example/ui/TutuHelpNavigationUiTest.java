package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.ui.page.TutuHelpPage;
import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("ui")
class TutuHelpNavigationUiTest extends TutuUiTestBase {

    @Test
    void shouldOpenHelpSectionFromHeader() {
        TutuHomePage homePage = new TutuHomePage(this);
        TutuHelpPage helpPage = new TutuHelpPage(this);

        homePage.open();
        String href = homePage.helpSectionHref();

        assertTrue(href.contains("/2read/"));
        assertTrue(helpPage.helpHref().contains("/2read/"));
    }

    @Test
    void shouldNavigateToHelpSectionAfterClick() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();

        homePage.openHelpSection();
        waitUntilUrlContains("/2read/");

        assertTrue(driver.getCurrentUrl().contains("/2read/"));
    }
}
