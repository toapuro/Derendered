package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.lang.DerenderedLang;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;

public class ConfigScreen {

    private final ConfigHolder<ModConfig> config;
    private final ConfigBuilder builder;

    public ConfigScreen(ConfigHolder<ModConfig> config, Screen parent) {
        this.config = config;

        builder = ConfigBuilder.create();

        builder.setParentScreen(parent);
        builder.setTitle(DerenderedLang.CONFIG_TITLE.asComponent());
        builder.setSavingRunnable(config::save);

        createPreloadCategory();
        createRuntimeCategory();
    }

    protected void createPreloadCategory() {
        ConfigCategory category = builder.getOrCreateCategory(DerenderedLang.PRELOAD_TITLE.asComponent());

        ConfigEntryBuilder entries = builder.entryBuilder();
        for (PreloadOption option : PreloadOption.OPTIONS) {
            category.addEntry(
                    entries.startBooleanToggle(option.getLang().asComponent(), config.get().isPreloadOptionEnabled(option))
                            .requireRestart()
                            .setTooltip(option.getDescription())
                            .setSaveConsumer(enabled -> config.get().preload.put(option.getId(), enabled))
                            .build()
            );
        }
    }

    protected void createRuntimeCategory() {
        ConfigCategory category = builder.getOrCreateCategory(DerenderedLang.RUNTIME_TITLE.asComponent());

        ConfigEntryBuilder entries = builder.entryBuilder();
        for (RuntimeOption option : RuntimeOption.OPTIONS) {
            category.addEntry(
                    entries.startBooleanToggle(option.getLang().asComponent(), config.get().isRuntimeOptionEnabled(option))
                            .setTooltip(option.getDescription())
                            .setSaveConsumer(enabled -> config.get().runtime.put(option.getId(), enabled))
                            .build()
            );
        }
    }

    public Screen build() {
        return builder.build();
    }
}
