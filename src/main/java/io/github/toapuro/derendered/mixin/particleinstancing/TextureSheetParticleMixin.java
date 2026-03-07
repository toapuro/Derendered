package io.github.toapuro.derendered.mixin.particleinstancing;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.toapuro.derendered.api.render.instancing.InstancedBufferStack;
import io.github.toapuro.derendered.api.render.instancing.particle.IInstancedParticle;
import io.github.toapuro.derendered.api.render.instancing.particle.InstancedParticleBufferBuilder;
import io.github.toapuro.derendered.api.render.instancing.particle.ParticleVertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TextureSheetParticle.class)
public abstract class TextureSheetParticleMixin extends SingleQuadParticle implements IInstancedParticle {

    @Shadow protected abstract float getU0();
    @Shadow protected abstract float getU1();
    @Shadow protected abstract float getV0();
    @Shadow protected abstract float getV1();

    @Shadow
    protected TextureAtlasSprite sprite;

    protected TextureSheetParticleMixin(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ);
    }

    @Override
    public boolean derendered$isVisible() {
        return this.alpha > 0.0f;
    }

    @SuppressWarnings("resource")
    @Override
    public int derendered$getBatchHash(TextureAtlas textureAtlas) {
        float uvTolerance = 0.01f;
        float colorTolerance = 0.02f;

        int h =      sprite.contents().name().hashCode();
        h = 31 * h + Float.floatToIntBits((int) ((getU0() - sprite.getU0()) / sprite.contents().width() / uvTolerance));
        h = 31 * h + Float.floatToIntBits((int) ((getU1() - sprite.getU0()) / sprite.contents().width() / uvTolerance));
        h = 31 * h + Float.floatToIntBits((int) ((getV0() - sprite.getV0()) / sprite.contents().height() / uvTolerance));
        h = 31 * h + Float.floatToIntBits((int) ((getV1() - sprite.getV0()) / sprite.contents().height() / uvTolerance));

        h = 31 * h + Float.floatToIntBits((int) rCol / colorTolerance);
        h = 31 * h + Float.floatToIntBits((int) gCol / colorTolerance);
        h = 31 * h + Float.floatToIntBits((int) bCol / colorTolerance);
        return h;
    }

    /// [SingleQuadParticle#render(VertexConsumer,Camera,float)]
    @Unique
    @Override
    public void derendered$renderInstance(InstancedBufferStack bufferStack, ParticleRenderType renderType, Camera renderInfo, float partialTicks) {
        Vec3 vec3 = renderInfo.getPosition();
        float offsetX = (float)(Mth.lerp(partialTicks, this.xo, this.x) - vec3.x());
        float offsetY = (float)(Mth.lerp(partialTicks, this.yo, this.y) - vec3.y());
        float offsetZ = (float)(Mth.lerp(partialTicks, this.zo, this.z) - vec3.z());

        Quaternionf quaternion = new Quaternionf(renderInfo.rotation());
        quaternion.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));

        int lightColor = this.getLightColor(partialTicks);

        bufferStack.expectFormat(ParticleVertexFormat.PARTICLE_ARRAY, ParticleVertexFormat.PARTICLE_ARRAY_DIVS);

        InstancedParticleBufferBuilder instance = bufferStack.instanceBuilder();

        /// {@link ParticleVertexFormat#PARTICLE_ARRAY}

        // UV2 location=0 2s
        instance.uv2(lightColor);
        // Alpha location=1 1f
        instance.alpha(alpha);
        // InstancePos location=2 3f
        instance.instancedPos(offsetX, offsetY, offsetZ);
        // Quaternion location=3 4f
        instance.quaternion(quaternion);
        // Size location=4 1f
        instance.size(getQuadSize(partialTicks));

        instance.endVertex();
    }


    @Unique
    public void derendered$renderVBOSingle(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        Quaternionf quaternionf;
        quaternionf = renderInfo.rotation();

        Vector3f[] positionVec = new Vector3f[] {
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };

        for(int i = 0; i < 4; ++i) {
            Vector3f position = positionVec[i];
            position.rotate(quaternionf);
        }

        float u0 = this.getU0();
        float u1 = this.getU1();
        float v0 = this.getV0();
        float v1 = this.getV1();
        buffer.vertex(positionVec[0].x(), positionVec[0].y(), positionVec[0].z()).uv(u1, v1).color(this.rCol, this.gCol, this.bCol, 1.0f).endVertex();
        buffer.vertex(positionVec[1].x(), positionVec[1].y(), positionVec[1].z()).uv(u1, v0).color(this.rCol, this.gCol, this.bCol, 1.0f).endVertex();
        buffer.vertex(positionVec[2].x(), positionVec[2].y(), positionVec[2].z()).uv(u0, v0).color(this.rCol, this.gCol, this.bCol, 1.0f).endVertex();
        buffer.vertex(positionVec[3].x(), positionVec[3].y(), positionVec[3].z()).uv(u0, v1).color(this.rCol, this.gCol, this.bCol, 1.0f).endVertex();
    }
}
