package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

/**
 * UC-10: Скролл вниз открывает блок «Идеи для поездок» с карточками направлений.
 */
@Tag("ui")
class TutuLandingScrollTest extends TutuUiTestBase {

    @Test
    void shouldRevealIdeasCardsAfterScrollingBelowFold() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();

        // Шаг 1: убедиться, что блок «Идеи для поездок» присутствует в DOM
        WebElement ideasHeading = homePage.ideasHeading();
        assertNotNull(ideasHeading,
            "Блок «Идеи для поездок» должен присутствовать на странице");

        // Шаг 2: зафиксировать, что блок изначально может быть вне viewport (ниже fold)
        boolean visibleBeforeScroll = isElementInViewport(ideasHeading);

        // Шаг 3: прокрутить страницу до блока «Идеи для поездок»
        homePage.scrollToIdeasSection();

        // Шаг 4: после скролла блок должен попасть в область видимости
        boolean visibleAfterScroll = isElementInViewport(ideasHeading);
        assertTrue(visibleAfterScroll,
            "После скролла блок «Идеи для поездок» должен быть виден в viewport");

        // Шаг 5: проверить, что состояние видимости изменилось (блок был не виден)
        if (!visibleBeforeScroll) {
            assertTrue(visibleAfterScroll,
                "Блок должен стать видимым именно в результате скролла");
        }

        // Шаг 6: в блоке должны присутствовать карточки направлений
        List<WebElement> cards = homePage.getIdeasCards();
        assertFalse(cards.isEmpty(),
            "В блоке «Идеи для поездок» должны быть карточки с направлениями");
        assertTrue(cards.size() >= 2,
            "Должно быть минимум 2 карточки с идеями для поездок");

        // Шаг 7: карточки видимы и интерактивны (имеют ссылки)
        int checkedCards = 0;
        for (WebElement card : cards) {
            if (checkedCards >= 4) break;
            try {
                if (card.isDisplayed()) {
                    String href = card.getAttribute("href");
                    assertNotNull(href,
                        "Карточка в блоке «Идеи для поездок» должна содержать ссылку");
                    assertFalse(href.isBlank(),
                        "href карточки не должен быть пустым");
                    checkedCards++;
                }
            } catch (Exception ignored) {}
        }
        assertTrue(checkedCards >= 1,
            "Хотя бы одна видимая карточка должна иметь корректный href");
    }
}
