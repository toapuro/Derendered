package io.github.toapuro.derendered.mixin.particleinstancing;

import com.llamalad7.mixinextras.sugar.Local;
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
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.*;

@Mixin(value = ParticleEngine.class, remap = false)
public class MixinParticleEngine {

    @Shadow(remap = true)
    @Final private TextureManager textureManager;

    @Shadow(remap = true)
    @Final private Map<ParticleRenderType, Queue<Particle>> particles;

    @Shadow(remap = true)
    @Final private TextureAtlas textureAtlas;

    @Unique
    private final InstancedBufferStack derendered$instancedBuffer = new InstancedBufferStack(Tesselator.getInstance().getBuilder());

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
                fallbackParticles.add(particle);
            }
        }

        Queue<Particle> particleQueue = this.particles.get(particleRenderType);

        int minimumBatchSize = (int) (particleQueue.size() * 0.05);

        // Fallback small batches
        Map<Integer, List<IInstancedParticle>> batchMap = new HashMap<>();
        preBatchMap.forEach((hash, batchParticles) -> {
            if (batchParticles.size() >= minimumBatchSize) {
                batchMap.put(hash, batchParticles);
            } else {
                fallbackParticles.addAll(batchParticles.stream()
                        .map(IInstancedParticle::self)
                        .toList());
            }
        });

        if(preBatchMap.isEmpty()) {
            return particles;
        }

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        // Rendering
        for (List<IInstancedParticle> batchParticles : batchMap.values()) {
            RenderResult result = InstancedParticleEngine.render(batchParticles, bufferbuilder, derendered$instancedBuffer, particleRenderType, activeRenderInfo, textureManager, partialTicks);

            if(result.failure()) {
                // If it fails, it fails for all particles
                return particles;
            }
        }

        return fallbackParticles;
    }
}
