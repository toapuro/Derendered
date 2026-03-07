package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.Derendered;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.HashMap;
import java.util.Map;

@Config(name = Derendered.MODID)
public class ModConfig implements ConfigData {

    public static final ModConfigLoader LOADER = new ModConfigLoader();

    @ConfigEntry.Gui.RequiresRestart
    Map<String, Boolean> preload;

    Map<String, Boolean> runtime;

    public ModConfig() {
        this.preload = new HashMap<>();
        this.runtime = new HashMap<>();

        for (PreloadOption option : PreloadOption.OPTIONS) {
            preload.put(option.getId(), option.isDefaultEnabled());
        }
        for (RuntimeOption option : RuntimeOption.OPTIONS) {
            runtime.put(option.getId(), option.isDefaultEnabled());
        }
    }

    public static ModConfig get() {
        return LOADER.getConfigHolder().get();
    }

    public boolean isPreloadOptionEnabled(PreloadOption option) {
        String id = option.getId();
        if(!this.preload.containsKey(id)) {
            return option.isDefaultEnabled();
        }
        return this.preload.get(id);
    }

    public boolean isRuntimeOptionEnabled(RuntimeOption option) {
        String id = option.getId();
        if(!this.runtime.containsKey(id)) {
            return option.isDefaultEnabled();
        }
        return this.runtime.get(id);
    }
}
