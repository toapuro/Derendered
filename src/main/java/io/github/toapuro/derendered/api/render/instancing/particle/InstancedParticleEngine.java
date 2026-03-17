package io.github.toapuro.derendered.api.render.instancing.particle;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.toapuro.derendered.api.render.buffer.NativeMemBuffer;
import io.github.toapuro.derendered.api.render.instancing.EmptyBufferBuilder;
import io.github.toapuro.derendered.api.render.instancing.InstancedBufferStack;
import io.github.toapuro.derendered.api.render.util.RenderResult;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.system.MemoryStack;

import java.util.List;
import java.util.Objects;

public class InstancedParticleEngine {

    // The only time memory needs to be freed is when the process terminates, so this does not result in a memory leak.
    private final NativeMemBuffer bufferCache = new NativeMemBuffer(32768);

    public final RenderResult renderInstancing(List<IInstancedParticle> particles, BufferBuilder bufferbuilder, InstancedBufferStack bufStack, ParticleRenderType particleRenderType, Camera activeRenderInfo, TextureManager textureManager, float partialTicks) {
        if(particles.isEmpty()) return RenderResult.PASS;

        VertexFormat.Mode mode = VertexFormat.Mode.QUADS;
        VertexFormat vboFormat = ParticleVertexFormat.PARTICLE_VBO;
        ShaderInstance shader = ParticleInstancingShader.PARTICLE_INSTANCING.get();

        // Setup
        bufStack.beginInstance(mode, ParticleVertexFormat.PARTICLE_ARRAY, ParticleVertexFormat.PARTICLE_ARRAY_DIVS);
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
        var instancedBuilder = bufStack.instanceBuilder();
        VertexBufferWriter instanceWriter = VertexBufferWriter.of(instancedBuilder);


        int numParticles = particles.size();
        long stride = InstancedParticleVertex.INSTANCE_STRIDE;

        bufferCache.ensureSize((int) (stride * numParticles));

        Vec3 camPos = activeRenderInfo.getPosition();

        long currentPtr = bufferCache.ptr();

        for (IInstancedParticle particle : particles) {
            particle.derendered$writeInstanceFast(instanceWriter, currentPtr, particleRenderType, camPos, partialTicks);
            currentPtr += stride;
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            instanceWriter.push(stack, bufferCache.ptr(), numParticles, InstancedParticleVertex.INSTANCE_FORMAT);
        }

        // Write Uniforms
        TextureAtlasSprite sprite = firstParticle.derendered$getSprite();

        Uniform spriteUV0 = Objects.requireNonNull(shader.getUniform("SpriteUV0"), "SpriteUV0 must not be null");
        Uniform spriteUV1 = Objects.requireNonNull(shader.getUniform("SpriteUV1"), "SpriteUV1 must not be null");

        spriteUV0.set(sprite.getU0(), sprite.getV0());
        spriteUV1.set(sprite.getU1(), sprite.getV1());

        bufStack.flush(RenderSystem.getModelViewStack().last().pose(), RenderSystem.getProjectionMatrix());

        return RenderResult.SUCCESS;
    }
}
