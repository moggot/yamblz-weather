package com.example.toor.yamblzweather.domain.utils;

import android.support.annotation.NonNull;

public enum APISupportedLanguages {
    ENGLISH("en"),
    RUSSIAN("ru"),
    ITALIAN("it"),
    SPANISH("es"),
    ROMANIAN("ro"),
    POLISH("pl"),
    FINNISH("fi"),
    DUTCH("nl"),
    FRENCH("fr"),
    BULGARIAN("bg"),
    SWEDISH("sv"),
    CHINESE_T("en"),
    CHINESE_S("en"),
    TURKISH("tr"),
    CROATIAN("hr"),
    CATALAN("ca");

    String language;

    APISupportedLanguages(String languageLocale) {
        this.language = languageLocale;
    }

    public @NonNull String getLanguageLocale() {
        return language;
    }
}
