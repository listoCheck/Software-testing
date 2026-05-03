package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * UC-02: Автодополнение при вводе города.
 * UC-03: Кнопка «⇄» меняет местами города отправления и назначения.
 * UC-05: Счётчик пассажиров обновляется при нажатии «+».
 * UC-06: Переключение «В одну сторону» / «Туда-обратно» показывает/скрывает поле обратной даты.
 */
@Tag("ui")
class TutuFlightFormLandingTest extends TutuUiTestBase {

    // UC-02
    @Test
    void shouldDisplayAutocompleteSuggestionsAndFillFieldOnSelection() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();
        homePage.clickFlightTab();

        // Шаг 1: начать вводить город — появляется dropdown с подсказками
        By originInput = By.xpath(
            "(//input[@type='text' and (contains(@placeholder,'Откуда') or " +
            "contains(@aria-label,'Откуда') or contains(@name,'from'))])[1]");
        WebElement field = waitPresent(originInput);
        scrollIntoView(field);
        field.click();
        field.sendKeys("Мос");

        // Шаг 2: дождаться появления выпадающего списка
        By dropdown = By.xpath(
            "(//*[@role='listbox' or @role='option' or " +
            "contains(@class,'suggest') or contains(@class,'autocomplete') or " +
            "contains(@class,'dropdown')])[1]");
        WebElement suggestionList = waitVisible(dropdown);
        assertTrue(suggestionList.isDisplayed(),
            "После ввода 'Мос' должен появиться выпадающий список с подсказками");

        // Шаг 3: список непустой
        List<WebElement> suggestions = findElements(By.xpath(
            "(//*[@role='listbox'] | //*[@role='option'] | " +
            "//*[contains(@class,'suggest')])//*[self::li or self::div or self::button]"));
        assertFalse(suggestions.isEmpty(),
            "Список подсказок должен содержать хотя бы одну позицию");

        // Шаг 4: выбрать первую подсказку — поле заполняется полным названием
        WebElement firstSuggestion = suggestions.get(0);
        String suggestionText = firstSuggestion.getText();
        scrollIntoView(firstSuggestion);
        firstSuggestion.click();

        String filledValue = homePage.getFlightOriginValue();
        assertFalse(filledValue.isBlank(),
            "После выбора подсказки поле 'Откуда' не должно быть пустым");
        assertFalse(filledValue.equals("Мос"),
            "Значение поля должно быть развёрнутым названием, а не введённым фрагментом");
    }

    // UC-03
    @Test
    void shouldSwapOriginAndDestinationCitiesOnButtonClick() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();
        homePage.clickFlightTab();

        // Шаг 1: ввести города «Откуда» и «Куда»
        homePage.enterFlightOrigin("Москва");
        String originValue = homePage.getFlightOriginValue();

        homePage.enterFlightDestination("Санкт-Петербург");
        String destinationValue = homePage.getFlightDestinationValue();

        assertFalse(originValue.isBlank(), "Поле 'Откуда' должно быть заполнено");
        assertFalse(destinationValue.isBlank(), "Поле 'Куда' должно быть заполнено");
        assertNotEquals(originValue, destinationValue,
            "Значения полей 'Откуда' и 'Куда' должны отличаться до нажатия кнопки обмена");

        // Шаг 2: нажать кнопку обмена «⇄»
        homePage.clickSwapDirectionButton();

        // Шаг 3: убедиться, что значения поменялись местами
        String newOrigin = homePage.getFlightOriginValue();
        String newDestination = homePage.getFlightDestinationValue();

        assertFalse(newOrigin.isBlank(), "После обмена поле 'Откуда' не должно быть пустым");
        assertFalse(newDestination.isBlank(), "После обмена поле 'Куда' не должно быть пустым");
        assertNotEquals(originValue, newOrigin,
            "Значение 'Откуда' должно измениться после нажатия кнопки обмена");
        assertNotEquals(destinationValue, newDestination,
            "Значение 'Куда' должно измениться после нажатия кнопки обмена");
    }

    // UC-05
    @Test
    void shouldUpdatePassengerDisplayTextOnCountIncrement() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();
        homePage.clickFlightTab();

        // Шаг 1: зафиксировать начальный текст счётчика (обычно «1 пассажир»)
        String initialText = homePage.getPassengerButtonText();
        assertFalse(initialText.isBlank(),
            "Кнопка пассажиров должна отображать текущее количество");

        // Шаг 2: открыть панель пассажиров и нажать «+» для взрослых дважды
        homePage.openPassengerSelector();
        homePage.clickIncrementAdults();
        homePage.clickIncrementAdults();
        homePage.applyPassengerSelection();

        // Шаг 3: текст счётчика изменился и содержит новое количество
        String updatedText = homePage.getPassengerButtonText();
        assertFalse(updatedText.isBlank(),
            "Кнопка пассажиров не должна быть пустой после изменения");
        assertNotEquals(initialText, updatedText,
            "Текст счётчика пассажиров должен обновиться после увеличения значения");
        assertTrue(updatedText.contains("3") || updatedText.matches(".*[3-9].*"),
            "Счётчик должен отражать добавленных пассажиров (ожидается 3 или больше)");
    }

    // UC-06
    @Test
    void shouldToggleReturnDateFieldVisibilityOnRoundTripSelection() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();
        homePage.clickFlightTab();

        boolean initiallyVisible = homePage.isReturnDateFieldVisible();

        if (!initiallyVisible) {
            // Стандартный сценарий: форма открыта в режиме «В одну сторону»
            // Шаг 1: переключиться на «Туда-обратно» — должно появиться поле обратной даты
            homePage.selectRoundTrip();
            assertTrue(homePage.isReturnDateFieldVisible(),
                "После выбора 'Туда-обратно' должно появиться поле даты обратного билета");

            // Шаг 2: вернуться в «В одну сторону» — поле обратной даты исчезает
            homePage.selectOneWay();
            assertFalse(homePage.isReturnDateFieldVisible(),
                "После возврата в 'В одну сторону' поле даты обратного билета должно исчезнуть");
        } else {
            // Форма по умолчанию в режиме «Туда-обратно»
            // Шаг 1: переключиться на «В одну сторону»
            homePage.selectOneWay();
            assertFalse(homePage.isReturnDateFieldVisible(),
                "В режиме 'В одну сторону' поле даты обратного билета не должно быть видно");

            // Шаг 2: вернуться на «Туда-обратно»
            homePage.selectRoundTrip();
            assertTrue(homePage.isReturnDateFieldVisible(),
                "После возврата в 'Туда-обратно' поле даты обратного билета должно появиться");
        }
    }
}
