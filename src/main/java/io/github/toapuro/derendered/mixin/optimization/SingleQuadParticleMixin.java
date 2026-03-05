package io.github.toapuro.derendered.mixin.optimization;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.toapuro.derendered.api.config.PreloadOption;
import io.github.toapuro.derendered.api.mixin.RequirePreloadOption;
import io.github.toapuro.derendered.api.render.InstancedBufferBuilder;
import io.github.toapuro.derendered.api.render.InstancedBufferStack;
import io.github.toapuro.derendered.api.render.ParticleVertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@RequirePreloadOption(PreloadOption.PARTICLE_INSTANCING)
@Mixin(SingleQuadParticle.class)
public abstract class SingleQuadParticleMixin extends Particle {

    protected SingleQuadParticleMixin(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
    }

    /// [#render(VertexConsumer,Camera,float)]
    @Unique
    public void derendered$renderInstanced(InstancedBufferStack bufferStack, ParticleRenderType renderType, Camera pRenderInfo, float pPartialTicks) {
        Vec3 vec3 = pRenderInfo.getPosition();
        float offsetX = (float)(Mth.lerp(pPartialTicks, this.xo, this.x) - vec3.x());
        float offsetY = (float)(Mth.lerp(pPartialTicks, this.yo, this.y) - vec3.y());
        float offsetZ = (float)(Mth.lerp(pPartialTicks, this.zo, this.z) - vec3.z());
        Quaternionf quaternion;
        if (this.roll == 0.0F) {
            quaternion = pRenderInfo.rotation();
        } else {
            quaternion = new Quaternionf(pRenderInfo.rotation());
            quaternion.rotateZ(Mth.lerp(pPartialTicks, this.oRoll, this.roll));
        }

        int lightColor = this.getLightColor(pPartialTicks);

        bufferStack.beginInstance(VertexFormat.Mode.QUADS, ParticleVertexFormat.PARTICLE_INSTANCED_EXTRAS, ParticleVertexFormat.PARTICLE_INSTANCED_EXTRAS_DIVS);

        InstancedBufferBuilder instance = bufferStack.instance();
        renderType.begin(instance, Minecraft.getInstance().textureManager);

        /// {@link io.github.toapuro.derendered.api.render.ParticleVertexFormat#PARTICLE_INSTANCED_EXTRAS}
        instance.uv2(lightColor);

        // InstancePos location=4 3f
        instance.instancedPos(offsetX, offsetY, offsetZ);
        // Quaternion location=5 4f
        instance.quaternion(quaternion);

        instance.endVertex();
    }
}
