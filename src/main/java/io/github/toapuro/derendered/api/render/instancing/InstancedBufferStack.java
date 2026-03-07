package io.github.toapuro.derendered.api.render.instancing;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import io.github.toapuro.derendered.api.context.MixinContexts;
import io.github.toapuro.derendered.api.render.GpuBuffer;
import io.github.toapuro.derendered.api.render.instancing.particle.InstancedParticleBufferBuilder;
import io.github.toapuro.derendered.api.render.util.VertexFormatUtil;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.renderer.ShaderInstance;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

import java.util.Objects;

@RequiredArgsConstructor
public class InstancedBufferStack {

    private final InstancedParticleBufferBuilder instanceBuilder = new InstancedParticleBufferBuilder(256);
    private final GpuBuffer instanceVBO = new GpuBuffer(GpuBuffer.Usage.DYNAMIC);
    private VertexFormat instanceFormat = null;
    private DivisorVertexFormat divisorFormat = null;
    private boolean building = false;

    private final BufferBuilder vboBuilder;

    public void beginInstance(VertexFormat.Mode mode, VertexFormat instanceFormat, DivisorVertexFormat divisorFormat) {
        instanceBuilder.begin(mode, instanceFormat);

        this.instanceFormat = instanceFormat;
        this.divisorFormat = divisorFormat;
        this.building = true;
    }

    public void expectFormat(VertexFormat instanceFormat, DivisorVertexFormat divisorFormat) {
        Preconditions.checkArgument(this.instanceFormat.equals(instanceFormat), "Instance format does not match");
        Preconditions.checkArgument(this.divisorFormat.equals(divisorFormat), "Divisor vertex format does not match");
    }

    public void flush(Matrix4f modelViewMatrix, Matrix4f projectionMatrix) {
        ensureBuilding();

        ShaderInstance shader = RenderSystem.getShader();
        if(shader == null) throw new IllegalStateException("Shader instance must not be null");

        vboBuilder.setQuadSorting(RenderSystem.getVertexSorting());

        // Must begin
        BufferBuilder.RenderedBuffer vboRendered = vboBuilder.end();
        BufferBuilder.DrawState vboRenderedState = vboRendered.drawState();
        VertexFormat vboFormat = vboRenderedState.format();
        VertexFormat.Mode vboMode = vboRenderedState.mode();

        BufferBuilder.RenderedBuffer instanceRendered = instanceBuilder.end();
        BufferBuilder.DrawState instanceState = instanceRendered.drawState();

        VertexBuffer vboBuffer = vboFormat.getImmediateDrawVertexBuffer();

        // Bind VAO
        vboBuffer.bind();

        {
            // Bind and Upload VBO & EBO
            vboBuffer.upload(vboRendered);

            // Setup VBO Attribute
            vboFormat.setupBufferState();
        }

        {
            // Bind Instance VBO
            instanceVBO.bind(GL15.GL_ARRAY_BUFFER);

            // Upload Instance VBO
            instanceVBO.upload(GL15.GL_ARRAY_BUFFER, instanceRendered.vertexBuffer());

            int indexOffset = vboFormat.getElements().size();

            // Setup Instance VBO Attributes (with offset)
            VertexFormatUtil.setupBufferState(instanceFormat, indexOffset);

            // Setup Attribute Divisor (with offset)
            divisorFormat.setup(instanceFormat, indexOffset);
        }

        try(var ignored = MixinContexts.GL_DRAW_FUNCTION.open(vbo ->
                GL31.glDrawElementsInstanced(vboMode.asGLMode, vboRenderedState.indexCount(), vboRenderedState.indexType().asGLType, 0L, instanceState.vertexCount())
        )) {
            vboBuffer.drawWithShader(modelViewMatrix, projectionMatrix, Objects.requireNonNull(RenderSystem.getShader()));
        }

        VertexBuffer.unbind();

        instanceRendered.release();
        end();
    }

    public void end() {
        this.building = false;
    }

    public void ensureBuilding() {
        if (!this.building || !vboBuilder.building()) {
            throw new IllegalStateException("Not building!");
        }
    }

    public InstancedParticleBufferBuilder instanceBuilder() {
        return instanceBuilder;
    }

    public BufferBuilder vboBuilder() {
        return vboBuilder;
    }
}
