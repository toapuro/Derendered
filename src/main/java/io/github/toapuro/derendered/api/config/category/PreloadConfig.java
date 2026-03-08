package io.github.toapuro.derendered.api.config.category;

import io.github.toapuro.derendered.api.config.PreloadOption;
import me.shedaniel.autoconfig.ConfigData;

import java.util.HashMap;
import java.util.Map;

public class PreloadConfig implements ConfigData {

    private final Map<String, Boolean> enabled;

    public PreloadConfig() {
        this.enabled = new HashMap<>();

        for (PreloadOption option : PreloadOption.OPTIONS) {
            enabled.put(option.getId(), option.isDefaultEnabled());
        }
    }

    public void setEnabled(PreloadOption option, boolean enabled) {
        this.enabled.put(option.getId(), enabled);
    }

    public boolean isEnabled(PreloadOption option) {
        return this.enabled.getOrDefault(option.getId(), option.isDefaultEnabled());
    }
}
