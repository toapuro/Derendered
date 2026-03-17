package io.github.toapuro.derendered.api.config.category;

import io.github.toapuro.derendered.api.config.PreloadOption;
import io.github.toapuro.derendered.api.config.RuntimeOption;

import java.util.HashMap;
import java.util.Map;

public class PreloadConfig extends OptionFlagsConfigData<PreloadOption> {

    private final Map<String, Boolean> enabled;

    public PreloadConfig() {
        this.enabled = new HashMap<>();
        initialize();
    }

    public void initialize() {
        for (RuntimeOption option : RuntimeOption.OPTIONS) {
            enabled.putIfAbsent(option.getId(), option.isDefaultEnabled());
        }
    }

    @Override
    protected Map<String, Boolean> mutableOptionMap() {
        return enabled;
    }

    @Override
    protected PreloadOption[] options() {
        return PreloadOption.values();
    }
}
