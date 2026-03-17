package io.github.toapuro.derendered.api.config.category;

import io.github.toapuro.derendered.api.config.PreloadOption;

import java.util.HashMap;
import java.util.Map;

public class PreloadConfig extends OptionFlagsConfigData<PreloadOption> {

    private final Map<String, Boolean> enabled;

    public PreloadConfig() {
        this.enabled = new HashMap<>();

        for (PreloadOption option : PreloadOption.OPTIONS) {
            enabled.put(option.getId(), option.isDefaultEnabled());
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
