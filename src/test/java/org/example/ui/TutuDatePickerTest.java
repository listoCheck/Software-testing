package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * UC-04: Навигация вперёд по месяцам в датапикере — заголовок меняется, выбранная дата сохраняется.
 */
@Tag("ui")
class TutuDatePickerTest extends TutuUiTestBase {

    @Test
    void shouldNavigateToNextMonthAndSelectDateInDatepicker() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();

        // Шаг 1: кликнуть на поле даты — должен открыться календарь
        // Датапикер доступен на форме по умолчанию (поезда/электрички)
        homePage.clickDepartureDate();
        assertTrue(homePage.isDatepickerVisible(),
            "После клика по полю даты должен открыться датапикер");

        // Шаг 2: считать текущий заголовок месяца (например «Май 2026»)
        String initialMonthTitle = homePage.getDatepickerMonthTitle();
        assertFalse(initialMonthTitle.isBlank(),
            "Заголовок текущего месяца в датапикере должен быть непустым");

        // Шаг 3: нажать кнопку «следующий месяц» — заголовок меняется
        homePage.clickDatepickerNextMonth();

        String nextMonthTitle = homePage.getDatepickerMonthTitle();
        assertFalse(nextMonthTitle.isBlank(),
            "Заголовок месяца после навигации вперёд не должен быть пустым");
        assertNotEquals(initialMonthTitle, nextMonthTitle,
            "Заголовок месяца должен измениться после нажатия кнопки 'вперёд'");

        // Шаг 4: выбрать первую доступную дату следующего месяца
        homePage.clickFirstAvailableDayInDatepicker();

        // Шаг 5: поле даты заполнено
        String dateValue = homePage.getDepartureDateValue();
        assertFalse(dateValue.isBlank(),
            "После выбора даты в датапикере поле отправления должно быть заполнено");
    }
}
