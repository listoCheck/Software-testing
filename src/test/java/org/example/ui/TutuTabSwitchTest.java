package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.ui.page.TutuHomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * UC-01: Переключение вкладок сервисов меняет тип формы поиска.
 */
@Tag("ui")
class TutuTabSwitchTest extends TutuUiTestBase {

    @Test
    void shouldChangeSearchFormContentWhenSwitchingBetweenServiceTabs() {
        TutuHomePage homePage = new TutuHomePage(this);
        homePage.open();

        // Шаг 1: активировать вкладку «Авиабилеты» — должен появиться счётчик пассажиров
        homePage.clickFlightTab();
        boolean passengerSelectorOnFlight = homePage.isPassengerSelectorVisible();
        assertTrue(passengerSelectorOnFlight,
            "На вкладке авиа должен быть виден блок выбора пассажиров");

        // Шаг 2: переключиться на «Отели» — форма структурно меняется,
        // счётчик пассажиров пропадает, появляется виджет гостей / комнат
        homePage.clickHotelsFormTab();

        // Короткое ожидание перерисовки формы
        wait.until(d -> !homePage.isFlightTabActive());

        boolean passengerSelectorOnHotel = homePage.isPassengerSelectorVisible();
        assertFalse(passengerSelectorOnHotel,
            "На вкладке отелей счётчик пассажиров авиа не должен быть виден");

        boolean hotelGuestsVisible = homePage.isHotelGuestsWidgetVisible();
        assertTrue(hotelGuestsVisible,
            "На вкладке отелей должен появиться виджет выбора гостей/комнат");

        // Шаг 3: вернуться на «Авиабилеты» — счётчик пассажиров снова активен,
        // виджет гостей отелей исчезает
        homePage.clickFlightTab();
        assertTrue(homePage.isPassengerSelectorVisible(),
            "После возврата на авиа вкладку счётчик пассажиров должен снова появиться");

        boolean hotelGuestsAfterReturn = homePage.isHotelGuestsWidgetVisible();
        assertNotEquals(passengerSelectorOnFlight, passengerSelectorOnHotel,
            "Видимость авиа-счётчика должна различаться между вкладками");
    }
}
