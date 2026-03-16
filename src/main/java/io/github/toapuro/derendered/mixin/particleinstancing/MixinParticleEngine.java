package io.github.toapuro.derendered.mixin.particleinstancing;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import io.github.toapuro.derendered.api.config.ModConfig;
import io.github.toapuro.derendered.api.config.RuntimeOption;
import io.github.toapuro.derendered.api.render.instancing.InstancedBufferStack;
import io.github.toapuro.derendered.api.render.instancing.particle.IInstancedParticle;
import io.github.toapuro.derendered.api.render.instancing.particle.InstancedParticleEngine;
import io.github.toapuro.derendered.api.render.util.RenderResult;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(value = ParticleEngine.class, remap = false)
public class MixinParticleEngine {

    @Shadow(remap = true)
    @Final private TextureManager textureManager;

    @Shadow(remap = true)
    @Final private TextureAtlas textureAtlas;

    @Unique
    private final InstancedBufferStack derendered$instancedBuffer = new InstancedBufferStack(Tesselator.getInstance().getBuilder());

    @Unique
    private final InstancedParticleEngine derendered$instancedParticleEngine = new InstancedParticleEngine();

    @Unique
    private static @NotNull Map<Integer, List<IInstancedParticle>> derendered$fallbackSmallBatches(Map<Integer, List<IInstancedParticle>> preBatchMap, int minimumBatchSize, Consumer<List<Particle>> listFallback) {
        Map<Integer, List<IInstancedParticle>> batchMap = new HashMap<>();
        preBatchMap.forEach((hash, batchParticles) -> {
            if (batchParticles.size() >= minimumBatchSize) {
                batchMap.put(hash, batchParticles);
            } else {
                listFallback.accept(batchParticles.stream()
                        .map(IInstancedParticle::self)
                        .toList());
            }
        });
        return batchMap;
    }

    @ModifyVariable(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V",
            at = @At(value = "STORE"), index = 10)
    public Iterable<Particle> render(Iterable<Particle> particles,
                                     @Local(argsOnly = true) Frustum clippingHelper,
                                     @Local(argsOnly = true) Camera activeRenderInfo,
                                     @Local(argsOnly = true) float partialTicks,
                                     @Local ParticleRenderType particleRenderType) {

        if(!ModConfig.get().getRuntime().isEnabled(RuntimeOption.PARTICLE_INSTANCING)) {
            return particles;
        }

        List<Particle> fallbackParticles = new ArrayList<>();
        Map<Integer, List<IInstancedParticle>> preBatchMap =
                derendered$makePrebatches(particles, clippingHelper, fallbackParticles::add);

        if(preBatchMap.isEmpty()) return particles;

        int minimumBatchSize = ModConfig.get().getRuntime().particleInstancingMinBatch;

        // Fallback small batches
        Map<Integer, List<IInstancedParticle>> batchMap =
                derendered$fallbackSmallBatches(preBatchMap, minimumBatchSize, fallbackParticles::addAll);

        if(preBatchMap.isEmpty()) return particles;

        BufferBuilder bufferbuilder = derendered$instancedBuffer.vboBuilder();

        RenderResult result = derendered$renderWithShaderInstanced(activeRenderInfo, partialTicks, particleRenderType, batchMap, bufferbuilder);
        if (result.failure()) return particles;

        return fallbackParticles;
    }

    @Unique
    private RenderResult derendered$renderWithShaderInstanced(Camera activeRenderInfo, float partialTicks, ParticleRenderType particleRenderType, Map<Integer, List<IInstancedParticle>> batchMap, BufferBuilder bufferbuilder) {
        ShaderInstance lastShader = RenderSystem.getShader();

        // Rendering
        for (List<IInstancedParticle> batchParticles : batchMap.values()) {
            RenderResult result = derendered$instancedParticleEngine.renderInstancing(batchParticles, bufferbuilder, derendered$instancedBuffer, particleRenderType, activeRenderInfo, textureManager, partialTicks);

            if(result.failure()) {
                // Fail once, fail all
                return result;
            }
        }

        RenderSystem.setShader(() -> lastShader);
        return RenderResult.SUCCESS;
    }

    @Unique
    private @NotNull Map<Integer, List<IInstancedParticle>> derendered$makePrebatches(Iterable<Particle> particles, Frustum clippingHelper, Consumer<Particle> fallback) {
        Map<Integer, List<IInstancedParticle>> preBatchMap = new HashMap<>();

        // Particle batching
        for (Particle particle : particles) {
            if (particle instanceof IInstancedParticle instancedParticle
                    && instancedParticle.derendered$isVisible()
                    && (clippingHelper == null || !particle.shouldCull() || clippingHelper.isVisible(particle.getBoundingBox()))) {
                preBatchMap
                        .computeIfAbsent(instancedParticle.derendered$getBatchHash(textureAtlas), k -> new ArrayList<>())
                        .add(instancedParticle);
            } else {
                fallback.accept(particle);
            }
        }
        return preBatchMap;
    }
}
