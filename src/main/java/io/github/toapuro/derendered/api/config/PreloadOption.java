package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.api.config.option.OptionProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PreloadOption implements IConfigOption {
    ;

    public static final PreloadOption[] OPTIONS = values();

    private final String id;
    private final boolean defaultEnabled;
    private final OptionProperties properties;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }

    @Override
    public OptionProperties properties() {
        return properties;
    }

    @Override
    public String toString() {
        return id;
    }
}
