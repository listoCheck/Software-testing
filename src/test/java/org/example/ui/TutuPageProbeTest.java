package org.example.ui;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.openqa.selenium.JavascriptExecutor;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Diagnostic test — dumps the real page structure so we can fix selectors.
 * Not part of the UC suite; run once, read build/page-structure.json, then delete.
 */
@Tag("ui")
class TutuPageProbeTest extends TutuUiTestBase {

    @Test
    void probeLiveTutuPageStructure() throws Exception {
        // Navigate directly — skip waitForAnyVisible to avoid timeout
        try {
            driver.get(LIVE_URL);
        } catch (Exception ignored) {}
        Thread.sleep(8000); // let React/JS render

        JavascriptExecutor js = (JavascriptExecutor) driver;

        String json = (String) js.executeScript(
            "var r = {url: location.href, title: document.title, links: [], inputs: [], buttons: [], dataTi: []};" +

            "document.querySelectorAll('a[href]').forEach(function(e,i){" +
            "  if(i<40) r.links.push({href:e.href, text:e.textContent.trim().slice(0,60)});" +
            "});" +

            "document.querySelectorAll('input').forEach(function(e,i){" +
            "  if(i<30) r.inputs.push({" +
            "    type: e.type," +
            "    placeholder: e.placeholder," +
            "    ariaLabel: e.getAttribute('aria-label')," +
            "    dataTi: e.dataset.ti," +
            "    name: e.name," +
            "    className: e.className.slice(0,60)," +
            "    visible: e.offsetWidth>0" +
            "  });" +
            "});" +

            "document.querySelectorAll('button').forEach(function(e,i){" +
            "  if(i<20) r.buttons.push({" +
            "    text: e.textContent.trim().slice(0,60)," +
            "    ariaLabel: e.getAttribute('aria-label')," +
            "    dataTi: e.dataset.ti," +
            "    className: e.className.slice(0,60)," +
            "    visible: e.offsetWidth>0" +
            "  });" +
            "});" +

            "document.querySelectorAll('[data-ti]').forEach(function(e,i){" +
            "  if(i<50) r.dataTi.push({" +
            "    tag: e.tagName," +
            "    ti: e.dataset.ti," +
            "    text: e.textContent.trim().slice(0,50)," +
            "    visible: e.offsetWidth>0" +
            "  });" +
            "});" +

            "return JSON.stringify(r, null, 2);"
        );

        Path out = Path.of("build/page-structure.json");
        Files.createDirectories(out.getParent());
        Files.writeString(out, json == null ? "{\"error\":\"JS returned null\"}" : json);

        System.out.println("Page structure saved to: " + out.toAbsolutePath());
        System.out.println("URL: " + driver.getCurrentUrl());
        System.out.println("Title: " + driver.getTitle());

        assertTrue(json != null && !json.contains("\"error\""),
            "Page structure should be captured; see build/page-structure.json");
    }
}
