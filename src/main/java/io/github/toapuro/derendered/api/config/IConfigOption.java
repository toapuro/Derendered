package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.api.config.option.OptionProperties;

public interface IConfigOption {
    String getId();
    boolean isDefaultEnabled();
    OptionProperties properties();
}
