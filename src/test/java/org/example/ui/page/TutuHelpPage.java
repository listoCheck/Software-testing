package org.example.ui.page;

import org.example.ui.TutuUiTestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TutuHelpPage {
    private static final By HELP_LINK = By.xpath("(//a[normalize-space()='Справочная' and contains(@href, '/2read/')])[1]");

    private final TutuUiTestBase base;

    public TutuHelpPage(TutuUiTestBase base) {
        this.base = base;
    }

    public WebElement helpLink() {
        return base.waitVisible(HELP_LINK);
    }

    public String helpHref() {
        return helpLink().getAttribute("href");
    }
}
