package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.api.compat.OculusCompat;
import io.github.toapuro.derendered.api.config.option.ErrorResult;
import io.github.toapuro.derendered.api.config.option.OptionProperties;
import io.github.toapuro.derendered.lang.DerenderedLang;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuntimeOption implements IConfigOption {
    PARTICLE_INSTANCING("particle_instancing", true, OptionProperties.builder()
            .errorChecker(flag -> flag && OculusCompat.isShaderEnabledSafe() ?
                    ErrorResult.forceFalse(DerenderedLang.INVALID_FOR_SHADER.asComponent())
                    : ErrorResult.keep(null))
            .flagModifier(flag -> flag && !OculusCompat.isShaderEnabledSafe())
            .build()),
    OCCLUSION_CULLING("occlusion_culling", false, OptionProperties.builder().build());

    public static final RuntimeOption[] OPTIONS = values();

    private final String id;
    private final boolean defaultEnabled;
    private final OptionProperties properties;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }

    @Override
    public OptionProperties properties() {
        return properties;
    }

    @Override
    public String toString() {
        return id;
    }
}
