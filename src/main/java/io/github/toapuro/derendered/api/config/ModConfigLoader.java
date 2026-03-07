package io.github.toapuro.derendered.api.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

public class ModConfigLoader {

    private ConfigHolder<ModConfig> configHolder;

    public ConfigHolder<ModConfig> getConfigHolder() {
        ensureLoaded();
        return configHolder;
    }

    public void ensureLoaded() {
        if(configHolder == null) {
            configHolder = AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
        }
    }
}
