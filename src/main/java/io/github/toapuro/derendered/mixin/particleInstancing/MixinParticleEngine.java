package io.github.toapuro.derendered.mixin.particleInstancing;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.toapuro.derendered.api.render.particleInstancing.*;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureManager;
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

@Mixin(value = ParticleEngine.class, remap = false)
public class MixinParticleEngine {

    @Shadow(remap = true)
    @Final
    private TextureManager textureManager;

    @Unique
    private final InstancedBufferStack derendered$instancedBuffer = new InstancedBufferStack(Tesselator.getInstance().getBuilder());

    @ModifyVariable(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/client/Camera;FLnet/minecraft/client/renderer/culling/Frustum;)V",
            at = @At(value = "STORE"), index = 10)
    public Iterable<Particle> render(Iterable<Particle> particles,
                                     @Local(argsOnly = true) Frustum clippingHelper,
                                     @Local(argsOnly = true) Camera activeRenderInfo,
                                     @Local(argsOnly = true) float partialTicks,
                                     @Local ParticleRenderType particleRenderType) {
        ImmutableList.Builder<Particle> unInstancedParticles = ImmutableList.builder();
        List<IInstancedParticle> instancedParticles = new ArrayList<>();

        for (Particle particle : particles) {
            if(particle instanceof IInstancedParticle instancedParticle) {
                if (clippingHelper != null && particle.shouldCull() && !clippingHelper.isVisible(particle.getBoundingBox())) continue;
                instancedParticles.add(instancedParticle);
            } else {
                unInstancedParticles.add(particle);
            }
        }

        Map<Integer, List<IInstancedParticle>> batchMap = new HashMap<>();
        for (IInstancedParticle instanced : instancedParticles) {
            batchMap.computeIfAbsent(instanced.derendered$getTypeHashCode(), integer -> new ArrayList<>())
                    .add(instanced);
        }

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        for (List<IInstancedParticle> particlesBatch : batchMap.values()) {
            if(particlesBatch.isEmpty()) continue;

            // Setup
            derendered$instancedBuffer.beginInstance(VertexFormat.Mode.QUADS, ParticleVertexFormat.PARTICLE_ARRAY, ParticleVertexFormat.PARTICLE_ARRAY_DIVS);
            particleRenderType.begin(EmptyBufferBuilder.EMPTY, this.textureManager);
            bufferbuilder.begin(VertexFormat.Mode.QUADS, ParticleVertexFormat.PARTICLE_VBO);
            RenderSystem.setShader(ParticleInstancingShader.PARTICLE_INSTANCING::get);

            if(!bufferbuilder.building()) {
                // Unsupported Particle Type
                return particles;
            }

            // Render VBO
            IInstancedParticle firstParticle = particlesBatch.get(0);
            firstParticle.derendered$renderSingle(bufferbuilder, activeRenderInfo, partialTicks);

            // Render Instanced VBO
            for (IInstancedParticle instancedParticle : particlesBatch) {
                instancedParticle.derendered$renderInstance(derendered$instancedBuffer, particleRenderType, activeRenderInfo, partialTicks);
            }

            derendered$instancedBuffer.flush(RenderSystem.getModelViewStack(), RenderSystem.getProjectionMatrix());
        }

        return unInstancedParticles.build();
    }
}
