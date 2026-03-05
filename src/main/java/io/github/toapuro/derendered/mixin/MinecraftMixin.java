package io.github.toapuro.derendered.mixin;

import io.github.toapuro.derendered.api.mixin.RequirePreloadOption;
import io.github.toapuro.derendered.api.config.PreloadOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@RequirePreloadOption(PreloadOption.PARTICLE_INSTANCING)
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Nullable
    public LocalPlayer player;

    @Inject(method = "runTick", at = @At("HEAD"))
    public void runTick(boolean pRenderLevel, CallbackInfo ci) {
        if (player != null) {
            player.sendSystemMessage(Component.literal("Hello World"));
        }
    }
}
