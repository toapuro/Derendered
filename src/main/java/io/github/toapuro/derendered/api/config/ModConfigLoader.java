package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.api.config.registry.EnableConfigGuiProvider;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;

import java.util.Map;

public class ModConfigLoader {

    private ConfigHolder<ModConfig> configHolder;

    public ConfigHolder<ModConfig> getConfigHolder() {
        ensureLoaded();
        return configHolder;
    }

    public void ensureLoaded() {
        if(configHolder == null) {
            configHolder = loadConfig();
        }
    }

    protected ConfigHolder<ModConfig> loadConfig() {
        GuiRegistry guiRegistry = AutoConfig.getGuiRegistry(ModConfig.class);
        guiRegistry.registerTypeProvider(new EnableConfigGuiProvider(), Map.class);

        return AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
    }
}
