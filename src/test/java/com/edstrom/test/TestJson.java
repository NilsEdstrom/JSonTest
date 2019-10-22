package com.edstrom.test;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJson {
    private ProcessJson processJson;

    @BeforeEach
    void init() {
        processJson = new ProcessJson();
        assertTrue(processJson.JSON_STRING != null);
    }

    @Test
    @DisplayName("JSON correctly loaded from URL")
    void testJsonLoaded() {
        assertTrue(processJson.JSON_STRING.startsWith("{"));
    }

    @Test
    @DisplayName("JSON file is properly parsed")
    void testJsonParsed() {
        assertTrue(processJson.docContext != null);
    }

    @Test
    @DisplayName("retrieval of 'subviews'")
    void retrieveSubview() {
        processJson.retrieveStackViewItem("$..subviews[?(@['class'] == 'StackView')]");
    }

    @Test
    @DisplayName("retrieve css class names")
    void retrieveCssClassNames() {
        processJson.retrieveClassNames("$..classNames");
    }
    @Test
    @DisplayName("retrieve '#videoMode'")
    void retrieveVideoMode() {
        processJson.retrieveVideoMode("$..control[?(@.identifier == 'videoMode')]");
    }


    @AfterEach
    void tearDown() {
        processJson = null;
    }

}
