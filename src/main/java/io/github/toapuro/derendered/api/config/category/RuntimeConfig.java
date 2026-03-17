package io.github.toapuro.derendered.api.config.category;

import io.github.toapuro.derendered.api.config.RuntimeOption;

import java.util.HashMap;
import java.util.Map;

public class RuntimeConfig extends OptionFlagsConfigData<RuntimeOption> {

    private final Map<String, Boolean> enabled;

    public int particleInstancingMinBatch = 1000;

    public RuntimeConfig() {
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
    protected RuntimeOption[] options() {
        return RuntimeOption.values();
    }
}
