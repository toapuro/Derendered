package io.github.toapuro.derendered.api.render.queryculling;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import lombok.Getter;
import net.minecraft.client.renderer.ShaderInstance;
import org.lwjgl.system.MemoryUtil;

@Getter
public class BoxfBuffer {

    private final VertexBuffer vertexBuffer;

    public BoxfBuffer() {
        vertexBuffer = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
        prerenderBuffer();
    }

    private void prerenderBuffer() {
        BufferBuilder builder = new BufferBuilder(512);
        builder.setQuadSorting(VertexSorting.DISTANCE_TO_ORIGIN);

        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        writeCubeBuffer(builder);

        BufferBuilder.RenderedBuffer rendered = builder.end();

        vertexBuffer.bind();
        vertexBuffer.upload(rendered);

        MemoryUtil.memFree(builder.buffer);
    }

    private void writeCubeBuffer(VertexConsumer consumer) {
        float[][][] faceVertices = new float[][][] {
                new float[][] {{-0.5f, -0.5f, 0.5f}, {0.5f, -0.5f, 0.5f}, {0.5f, 0.5f, 0.5f}, {-0.5f, 0.5f, 0.5f}},
                new float[][] {{0.5f, -0.5f, -0.5f}, {-0.5f, -0.5f, -0.5f}, {-0.5f, 0.5f, -0.5f}, {0.5f, 0.5f, -0.5f}},
                new float[][] {{-0.5f, -0.5f, -0.5f}, {-0.5f, -0.5f, 0.5f}, {-0.5f, 0.5f, 0.5f}, {-0.5f, 0.5f, -0.5f}},
                new float[][] {{0.5f, -0.5f, 0.5f}, {0.5f, -0.5f, -0.5f}, {0.5f, 0.5f, -0.5f}, {0.5f, 0.5f, 0.5f}},
                new float[][] {{-0.5f, 0.5f, 0.5f}, {0.5f, 0.5f, 0.5f}, {0.5f, 0.5f, -0.5f}, {-0.5f, 0.5f, -0.5f}},
                new float[][] {{-0.5f, -0.5f, -0.5f}, {0.5f, -0.5f, -0.5f}, {0.5f, -0.5f, 0.5f}, {-0.5f, -0.5f, 0.5f}}
        };

        for (float[][] verts : faceVertices) {
            for (float[] v : verts) {
                consumer.vertex(v[0], v[1], v[2]).endVertex();
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public void renderBox(Boxf box, PoseStack poseStack) {
        ShaderInstance boxShader = QueryCullingShader.QUERY_BOX.get();

        ShaderInstance lastShader = RenderSystem.getShader();
        RenderSystem.setShader(() -> boxShader);

        boxShader.getUniform("BoxMat").set(poseStack.last().pose());
        boxShader.getUniform("BoxOffset").set(box.minVec());
        boxShader.getUniform("BoxScale").set(box.getScale());

        vertexBuffer.bind();
        vertexBuffer.drawWithShader(RenderSystem.getModelViewMatrix(), RenderSystem.getProjectionMatrix(), boxShader);

        RenderSystem.setShader(() -> lastShader);
    }
}
