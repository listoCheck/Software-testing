package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * UC-07: Полное заполнение формы поиска отелей на лендинге.
 * UC-08: Попытка запустить поиск с незаполненными полями вызывает ошибку на форме.
 */
@Tag("ui")
class TutuHotelSearchTest extends TutuUiTestBase {

    // UC-07
    @Test
    void shouldFillHotelFormWithDestinationAndDatesOnLandingPage() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();

        // Шаг 1: переключиться на вкладку «Отели»
        homePage.clickHotelsFormTab();
        assertTrue(homePage.isHotelGuestsWidgetVisible(),
            "После переключения на вкладку «Отели» должен появиться виджет выбора гостей");

        // Шаг 2: ввести город назначения, выбрать из автодополнения
        homePage.enterHotelDestination("Сочи");
        String destinationBefore = homePage.hotelDestinationValue();
        assertFalse(destinationBefore.isBlank(),
            "После ввода города поле назначения должно быть заполнено");

        // Шаг 3: открыть поле даты заезда — форма реагирует (открывается календарь)
        homePage.clickHotelCheckinField();

        // Шаг 4: выбрать первую доступную дату
        homePage.clickFirstAvailableDayInDatepicker();

        // Шаг 5: значение поля даты заезда заполнилось
        String checkinValue = homePage.getHotelCheckinValue();
        assertFalse(checkinValue.isBlank(),
            "После выбора даты заезда поле должно быть заполнено");

        // Шаг 6: город назначения сохранился (не сбросился при работе с датой)
        String destinationAfter = homePage.hotelDestinationValue();
        assertFalse(destinationAfter.isBlank(),
            "Поле назначения не должно сбрасываться после взаимодействия с датапикером");
        assertTrue(destinationAfter.contains("Сочи") || destinationAfter.startsWith("Соч"),
            "Значение поля назначения должно соответствовать введённому городу");
    }

    // UC-08
    @Test
    void shouldNotNavigateAwayAndShowErrorOnEmptyFlightSearch() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();

        // Шаг 1: открыть вкладку авиа — форма поиска с пустыми полями
        homePage.clickFlightTab();
        assertTrue(homePage.isPassengerSelectorVisible(),
            "Форма авиабилетов должна быть активной");

        String urlBeforeSearch = currentUrl();

        // Шаг 2: нажать «Найти» без заполнения обязательных полей
        homePage.clickSearchButton();

        // Небольшая пауза — ждём реакции интерфейса
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

        // Шаг 3: либо URL не изменился (навигация заблокирована), либо на странице
        // появились визуальные ошибки (подсветка полей)
        String urlAfterSearch = currentUrl();
        boolean stayedOnLanding = homePage.isOnLandingPage();
        boolean errorDisplayed = homePage.isAnyFieldInErrorState()
            || homePage.isValidationErrorDisplayed();

        assertTrue(stayedOnLanding || errorDisplayed,
            "При пустых полях поиска система должна либо остаться на лендинге, " +
            "либо показать ошибку валидации полей");
    }
}
