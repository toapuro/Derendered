package io.github.toapuro.derendered.mixin.morebatching;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.toapuro.derendered.api.config.ModConfig;
import io.github.toapuro.derendered.api.config.RuntimeOption;
import io.github.toapuro.derendered.api.render.morebatching.RenderTypeComparator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Mixin(MultiBufferSource.BufferSource.class)
public class MixinBufferSource {

    @Shadow
    protected Optional<RenderType> lastState;

    @SuppressWarnings("OptionalIsPresent")
    @ModifyExpressionValue(method = "getBuffer", at = @At(value = "INVOKE", target = "Ljava/util/Objects;equals(Ljava/lang/Object;Ljava/lang/Object;)Z"))
    public boolean equals(boolean original,
                          @Local(argsOnly = true) RenderType renderType) {
        if(!ModConfig.get().getRuntime().isEnabled(RuntimeOption.MORE_RENDERTYPE_BATCHING)) {
            return original;
        }

        if(this.lastState.isPresent()) {
            return original || RenderTypeComparator.contentEquals(this.lastState.get(), renderType);
        }
        return original;
    }
}
