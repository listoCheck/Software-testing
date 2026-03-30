package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

@Tag("ui")
class TutuMainServicesUiTest extends TutuUiTestBase {

    @Test
    void shouldDisplayMainTravelServicesOnHomePage() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();

        for (List<String> service : homePage.mainServices()) {
            WebElement serviceLink = homePage.serviceLink(service.get(0), service.get(1));
            assertTrue(serviceLink.getAttribute("href").contains(service.get(1)),
                "Unexpected href for service: " + service.get(0));
        }

        assertTrue(homePage.ideasHeading().getText().contains("Идеи для поездок"));
    }
}
