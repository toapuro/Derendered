package io.github.toapuro.derendered.mixin.particleinstancing;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.toapuro.derendered.api.render.instancing.particle.IInstancedParticle;
import io.github.toapuro.derendered.api.render.instancing.particle.InstancedParticleVertex;
import io.github.toapuro.derendered.api.render.util.TextureAtlasSpriteUtil;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
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
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TextureSheetParticle.class)
public abstract class MixinTextureSheetParticle extends SingleQuadParticle implements IInstancedParticle {

    @Shadow protected abstract float getU0();
    @Shadow protected abstract float getU1();
    @Shadow protected abstract float getV0();
    @Shadow protected abstract float getV1();

    @Shadow
    protected TextureAtlasSprite sprite;

    protected MixinTextureSheetParticle(ClientLevel pLevel, double pX, double pY, double pZ) {
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

    @Unique
    @SuppressWarnings("UnnecessaryLocalVariable")
    private static void derendered$writeVBOVertex(long buffer,
                                                  Quaternionf rotation,
                                                  float posX, float posY,
                                                  byte u, byte v, int light) {
        // Quaternion q0 = new Quaternion(rotation);
        float q0x = rotation.x();
        float q0y = rotation.y();
        float q0z = rotation.z();
        float q0w = rotation.w();

        // q0.hamiltonProduct(x, y, 0.0f, 0.0f)
        float q1x = (q0w * posX) - (q0z * posY);
        float q1y = (q0w * posY) + (q0z * posX);
        float q1w = (q0x * posY) - (q0y * posX);
        float q1z = -(q0x * posX) - (q0y * posY);

        // Quaternion q2 = new Quaternion(rotation);
        // q2.conjugate()
        float q2x = -q0x;
        float q2y = -q0y;
        float q2z = -q0z;
        float q2w = q0w;

        // q2.hamiltonProduct(q1)
        float q3x = q1z * q2x + q1x * q2w + q1y * q2z - q1w * q2y;
        float q3y = q1z * q2y - q1x * q2z + q1y * q2w + q1w * q2x;
        float q3z = q1z * q2z + q1x * q2y - q1y * q2x + q1w * q2w;

        InstancedParticleVertex.putVBO(buffer, q3x, q3y, q3z, u, v, light);
    }

    @Unique
    public void derendered$renderVBOSingle(VertexBufferWriter writer, Camera renderInfo, float partialTicks) {
        Quaternionf quaternion = renderInfo.rotation();

        int packedLight = getLightColor(partialTicks);

        try (MemoryStack stack = MemoryStack.stackPush()) {
            long buffer = stack.nmalloc(4 * InstancedParticleVertex.VBO_STRIDE);
            long ptr = buffer;

            derendered$writeVBOVertex(ptr, quaternion,-1.0F, -1.0F, (byte) 1, (byte) 1, packedLight);
            ptr += InstancedParticleVertex.VBO_STRIDE;

            derendered$writeVBOVertex(ptr, quaternion,-1.0F, 1.0F, (byte) 1, (byte) 0, packedLight);
            ptr += InstancedParticleVertex.VBO_STRIDE;

            derendered$writeVBOVertex(ptr, quaternion,1.0F, 1.0F, (byte) 0, (byte) 0, packedLight);
            ptr += InstancedParticleVertex.VBO_STRIDE;

            derendered$writeVBOVertex(ptr, quaternion,1.0F, -1.0F, (byte) 0, (byte) 1, packedLight);

            writer.push(stack, buffer, 4, InstancedParticleVertex.VBO_FORMAT);
        }
    }

    /// [SingleQuadParticle#render(VertexConsumer,Camera,float)]
    @Unique
    @Override
    public final void derendered$writeInstanceFast(VertexBufferWriter writer, long buffPtr, ParticleRenderType renderType, Vec3 camPos, float partialTicks) {
        float x = (float)(Mth.lerp(partialTicks, this.xo, this.x) - camPos.x());
        float y = (float)(Mth.lerp(partialTicks, this.yo, this.y) - camPos.y());
        float z = (float)(Mth.lerp(partialTicks, this.zo, this.z) - camPos.z());

        int color = ColorABGR.pack(this.rCol , this.gCol, this.bCol, this.alpha);

        float size = this.getQuadSize(partialTicks);
        float angle = Mth.lerp(partialTicks, this.oRoll, this.roll);

        InstancedParticleVertex.putInstance(
                buffPtr, x, y, z, color,
                (short) (TextureAtlasSpriteUtil.getSpriteU(sprite, getU0()) * 65535f),
                (short) (TextureAtlasSpriteUtil.getSpriteV(sprite, getV0()) * 65535f),
                (short) (TextureAtlasSpriteUtil.getSpriteU(sprite, getU1()) * 65535f),
                (short) (TextureAtlasSpriteUtil.getSpriteV(sprite, getV1()) * 65535f),
                size, (byte) (angle * 255f)
        );
    }
}
