package io.github.toapuro.derendered.api.render.instancing.particle;

import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;

public interface IInstancedParticle {

    boolean derendered$isVisible();
    /**
     * Hash for batching
     */
    int derendered$getBatchHash(TextureAtlas textureAtlas);

    /**
     * Write to VBO
     */
    void derendered$renderVBOSingle(VertexBufferWriter writer, Camera renderInfo, float partialTicks);

    /**
     * Get TextureAtlasSprite
     */
    TextureAtlasSprite derendered$getSprite();

    /**
     * Write to Instance VBO
     *
     * @param renderType Needs to be a ParticleRenderType that uses begin(), but fail-safe.
     */
    void derendered$writeInstanceFast(VertexBufferWriter writer, long buffPtr, ParticleRenderType renderType, Vec3 camPos, float pPartialTicks);

    default Particle self() {
        return (Particle) this;
    }
}
