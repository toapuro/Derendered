package io.github.toapuro.derendered.lang;

import io.github.toapuro.derendered.Derendered;
import lombok.Getter;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

@Getter
public enum DerenderedLang {
    // Preload Config

    // Runtime Config
    PARTICLE_INSTANCING("title", "config.particle_instancing"),

    INVALID_FOR_SHADER("error", "invalid_for_shader");

    private final String key;

    DerenderedLang(String type, String id) {
        this.key = Util.makeDescriptionId(type, Derendered.getResource(id));
    }

    public MutableComponent asComponent() {
        return Component.translatable(key);
    }
}
