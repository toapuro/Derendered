package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.Derendered;
import io.github.toapuro.derendered.api.config.category.PreloadConfig;
import io.github.toapuro.derendered.api.config.category.RuntimeConfig;
import lombok.Getter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Getter
@Config(name = Derendered.MODID)
public class ModConfig implements ConfigData {

    public static final ModConfigLoader LOADER = new ModConfigLoader();

    @ConfigEntry.Category("preload")
    @ConfigEntry.Gui.TransitiveObject
    private PreloadConfig preload = new PreloadConfig();

    @ConfigEntry.Category("runtime")
    @ConfigEntry.Gui.TransitiveObject
    private RuntimeConfig runtime = new RuntimeConfig();

    public static ModConfig get() {
        return LOADER.getConfigHolder().get();
    }

    public void initialize() {
        this.preload.initialize();
        this.runtime.initialize();
    }
}
