package io.github.toapuro.derendered.mixin.particleinstancing;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.vertex.VertexBuffer;
import io.github.toapuro.derendered.api.context.MixinContexts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VertexBuffer.class)
public class MixinVertexBuffer {

    @WrapWithCondition(method = "_drawWithShader", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexBuffer;draw()V"))
    public boolean onDraw(VertexBuffer instance) {
        if(MixinContexts.GL_DRAW_FUNCTION.isEmpty()) {
            return true;
        }

        MixinContexts.GL_DRAW_FUNCTION.get().accept(instance);
        return false;
    }
}
