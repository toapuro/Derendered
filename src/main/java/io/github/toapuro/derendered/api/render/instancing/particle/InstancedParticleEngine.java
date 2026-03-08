package io.github.toapuro.derendered.api.render.instancing.particle;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.toapuro.derendered.api.render.instancing.EmptyBufferBuilder;
import io.github.toapuro.derendered.api.render.instancing.InstancedBufferStack;
import io.github.toapuro.derendered.api.render.shader.ShaderHolder;
import io.github.toapuro.derendered.api.render.util.RenderResult;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;

import java.util.List;
import java.util.Objects;

public class InstancedParticleEngine {

    public static RenderResult render(List<IInstancedParticle> particles, BufferBuilder bufferbuilder, InstancedBufferStack instancedBuffer, ParticleRenderType particleRenderType, Camera activeRenderInfo, TextureManager textureManager, float partialTicks) {
        if(particles.isEmpty()) return RenderResult.PASS;

        ShaderInstance shader = ParticleInstancingShader.PARTICLE_INSTANCING.get();

        // Setup
        instancedBuffer.beginInstance(VertexFormat.Mode.QUADS, ParticleVertexFormat.PARTICLE_ARRAY, ParticleVertexFormat.PARTICLE_ARRAY_DIVS);
        particleRenderType.begin(EmptyBufferBuilder.EMPTY, textureManager);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, ParticleVertexFormat.PARTICLE_VBO);
        RenderSystem.setShader(() -> shader);

        if(!bufferbuilder.building()) {
            // Unsupported Particle Type
            return RenderResult.FAILURE;
        }

        // Build VBO
        IInstancedParticle firstParticle = particles.get(0);
        firstParticle.derendered$renderVBOSingle(bufferbuilder, activeRenderInfo, partialTicks);

        // Build Instanced VBO
        for (IInstancedParticle instancedParticle : particles) {
            instancedParticle.derendered$renderInstance(instancedBuffer, particleRenderType, activeRenderInfo, partialTicks);
        }


        TextureAtlasSprite sprite = firstParticle.derendered$getSprite();

        Uniform spriteUV0 = Objects.requireNonNull(shader.getUniform("SpriteUV0"), "SpriteUV0 must not be null");
        Uniform spriteUV1 = Objects.requireNonNull(shader.getUniform("SpriteUV1"), "SpriteUV1 must not be null");

        spriteUV0.set(sprite.getU0(), sprite.getV0());
        spriteUV1.set(sprite.getU1(), sprite.getV1());

        instancedBuffer.flush(RenderSystem.getModelViewStack().last().pose(), RenderSystem.getProjectionMatrix());

        return RenderResult.SUCCESS;
    }
}
