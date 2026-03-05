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

    public ModConfig() {
        this.preload = new HashMap<>();
        for (PreloadOption option : PreloadOption.OPTIONS) {
            preload.put(option.getId(), option.isDefaultEnabled());
        }
    }

    public boolean isPreloadOptionEnabled(PreloadOption option) {
        return this.preload.get(option.getId());
    }
}
