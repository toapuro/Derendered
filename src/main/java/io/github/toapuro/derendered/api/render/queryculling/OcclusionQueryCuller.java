package io.github.toapuro.derendered.api.render.queryculling;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.toapuro.derendered.api.render.queryculling.buffer.BufferBoxCollector;
import io.github.toapuro.derendered.api.render.queryculling.buffer.BufferFlushListener;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL33;

import java.util.function.Consumer;

public class OcclusionQueryCuller {

    private static BoxfBuffer buffer = null;

    public static void render(OcclusionQuery query, IMutableQueryBox mutableBox, PoseStack poseStack, MultiBufferSource bufferSource, Consumer<MultiBufferSource> renderer) {
        if(buffer == null) {
            buffer = new BoxfBuffer();
        }

        boolean needRender = true;

        Boxf queryBox = mutableBox.derendered$getQueryBox();
        boolean isValidQueryBox = !queryBox.isPoint();
        if(isValidQueryBox) {
            poseStack.pushPose();
            poseStack.translate(1, 1, 1);

            GL15.glColorMask(false, false, false, false);
            GL15.glDepthMask(false);
            query.begin(GL33.GL_ANY_SAMPLES_PASSED);
            buffer.renderBox(queryBox, poseStack);
            query.end(GL33.GL_ANY_SAMPLES_PASSED);
            GL15.glColorMask(true, true, true, true);
            GL15.glDepthMask(true);

            poseStack.popPose();

            int samplePassed = query.getResultUInt();
            if(samplePassed == 0) {
                needRender = false;
            }
        }

        if (!needRender) {
            return;
        }

        if(!isValidQueryBox) {
            Boxf estimateBox = new Boxf(new Vector3f(Float.MAX_VALUE), new Vector3f(Float.MIN_VALUE));

            BufferFlushListener listener = renderedBuffer -> collectBox(renderedBuffer, estimateBox, poseStack.last().pose());

            if(bufferSource instanceof MultiBufferSource.BufferSource flushable) {
                flushable.endBatch();
            }

            BufferUploaderHost.pushFlushListener(listener);

            renderer.accept(bufferSource);

            if(bufferSource instanceof MultiBufferSource.BufferSource flushable) {
                flushable.endBatch();
            }

            BufferUploaderHost.popFlushListener(listener);

            if(!estimateBox.isInvalid()) {
                Vector3f minVec = estimateBox.minVec();
                Vector3f maxVec = estimateBox.maxVec();
                float quatScale = Math.max(
                        Math.max(Math.max(minVec.x, minVec.y), minVec.z),
                        Math.max(Math.max(maxVec.x, maxVec.y), maxVec.z)
                ) * 0.75f;
                Boxf newQueryBox = new Boxf(new Vector3f(-quatScale), new Vector3f(quatScale));

                mutableBox.derendered$setQueryBox(newQueryBox);
            }
        } else {
            renderer.accept(bufferSource);
        }
    }

    private static void collectBox(BufferBuilder.RenderedBuffer buffer, Boxf boxf, Matrix4f pose) {
        Boxf collected = BufferBoxCollector.getBoxFromBuffer(buffer, pose);
        boxf.expand(collected);
    }
}
