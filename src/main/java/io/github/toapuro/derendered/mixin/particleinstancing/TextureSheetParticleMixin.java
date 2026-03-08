package io.github.toapuro.derendered.mixin.particleinstancing;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.toapuro.derendered.api.render.instancing.InstancedBufferStack;
import io.github.toapuro.derendered.api.render.instancing.particle.IInstancedParticle;
import io.github.toapuro.derendered.api.render.instancing.particle.InstancedParticleBufferBuilder;
import io.github.toapuro.derendered.api.render.instancing.particle.ParticleVertexFormat;
import io.github.toapuro.derendered.api.render.util.TextureAtlasSpriteUtil;
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
        int h = getLightColor(0);
        h = h * 32 + textureAtlas.getId();
        h = h * 32 + sprite.contents().name().hashCode();
        return h;
    }

    @Override
    public TextureAtlasSprite derendered$getSprite() {
        return sprite;
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

        bufferStack.expectFormat(ParticleVertexFormat.PARTICLE_ARRAY, ParticleVertexFormat.PARTICLE_ARRAY_DIVS);

        InstancedParticleBufferBuilder instance = bufferStack.instanceBuilder();

        /// {@link ParticleVertexFormat#PARTICLE_ARRAY}

        // Color location=0 4b
        instance.color(rCol, gCol, bCol, alpha);
        // UVTransform location=1 4f
        instance.localUV(
                TextureAtlasSpriteUtil.getSpriteU(sprite, getU0()),
                TextureAtlasSpriteUtil.getSpriteV(sprite, getV0()),
                TextureAtlasSpriteUtil.getSpriteU(sprite, getU1()),
                TextureAtlasSpriteUtil.getSpriteV(sprite, getV1())
        );
        // InstancePos location=2 3f
        instance.instancedPos(offsetX, offsetY, offsetZ);
        // Size location=3 1f
        instance.size(getQuadSize(partialTicks));
        // Roll location=4 1f
        instance.roll(Mth.lerp(partialTicks, this.oRoll, this.roll));

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

        int packedLight = getLightColor(partialTicks);

        buffer.vertex(positionVec[0].x(), positionVec[0].y(), positionVec[0].z()).uv(1, 1).uv2(packedLight).endVertex();
        buffer.vertex(positionVec[1].x(), positionVec[1].y(), positionVec[1].z()).uv(1, 0).uv2(packedLight).endVertex();
        buffer.vertex(positionVec[2].x(), positionVec[2].y(), positionVec[2].z()).uv(0, 0).uv2(packedLight).endVertex();
        buffer.vertex(positionVec[3].x(), positionVec[3].y(), positionVec[3].z()).uv(0, 1).uv2(packedLight).endVertex();
    }
}
