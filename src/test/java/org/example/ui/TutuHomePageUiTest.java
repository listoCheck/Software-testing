package org.example.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

@Tag("ui")
class TutuHomePageUiTest extends TutuUiTestBase {

    @Test
    void shouldDisplayMainTravelServicesOnHomePage() {
        openHomePage();

        List<String> expectedServices = List.of(
            "Авиабилеты",
            "Ж/д билеты",
            "Автобусы",
            "Отели",
            "Электрички"
        );

        for (String service : expectedServices) {
            WebElement serviceElement = waitVisible(
                By.xpath("//*[self::a or self::button or self::div or self::span][normalize-space()='" + service + "']"));
            assertTrue(serviceElement.isDisplayed(), "Service is not visible: " + service);
        }

        WebElement ideasHeading = waitVisible(
            By.xpath("//*[self::h1 or self::h2 or self::div or self::span][normalize-space()='Идеи для поездок']"));
        assertTrue(ideasHeading.isDisplayed());
    }

    @Test
    void shouldOpenHelpSectionFromHeader() {
        openHomePage();

        clickWhenReady(By.xpath("(//a[normalize-space()='Справочная'])[1]"));

        wait.until(driver -> driver.getCurrentUrl().contains("/2read/"));
        WebElement heading = waitVisible(
            By.xpath("//*[self::h1 or self::h2][normalize-space()='Справочная']"));
        WebElement trainsCategory = waitVisible(
            By.xpath("//*[self::a or self::h2 or self::h3][normalize-space()='Поезда']"));

        assertTrue(driver.getCurrentUrl().contains("/2read/"));
        assertEquals("Справочная", heading.getText().trim());
        assertTrue(trainsCategory.isDisplayed());
    }

    @Test
    void shouldAllowEnteringHotelDestination() {
        openHomePage();

        clickWhenReady(By.xpath("(//*[self::a or self::button or self::div or self::span][normalize-space()='Отели'])[1]"));

        WebElement destinationField = waitVisible(
            By.xpath("//input[contains(@placeholder, 'Город') or contains(@aria-label, 'Город') or contains(@name, 'destination')]"));
        scrollIntoView(destinationField);
        destinationField.click();
        destinationField.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        destinationField.sendKeys("Казань");

        wait.until(driver -> "Казань".equals(destinationField.getDomProperty("value")));
        assertEquals("Казань", destinationField.getDomProperty("value"));
    }

    @Test
    void shouldRejectInvalidEmailInSubscriptionForm() {
        openHomePage();

        WebElement emailField = waitVisible(By.xpath("//input[@type='email']"));
        scrollIntoView(emailField);
        emailField.click();
        emailField.sendKeys("invalid-email");

        clickWhenReady(By.xpath("//button[normalize-space()='Подписаться']"));

        assertFalse(isHtml5Valid(emailField));
        assertFalse(emailField.getAttribute("validationMessage").isBlank());
    }
}
