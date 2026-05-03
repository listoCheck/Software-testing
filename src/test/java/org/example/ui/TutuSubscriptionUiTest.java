package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

/**
 * UC-09: Форма подписки — переход от невалидного состояния к готовности отправки.
 * Многошаговый сценарий с визуальной сменой состояния поля email и чекбокса согласия.
 */
@Tag("ui")
class TutuSubscriptionUiTest extends TutuUiTestBase {

    @Test
    void shouldTransitionSubscriptionFormFromInvalidToReadyState() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();

        // --- Фаза 1: Ввод заведомо невалидного email (без символа @) ---

        // Шаг 1: убрать согласие (если уже отмечено)
        homePage.clearSubscriptionConsent();
        assertFalse(homePage.consentCheckbox().isSelected(),
            "Чекбокс согласия должен быть снят для проверки невалидного состояния");

        // Шаг 2: ввести email без символа «@»
        homePage.enterInvalidEmail("notanemail");
        WebElement emailField = homePage.emailField();
        assertFalse(isHtml5Valid(emailField),
            "Email без '@' должен не проходить HTML5-валидацию");

        // Шаг 3: попытаться подписаться — форма остаётся в ошибочном состоянии
        homePage.submitSubscription();
        assertFalse(homePage.consentCheckbox().isSelected(),
            "Чекбокс должен оставаться снятым — согласие не было дано");
        assertFalse(isHtml5Valid(emailField),
            "После безуспешной отправки email всё ещё должен быть невалидным");

        // --- Фаза 2: Исправление email и выдача согласия ---

        // Шаг 4: ввести корректный email
        homePage.enterEmail("test.user@example.com");
        WebElement fixedEmailField = homePage.emailField();
        assertTrue(isHtml5Valid(fixedEmailField),
            "Корректный email должен проходить HTML5-валидацию");

        // Шаг 5: отметить чекбокс согласия — форма полностью готова
        homePage.acceptSubscriptionConsent();
        assertTrue(homePage.consentCheckbox().isSelected(),
            "После клика чекбокс согласия должен быть отмечен");

        // --- Фаза 3: Проверка итогового состояния формы ---

        // Шаг 6: финальные ассерты — все условия для отправки выполнены
        assertTrue(isHtml5Valid(homePage.emailField()),
            "Email должен быть валидным перед отправкой");
        assertTrue(homePage.consentCheckbox().isSelected(),
            "Согласие должно быть отмечено перед отправкой");
        assertTrue(homePage.subscribeButton().isDisplayed(),
            "Кнопка «Подписаться» должна быть видна");
        assertTrue(homePage.subscribeButton().isEnabled(),
            "Кнопка «Подписаться» должна быть активна");
    }
}
