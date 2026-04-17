package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

@Tag("ui")
class TutuNavigationAndContentUiTest extends TutuUiTestBase {

    @Test
    void shouldExposeConsistentNavigationAndDiscoveryContent() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();

        Map<String, String> headerLinks = new LinkedHashMap<>();
        for (WebElement link : homePage.headerLinks()) {
            headerLinks.put(link.getText().trim(), link.getAttribute("href"));
        }

        assertEquals(homePage.expectedHeaderLinks().keySet(), headerLinks.keySet());
        for (Map.Entry<String, String> expectedLink : homePage.expectedHeaderLinks().entrySet()) {
            assertTrue(headerLinks.get(expectedLink.getKey()).contains(expectedLink.getValue()),
                "Unexpected href for header link: " + expectedLink.getKey());
        }

        for (List<String> service : homePage.mainServices()) {
            WebElement serviceLink = homePage.serviceLink(service.get(0), service.get(1));
            assertTrue(serviceLink.getAttribute("href").contains(service.get(1)),
                "Unexpected href for service: " + service.get(0));
        }

        assertEquals("Войти", homePage.loginButton().getText().trim());
        assertEquals("Идеи для поездок", homePage.ideasHeading().getText().trim());
        assertEquals(5, homePage.ideasCards().size());
        assertTrue(homePage.ideasAllLink().getAttribute("href").contains("/geo/"));

        assertEquals("Вопросы и ответы", homePage.faqHeading().getText().trim());
        assertEquals(4, homePage.faqCards().size());
        assertTrue(homePage.faqAllLink().getAttribute("href").contains("/2read/"));

        assertTrue(homePage.advantageCards().size() >= 4);
    }
}
