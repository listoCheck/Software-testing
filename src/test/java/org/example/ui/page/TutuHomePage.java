package org.example.ui.page;

import java.util.List;

import org.example.ui.TutuUiTestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriverException;

public class TutuHomePage {
    private static final String CONSENT_LABEL = "Даю согласие на обработку персональных данных и рекламные рассылки";
    private static final By IDEAS_HEADING = By.xpath(
        "//*[self::h1 or self::h2 or self::div or self::span][normalize-space()='Идеи для поездок']");
    private static final By HELP_LINK = By.xpath("(//a[contains(@href, '/2read/')])[1]");
    private static final By DESTINATION_FIELD = By.xpath(
        "(//input[@data-ti='input' and @type='text' and not(@name='userEmail')])[1]");
    private static final By EMAIL_FIELD = By.xpath("//input[contains(@aria-label, 'Электронная почта') and @name='userEmail']");
    private static final By SUBSCRIBE_BUTTON = By.xpath(
        "(//button[normalize-space()='Подписаться' or contains(@aria-label, 'Подписаться')])[last()]");
    private static final By CONSENT_CHECKBOX = By.xpath("//input[contains(@aria-label, '" + CONSENT_LABEL + "')]");

    private final TutuUiTestBase base;

    public TutuHomePage(TutuUiTestBase base) {
        this.base = base;
    }

    public void open() {
        base.openHomePage();
    }

    public WebElement serviceLink(String name, String hrefFragment) {
        return base.waitPresent(By.xpath(
            "//a[contains(@href, '" + hrefFragment + "') and .//*[normalize-space()='" + name + "']]"));
    }

    public List<List<String>> mainServices() {
        return List.of(
            List.of("Авиабилеты", "avia.tutu.ru"),
            List.of("Ж/д билеты", "/poezda/"),
            List.of("Автобусы", "bus.tutu.ru"),
            List.of("Отели", "hotel.tutu.ru"),
            List.of("Электрички", "/prigorod/")
        );
    }

    public WebElement ideasHeading() {
        return base.waitPresent(IDEAS_HEADING);
    }

    public void openHelpSection() {
        WebElement link = base.waitPresent(HELP_LINK);
        base.scrollIntoView(link);
        String href = link.getAttribute("href");
        try {
            link.click();
        } catch (WebDriverException ignored) {
            if (href != null && !href.isBlank()) {
                base.openUrl(href);
            } else {
                throw ignored;
            }
        }
    }

    public String helpSectionHref() {
        return base.waitPresent(HELP_LINK).getAttribute("href");
    }

    public void enterHotelDestination(String destination) {
        WebElement field = base.waitPresent(DESTINATION_FIELD);
        base.scrollIntoView(field);
        field.click();
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        field.sendKeys(destination);
    }

    public String hotelDestinationValue() {
        WebElement field = base.waitPresent(DESTINATION_FIELD);
        return field.getDomProperty("value");
    }

    public WebElement emailField() {
        return base.waitPresent(EMAIL_FIELD);
    }

    public void enterEmail(String value) {
        WebElement field = emailField();
        base.scrollIntoView(field);
        field.click();
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        field.sendKeys(Keys.DELETE);
        field.sendKeys(value);
    }

    public void enterInvalidEmail(String value) {
        enterEmail(value);
    }

    public void submitSubscription() {
        base.scrollIntoView(subscribeButton());
        subscribeButton().click();
    }

    public WebElement subscribeButton() {
        return base.waitPresent(SUBSCRIBE_BUTTON);
    }

    public WebElement consentCheckbox() {
        return base.waitPresent(CONSENT_CHECKBOX);
    }

    public void acceptSubscriptionConsent() {
        WebElement checkbox = consentCheckbox();
        base.scrollIntoView(checkbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void clearSubscriptionConsent() {
        WebElement checkbox = consentCheckbox();
        base.scrollIntoView(checkbox);
        if (checkbox.isSelected()) {
            checkbox.click();
        }
    }
}
