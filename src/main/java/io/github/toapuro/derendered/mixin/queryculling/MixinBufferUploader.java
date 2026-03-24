package io.github.toapuro.derendered.mixin.queryculling;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import io.github.toapuro.derendered.api.render.queryculling.BufferUploaderHost;
import io.github.toapuro.derendered.api.render.queryculling.buffer.BufferFlushListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BufferUploader.class)
public class MixinBufferUploader {

    @Inject(method = "drawWithShader", at = @At("HEAD"))
    private static void drawWithShader(BufferBuilder.RenderedBuffer buffer, CallbackInfo ci) {
        for (BufferFlushListener listener : BufferUploaderHost.getFlushListeners()) {
            listener.onFlush(buffer);
        }
    }
}
