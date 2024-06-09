package com.nektarinne.l18n;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TrTest {

    private final Tr underTest = new Tr();

    private String originalLocale;

    @BeforeEach
    void beforeEach() {
        originalLocale = underTest.setLocale("en-US");
    }

    @AfterEach
    void afterEach() {
        underTest.setLocale(originalLocale);
    }

    @Test
    void setLocale() {
        assertThat(underTest.tr(valid.TEST.toString())).isEqualTo("English");
        underTest.setLocale("fr-FR");
        assertThat(underTest.tr(valid.TEST.toString())).isEqualTo("French");
    }

    @Test
    void tr() {
        assertThat(underTest.tr(valid.TEST.toString())).isEqualTo("English");
        underTest.setLocale("fr-FR");
        assertThat(underTest.tr(valid.TEST.toString())).isEqualTo("French");
    }

    @Test
    void tr_withVariable() {
        assertThat(underTest.tr(valid.TEST_S_VARIABLE.toString(), "expected"))
                .isEqualTo("English expected variable");
        underTest.setLocale("fr-FR");
        assertThat(underTest.tr(valid.TEST_S_VARIABLE.toString(), "expected"))
                .isEqualTo("French expected variable");
    }

    @Test
    void tr_withNullVariable() {
        assertThat(underTest.tr(valid.TEST_S_VARIABLE.toString(), null))
                .isEqualTo("English %s variable");
        underTest.setLocale("fr-FR");
        assertThat(underTest.tr(valid.TEST_S_VARIABLE.toString(), null))
                .isEqualTo("French %s variable");
    }

    @Test
    void tr_withVariables() {
        assertThat(underTest.tr(valid.TEST_S_VAR_D.toString(), "expected", 1))
                .isEqualTo("English expected variable 1");
        underTest.setLocale("fr-FR");
        assertThat(underTest.tr(valid.TEST_S_VAR_D.toString(), "expected", 1))
                .isEqualTo("French expected variable 1");
    }

    enum valid {
        TEST,
        TEST_S_VARIABLE,
        TEST_S_VAR_D
    }
}