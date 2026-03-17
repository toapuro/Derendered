package io.github.toapuro.derendered.api.render.instancing;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.ByteBuffer;

@SuppressWarnings("NullableProblems")
public class EmptyBufferBuilder extends BufferBuilder {

    public static EmptyBufferBuilder EMPTY = new EmptyBufferBuilder(1);

    public EmptyBufferBuilder(int pCapacity) {
        super(pCapacity);
    }

    @Override
    public void setQuadSorting(VertexSorting pQuadSorting) {
    }

    @Override
    public SortState getSortState() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void restoreSortState(SortState pSortState) {
    }

    @Override
    public void begin(VertexFormat.Mode pMode, VertexFormat pFormat) {
    }

    @Override
    public boolean isCurrentBatchEmpty() {
        return false;
    }

    @Override
    public @Nullable RenderedBuffer endOrDiscardIfEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RenderedBuffer end() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putByte(int pIndex, byte pByteValue) {
    }

    @Override
    public void putShort(int pIndex, short pShortValue) {
    }

    @Override
    public void putFloat(int pIndex, float pFloatValue) {
    }

    @Override
    public void endVertex() {
    }

    @Override
    public void nextElement() {
    }

    @Override
    public VertexConsumer color(int pRed, int pGreen, int pBlue, int pAlpha) {
        return this;
    }

    @Override
    public void vertex(float pX, float pY, float pZ, float pRed, float pGreen, float pBlue, float pAlpha, float pTexU, float pTexV, int pOverlayUV, int pLightmapUV, float pNormalX, float pNormalY, float pNormalZ) {
    }

    @Override
    public void clear() {
    }

    @Override
    public void discard() {
    }

    @Override
    public VertexFormatElement currentElement() {
        return DefaultVertexFormat.ELEMENT_PADDING;
    }

    @Override
    public boolean building() {
        return false;
    }

    @Override
    public void putBulkData(ByteBuffer buffer) {
    }

    @Override
    public VertexConsumer vertex(double pX, double pY, double pZ) {
        return this;
    }

    @Override
    public VertexConsumer uv(float pU, float pV) {
        return this;
    }

    @Override
    public VertexConsumer overlayCoords(int pU, int pV) {
        return this;
    }

    @Override
    public VertexConsumer uv2(int pU, int pV) {
        return this;
    }

    @Override
    public VertexConsumer uvShort(short pU, short pV, int pIndex) {
        return this;
    }

    @Override
    public VertexConsumer normal(float pX, float pY, float pZ) {
        return this;
    }

    @Override
    public void defaultColor(int p_85830_, int p_85831_, int p_85832_, int p_85833_) {
    }

    @Override
    public void unsetDefaultColor() {
    }

    @Override
    public VertexConsumer color(float pRed, float pGreen, float pBlue, float pAlpha) {
        return this;
    }

    @Override
    public VertexConsumer color(int pColorARGB) {
        return this;
    }

    @Override
    public VertexConsumer uv2(int pLightmapUV) {
        return this;
    }

    @Override
    public VertexConsumer overlayCoords(int pOverlayUV) {
        return this;
    }

    @Override
    public void putBulkData(PoseStack.Pose pPoseEntry, BakedQuad pQuad, float pRed, float pGreen, float pBlue, int pCombinedLight, int pCombinedOverlay) {
    }

    @Override
    public void putBulkData(PoseStack.Pose pPoseEntry, BakedQuad pQuad, float[] pColorMuls, float pRed, float pGreen, float pBlue, int[] pCombinedLights, int pCombinedOverlay, boolean pMulColor) {
    }

    @Override
    public void putBulkData(PoseStack.Pose pPoseEntry, BakedQuad pQuad, float[] pColorMuls, float pRed, float pGreen, float pBlue, float alpha, int[] pCombinedLights, int pCombinedOverlay, boolean pMulColor) {
    }

    @Override
    public VertexConsumer vertex(Matrix4f pMatrix, float pX, float pY, float pZ) {
        return this;
    }

    @Override
    public VertexConsumer normal(Matrix3f pMatrix, float pX, float pY, float pZ) {
        return this;
    }

    @Override
    public VertexConsumer misc(VertexFormatElement element, int... rawData) {
        return this;
    }

    @Override
    public void putBulkData(PoseStack.Pose pose, BakedQuad bakedQuad, float red, float green, float blue, float alpha, int packedLight, int packedOverlay, boolean readExistingColor) {
    }

    @Override
    public int applyBakedLighting(int packedLight, ByteBuffer data) {
        return 0;
    }

    @Override
    public void applyBakedNormals(Vector3f generated, ByteBuffer data, Matrix3f normalTransform) {
    }
}
