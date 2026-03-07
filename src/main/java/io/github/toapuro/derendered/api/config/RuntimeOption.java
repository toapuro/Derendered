package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.lang.DerenderedLang;
import net.minecraft.network.chat.Component;

public enum RuntimeOption implements IOption {
    PARTICLE_INSTANCING("particle_instancing", DerenderedLang.PARTICLE_INSTANCING, true);

    public static final RuntimeOption[] OPTIONS = values();

    private final String id;
    private final DerenderedLang lang;
    private final boolean defaultEnabled;

    RuntimeOption(String id, DerenderedLang lang, boolean defaultEnabled) {
        this.id = id;
        this.lang = lang;
        this.defaultEnabled = defaultEnabled;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public DerenderedLang getLang() {
        return lang;
    }

    @Override
    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }

    @Override
    public Component getDescription() {
        return Component.translatable(lang.getKey() + ".desc");
    }
}
