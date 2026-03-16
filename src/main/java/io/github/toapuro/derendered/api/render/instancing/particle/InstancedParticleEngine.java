package io.github.toapuro.derendered.api.render.instancing.particle;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.toapuro.derendered.api.render.instancing.EmptyBufferBuilder;
import io.github.toapuro.derendered.api.render.instancing.InstancedBufferStack;
import io.github.toapuro.derendered.api.render.util.RenderResult;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;

import java.util.List;
import java.util.Objects;

public class InstancedParticleEngine {

    public static RenderResult renderInstancing(List<IInstancedParticle> particles, BufferBuilder bufferbuilder, InstancedBufferStack stack, ParticleRenderType particleRenderType, Camera activeRenderInfo, TextureManager textureManager, float partialTicks) {
        if(particles.isEmpty()) return RenderResult.PASS;

        VertexFormat.Mode mode = VertexFormat.Mode.QUADS;
        VertexFormat vboFormat = ParticleVertexFormat.PARTICLE_VBO;
        ShaderInstance shader = ParticleInstancingShader.PARTICLE_INSTANCING.get();

        // Setup
        stack.beginInstance(mode, ParticleVertexFormat.PARTICLE_ARRAY, ParticleVertexFormat.PARTICLE_ARRAY_DIVS);
        particleRenderType.begin(EmptyBufferBuilder.EMPTY, textureManager);
        bufferbuilder.begin(mode, vboFormat);
        RenderSystem.setShader(() -> shader);

        if(!bufferbuilder.building()) {
            // Unsupported Particle Type
            return RenderResult.FAILURE;
        }

        // Build VBO
        IInstancedParticle firstParticle = particles.get(0);
        VertexBufferWriter vboWriter = VertexBufferWriter.of(bufferbuilder);
        firstParticle.derendered$renderVBOSingle(vboWriter, activeRenderInfo, partialTicks);

        // Build Instanced VBO
        var instancedBuilder = stack.instanceBuilder();
        VertexBufferWriter instanceWriter = VertexBufferWriter.of(instancedBuilder);

        for (IInstancedParticle instancedParticle : particles) {
            instancedParticle.derendered$renderInstance(instanceWriter, particleRenderType, activeRenderInfo, partialTicks);
        }

        TextureAtlasSprite sprite = firstParticle.derendered$getSprite();

        Uniform spriteUV0 = Objects.requireNonNull(shader.getUniform("SpriteUV0"), "SpriteUV0 must not be null");
        Uniform spriteUV1 = Objects.requireNonNull(shader.getUniform("SpriteUV1"), "SpriteUV1 must not be null");

        spriteUV0.set(sprite.getU0(), sprite.getV0());
        spriteUV1.set(sprite.getU1(), sprite.getV1());

        /*
        // Transform Feedback
        if(OculusCompat.isShaderEnabledSafe()) {
            ShaderInstance transformShader = ParticleInstancingShader.INSTANCING_TRANSFORM.get();
            ProgramManager.glUseProgram(transformShader.getId());

            VertexBuffer vboBuffer = vboFormat.getImmediateDrawVertexBuffer();

            // Set output VBO
            GL30.glBindBufferBase(GL30.GL_TRANSFORM_FEEDBACK_BUFFER, 0, vboBuffer.vertexBufferId);
            GL11.glEnable(GL30.GL_RASTERIZER_DISCARD);

            GL30.glBeginTransformFeedback(mode.asGLMode);



            GL30.glEndTransformFeedback();

            GL11.glDisable(GL30.GL_RASTERIZER_DISCARD);
            ProgramManager.glUseProgram(0);
        }
        */

        stack.flush(RenderSystem.getModelViewStack().last().pose(), RenderSystem.getProjectionMatrix());

        return RenderResult.SUCCESS;
    }
}
