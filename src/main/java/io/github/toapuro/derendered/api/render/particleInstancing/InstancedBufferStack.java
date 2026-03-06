package io.github.toapuro.derendered.api.render.particleInstancing;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.toapuro.derendered.api.context.MixinContexts;
import io.github.toapuro.derendered.api.render.GpuBuffer;
import io.github.toapuro.derendered.api.render.util.VertexFormatUtil;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.renderer.ShaderInstance;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

import java.util.Objects;

@RequiredArgsConstructor
public class InstancedBufferStack {

    private final InstancedBufferBuilder instanceBuffer = new InstancedBufferBuilder(256);
    private final GpuBuffer instanceVBO = new GpuBuffer(GpuBuffer.Usage.DYNAMIC);
    private final BufferBuilder vboBuilder;
    private VertexFormat instanceFormat;
    private DivisorVertexFormat divisorFormat;
    private boolean building;

    public void beginInstance(VertexFormat.Mode mode, VertexFormat instanceFormat, DivisorVertexFormat divisorFormat) {
        instanceBuffer.begin(mode, instanceFormat);
        this.instanceFormat = instanceFormat;
        this.divisorFormat = divisorFormat;
        this.building = true;
    }

    public void expectFormat(VertexFormat instanceFormat, DivisorVertexFormat divisorFormat) {
        Preconditions.checkArgument(this.instanceFormat == instanceFormat, "Instance format does not match");
        Preconditions.checkArgument(this.divisorFormat == divisorFormat, "Divisor vertex format does not match");
    }

    public void flush(PoseStack poseStack, Matrix4f projectionMatrix) {
        ensureRendering();

        ShaderInstance shader = RenderSystem.getShader();
        if(shader == null) throw new IllegalStateException("Shader instance must not be null");
        VertexFormat shaderFormat = shader.getVertexFormat();

        vboBuilder.setQuadSorting(RenderSystem.getVertexSorting());

        // Must begin
        BufferBuilder.RenderedBuffer vboRendered = vboBuilder.end();
        BufferBuilder.DrawState vboRenderedState = vboRendered.drawState();
        VertexFormat vboFormat = vboRenderedState.format();
        VertexFormat.Mode vboMode = vboRenderedState.mode();

        BufferBuilder.RenderedBuffer instanceRendered = instanceBuffer.end();
        BufferBuilder.DrawState instanceState = instanceRendered.drawState();

        VertexBuffer vboBuffer = vboFormat.getImmediateDrawVertexBuffer();

        // Bind VAO
        vboBuffer.bind();

        {
            // Bind VBO
            // Upload VBO & EBO
            vboBuffer.upload(vboRendered);

            // Setup VBO Attribute
            vboFormat.setupBufferState();
        }

        {
            // Bind Instance VBO
            instanceVBO.bind(GL15.GL_ARRAY_BUFFER);
            instanceVBO.upload(GL15.GL_ARRAY_BUFFER, instanceRendered.vertexBuffer());

            int indexOffset = vboFormat.getElements().size();

            // Setup Instance VBO Attribute (with offset)
            VertexFormatUtil.setupBufferState(instanceFormat, indexOffset);

            // Setup Attribute Divisor
            divisorFormat.setup(instanceFormat, indexOffset);
        }

        try(var frame = MixinContexts.GL_DRAW_FUNCTION.open(vbo ->
                GL31.glDrawElementsInstanced(vboMode.asGLMode, vboRenderedState.indexCount(), vboRenderedState.indexType().asGLType, 0L, instanceState.vertexCount())
        )) {
            vboBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, Objects.requireNonNull(RenderSystem.getShader()));
        }

        VertexBuffer.unbind();

        instanceRendered.release();
        end();
    }

    public void end() {
        this.building = false;
    }

    public void ensureRendering() {
        if (!this.building || !vboBuilder.building()) {
            throw new IllegalStateException("Not rendering!");
        }
    }

    public BufferBuilder builder() {
        return vboBuilder;
    }

    public InstancedBufferBuilder instance() {
        return instanceBuffer;
    }
}
