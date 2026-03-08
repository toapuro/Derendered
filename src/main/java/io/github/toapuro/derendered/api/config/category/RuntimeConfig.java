package io.github.toapuro.derendered.api.config.category;

import io.github.toapuro.derendered.api.config.RuntimeOption;
import me.shedaniel.autoconfig.ConfigData;

import java.util.HashMap;
import java.util.Map;

public class RuntimeConfig implements ConfigData {

    private final Map<String, Boolean> enabled;

    public RuntimeConfig() {
        this.enabled = new HashMap<>();

        for (RuntimeOption option : RuntimeOption.OPTIONS) {
            enabled.put(option.getId(), option.isDefaultEnabled());
        }
    }

    public void setEnabled(RuntimeOption option, boolean enabled) {
        this.enabled.put(option.getId(), enabled);
    }

    public boolean isEnabled(RuntimeOption option) {
        return this.enabled.getOrDefault(option.getId(), option.isDefaultEnabled());
    }
}
