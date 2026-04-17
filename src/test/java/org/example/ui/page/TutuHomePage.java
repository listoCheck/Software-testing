package org.example.ui.page;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.example.ui.TutuUiTestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class TutuHomePage {
    private static final String CONSENT_LABEL = "Даю согласие на обработку персональных данных и рекламные рассылки";
    private static final By HEADER_LINKS = By.xpath(
        "//header//a[normalize-space()='Это выгодно!' or normalize-space()='Выходные' "
            + "or normalize-space()='Маршруты' or normalize-space()='Афиша' "
            + "or normalize-space()='Справочная' or normalize-space()='Путеводитель']");
    private static final By IDEAS_HEADING = By.xpath(
        "//*[self::h1 or self::h2 or self::div or self::span][normalize-space()='Идеи для поездок']");
    private static final By HELP_LINK = By.xpath("(//a[normalize-space()='Справочная' and contains(@href, '/2read/')])[1]");
    private static final By DESTINATION_FIELD = By.xpath(
        "(//input[@data-ti='input' and @type='text' and not(@name='userEmail')])[1]");
    private static final By EMAIL_FIELD = By.xpath("//input[contains(@aria-label, 'Электронная почта') and @name='userEmail']");
    private static final By SUBSCRIBE_BUTTON = By.xpath(
        "(//button[normalize-space()='Подписаться' or contains(@aria-label, 'Подписаться')])[last()]");
    private static final By CONSENT_CHECKBOX = By.xpath("//input[contains(@aria-label, '" + CONSENT_LABEL + "')]");
    private static final By LOGIN_BUTTON = By.xpath("//button[@data-ti='login-button']");
    private static final By IDEAS_CARDS = By.xpath(
        "//*[normalize-space()='Идеи для поездок']/ancestor::*[self::section or self::div][1]//a[.//*[self::div or self::span][normalize-space()='Восточные Гавайи' or normalize-space()='Домбай' or normalize-space()='Филиппины: сменить сезон' or normalize-space()='Ледяной Байкал' or normalize-space()='За сиянием — на Север']]");
    private static final By IDEAS_ALL_LINK = By.xpath(
        "//a[contains(@href, 'tutu.ru/geo') and .//*[normalize-space()='Посмотреть все']]");
    private static final By FAQ_HEADING = By.xpath(
        "//*[self::h1 or self::h2 or self::div or self::span][normalize-space()='Вопросы и ответы']");
    private static final By FAQ_CARDS = By.xpath(
        "//*[normalize-space()='Вопросы и ответы']/ancestor::*[self::section or self::div][1]//a[.//*[self::div or self::span][normalize-space()='Как вернуть билет на самолёт' or normalize-space()='Всё о возврате билета на поезд' or normalize-space()='Что можно изменить в билете на самолёт' or normalize-space()='Как исправить билет на поезд']]");
    private static final By FAQ_ALL_LINK = By.xpath(
        "(//a[contains(@href, '/2read/') and .//*[normalize-space()='Посмотреть все']])[last()]");
    private static final By ADVANTAGE_CARDS = By.xpath(
        "//*[normalize-space()='Фишки Туту']/ancestor::*[self::section or self::div][1]//*[self::div or self::span][normalize-space()='Оплата позже' or normalize-space()='Кешбэк 3% баллами' or normalize-space()='Поддержка 24/7' or normalize-space()='Всё и сразу']");
    private static final By PRIVACY_LINK = By.xpath(
        "//a[contains(normalize-space(.), 'Политика обработки персональных данных')]");

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
            List.of("Электрички", "/prigorod/"),
            List.of("Туры", "tours.tutu.ru"),
            List.of("Аренда авто", "avto.tutu.ru"),
            List.of("Аэроэкспрессы", "aeroexpress.tutu.ru")
        );
    }

    public Map<String, String> expectedHeaderLinks() {
        Map<String, String> links = new LinkedHashMap<>();
        links.put("Это выгодно!", "/juicy-offers/");
        links.put("Выходные", "/weekend/");
        links.put("Маршруты", "provereno.tutu.ru");
        links.put("Афиша", "/afisha/");
        links.put("Справочная", "/2read/");
        links.put("Путеводитель", "/geo/");
        return links;
    }

    public List<WebElement> headerLinks() {
        return base.waitVisibleMatching(HEADER_LINKS, links -> links.size() == expectedHeaderLinks().size());
    }

    public WebElement ideasHeading() {
        return base.waitPresent(IDEAS_HEADING);
    }

    public List<WebElement> ideasCards() {
        return base.waitVisibleAll(IDEAS_CARDS);
    }

    public WebElement ideasAllLink() {
        return base.waitPresent(IDEAS_ALL_LINK);
    }

    public WebElement faqHeading() {
        return base.waitPresent(FAQ_HEADING);
    }

    public List<WebElement> faqCards() {
        return base.waitVisibleAll(FAQ_CARDS);
    }

    public WebElement faqAllLink() {
        return base.waitPresent(FAQ_ALL_LINK);
    }

    public List<WebElement> advantageCards() {
        return base.waitVisibleAll(ADVANTAGE_CARDS);
    }

    public void openHelpSection() {
        base.clickWhenReady(HELP_LINK);
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
        field.sendKeys(value);
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

    public WebElement loginButton() {
        return base.waitPresent(LOGIN_BUTTON);
    }

    public WebElement privacyLink() {
        return base.waitPresent(PRIVACY_LINK);
    }
}
