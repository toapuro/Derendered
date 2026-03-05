package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.lang.DerenderedLang;
import lombok.Getter;
import net.minecraft.network.chat.Component;

@Getter
public enum PreloadOption {
    PARTICLE_INSTANCING("particle_instancing", DerenderedLang.PARTICLE_INSTANCING, true);

    public static final PreloadOption[] OPTIONS = values();

    private final String id;
    private final DerenderedLang lang;
    private final boolean defaultEnabled;

    PreloadOption(String id, DerenderedLang lang, boolean defaultEnabled) {
        this.id = id;
        this.lang = lang;
        this.defaultEnabled = defaultEnabled;
    }

    public Component getDescription() {
        return Component.translatable(lang.getKey() + ".desc");
    }
}
