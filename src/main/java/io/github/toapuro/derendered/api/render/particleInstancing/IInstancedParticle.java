package io.github.toapuro.derendered.api.render.particleInstancing;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleRenderType;

public interface IInstancedParticle {
    int derendered$getTypeHashCode();
    void derendered$renderSingle(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks);
    void derendered$renderInstance(InstancedBufferStack bufferStack, ParticleRenderType renderType, Camera pRenderInfo, float pPartialTicks);
}
