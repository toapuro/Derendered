package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.lang.DerenderedLang;

public enum PreloadOption implements IOption {
    ;

    public static final PreloadOption[] OPTIONS = values();

    private final String id;
    private final DerenderedLang lang;
    private final boolean defaultEnabled;

    PreloadOption(String id, DerenderedLang lang, boolean defaultEnabled) {
        this.id = id;
        this.lang = lang;
        this.defaultEnabled = defaultEnabled;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public DerenderedLang getLang() {
        return lang;
    }

    @Override
    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }
}
