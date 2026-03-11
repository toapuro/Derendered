package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.api.compat.OculusCompat;
import io.github.toapuro.derendered.lang.DerenderedLang;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.chat.Component;

import java.util.Optional;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public enum RuntimeOption implements IOption {
    PARTICLE_INSTANCING("particle_instancing", DerenderedLang.PARTICLE_INSTANCING, true, flag -> {
        if(OculusCompat.isEnabled() && OculusCompat.isShaderEnabled()) {
            return Optional.of(Component.translatable(""));
        }
        return Optional.empty();
    });

    public static final RuntimeOption[] OPTIONS = values();

    private final String id;
    private final DerenderedLang lang;
    private final boolean defaultEnabled;
    private final Function<Boolean, Optional<Component>> validator;

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
    public Optional<Component> validate(boolean flag) {
        return validator.apply(flag);
    }

    @Override
    public String toString() {
        return id;
    }
}
