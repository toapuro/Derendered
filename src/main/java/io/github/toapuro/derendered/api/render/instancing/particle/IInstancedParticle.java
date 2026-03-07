package io.github.toapuro.derendered.api.render.instancing.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.toapuro.derendered.api.render.instancing.InstancedBufferStack;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;

public interface IInstancedParticle {

    boolean derendered$isVisible();
    /**
     * Hash for batching
     */
    int derendered$getBatchHash(TextureAtlas textureAtlas);

    /**
     * Write to VBO
     */
    void derendered$renderVBOSingle(VertexConsumer buffer, Camera renderInfo, float partialTicks);

    /**
     * Write to Instance VBO
     * @param renderType Needs to be a ParticleRenderType that uses begin(), but fail-safe.
     * @param renderInfo camera
     */
    void derendered$renderInstance(InstancedBufferStack bufferStack, ParticleRenderType renderType, Camera renderInfo, float pPartialTicks);

    default Particle self() {
        return (Particle) this;
    }
}
