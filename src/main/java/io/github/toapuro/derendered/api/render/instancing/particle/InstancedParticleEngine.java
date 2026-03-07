package io.github.toapuro.derendered.api.render.instancing.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.toapuro.derendered.api.render.instancing.EmptyBufferBuilder;
import io.github.toapuro.derendered.api.render.instancing.InstancedBufferStack;
import io.github.toapuro.derendered.api.render.util.RenderResult;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;

import java.util.List;

public class InstancedParticleEngine {

    public static RenderResult render(List<IInstancedParticle> particles, BufferBuilder bufferbuilder, InstancedBufferStack instancedBuffer, ParticleRenderType particleRenderType, Camera activeRenderInfo, TextureManager textureManager, float partialTicks) {
        if(particles.isEmpty()) return RenderResult.PASS;

        // Setup
        instancedBuffer.beginInstance(VertexFormat.Mode.QUADS, ParticleVertexFormat.PARTICLE_ARRAY, ParticleVertexFormat.PARTICLE_ARRAY_DIVS);
        particleRenderType.begin(EmptyBufferBuilder.EMPTY, textureManager);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, ParticleVertexFormat.PARTICLE_VBO);
        RenderSystem.setShader(ParticleInstancingShader.PARTICLE_INSTANCING::get);

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

        instancedBuffer.flush(RenderSystem.getModelViewStack().last().pose(), RenderSystem.getProjectionMatrix());

        return RenderResult.SUCCESS;
    }
}
