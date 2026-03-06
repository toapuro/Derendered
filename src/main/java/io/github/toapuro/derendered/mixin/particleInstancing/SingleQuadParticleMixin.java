package io.github.toapuro.derendered.mixin.particleInstancing;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.toapuro.derendered.api.config.PreloadOption;
import io.github.toapuro.derendered.api.mixin.RequirePreloadOption;
import io.github.toapuro.derendered.api.render.particleInstancing.IInstancedParticle;
import io.github.toapuro.derendered.api.render.particleInstancing.InstancedBufferBuilder;
import io.github.toapuro.derendered.api.render.particleInstancing.InstancedBufferStack;
import io.github.toapuro.derendered.api.render.particleInstancing.ParticleVertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

@RequirePreloadOption(PreloadOption.PARTICLE_INSTANCING)
@Mixin(SingleQuadParticle.class)
public abstract class SingleQuadParticleMixin extends Particle implements IInstancedParticle {

    @Shadow
    protected abstract float getU0();

    @Shadow
    protected abstract float getU1();

    @Shadow
    protected abstract float getV0();

    @Shadow
    protected abstract float getV1();

    @Shadow
    public abstract float getQuadSize(float pScaleFactor);

    protected SingleQuadParticleMixin(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
    }

    @Override
    public int derendered$getTypeHashCode() {
        return Objects.hash(getU0(), getU1(), getV0(), getV1());
    }

    /// [SingleQuadParticle#render(VertexConsumer,Camera,float)]
    @Unique
    @Override
    public void derendered$renderInstance(InstancedBufferStack bufferStack, ParticleRenderType renderType, Camera pRenderInfo, float pPartialTicks) {
        Vec3 vec3 = pRenderInfo.getPosition();
        float offsetX = (float)(Mth.lerp(pPartialTicks, this.xo, this.x) - vec3.x());
        float offsetY = (float)(Mth.lerp(pPartialTicks, this.yo, this.y) - vec3.y());
        float offsetZ = (float)(Mth.lerp(pPartialTicks, this.zo, this.z) - vec3.z());

        Quaternionf quaternion = new Quaternionf(pRenderInfo.rotation());
        quaternion.rotateZ(Mth.lerp(pPartialTicks, this.oRoll, this.roll));

        int lightColor = this.getLightColor(pPartialTicks);

        bufferStack.expectFormat(ParticleVertexFormat.PARTICLE_ARRAY, ParticleVertexFormat.PARTICLE_ARRAY_DIVS);

        InstancedBufferBuilder instance = bufferStack.instance();

        /// {@link ParticleVertexFormat#PARTICLE_ARRAY}

        // UV2 location=0 1s
        instance.uv2(lightColor);
        // InstancePos location=1 3f
        instance.instancedPos(offsetX, offsetY, offsetZ);
        // Quaternion location=2 4f
        instance.quaternion(quaternion);

        instance.endVertex();
    }


    @Unique
    public void derendered$renderSingle(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        Quaternionf quaternionf;
        quaternionf = pRenderInfo.rotation();

        Vector3f[] positionVec = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        float quadSize = this.getQuadSize(pPartialTicks);

        for(int i = 0; i < 4; ++i) {
            Vector3f position = positionVec[i];
            position.rotate(quaternionf);
            position.mul(quadSize);
        }

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        pBuffer.vertex(positionVec[0].x(), positionVec[0].y(), positionVec[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).endVertex();
        pBuffer.vertex(positionVec[1].x(), positionVec[1].y(), positionVec[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).endVertex();
        pBuffer.vertex(positionVec[2].x(), positionVec[2].y(), positionVec[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, this.alpha).endVertex();
        pBuffer.vertex(positionVec[3].x(), positionVec[3].y(), positionVec[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, this.alpha).endVertex();
    }
}
