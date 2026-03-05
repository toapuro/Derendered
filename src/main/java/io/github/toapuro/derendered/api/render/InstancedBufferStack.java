package io.github.toapuro.derendered.api.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import lombok.RequiredArgsConstructor;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;

import java.util.Map;

@RequiredArgsConstructor
public class InstancedBufferStack {

    private static final InstancedBufferBuilder INSTANCE_BUFFER = new InstancedBufferBuilder(256);

    private final BufferBuilder builder;

    public void beginInstance(VertexFormat.Mode mode, VertexFormat instanceFormat, DivisorVertexFormat divisorFormat) {
        INSTANCE_BUFFER.begin(mode, instanceFormat);

        int i = 0;
        for (Map.Entry<VertexFormatElement, Integer> entry : divisorFormat.divisors().entrySet()) {
            if(instanceFormat.getElements().get(i) != entry.getKey()) {
                throw new RuntimeException("The divisor format does not match");
            }
            GL33.glVertexAttribDivisor(i, entry.getValue());
            i++;
        }
    }

    public void flush() {
        BufferBuilder.RenderedBuffer rendered = builder.end();
        BufferBuilder.DrawState state = rendered.drawState();
        VertexFormat format = state.format();
        VertexFormat.Mode mode = state.mode();

        BufferBuilder.RenderedBuffer instanceRendered = INSTANCE_BUFFER.end();
        BufferBuilder.DrawState instanceState = instanceRendered.drawState();

        VertexBuffer vertexBuffer = format.getImmediateDrawVertexBuffer();
        GpuBuffer instanceVBO = new GpuBuffer(GpuBuffer.Usage.DYNAMIC);
        // Bind VAO
        vertexBuffer.bind();
        vertexBuffer.upload(rendered);

        instanceVBO.bind(GL15.GL_ARRAY_BUFFER);
        instanceVBO.upload(GL15.GL_ARRAY_BUFFER, instanceRendered.vertexBuffer());

        GL31.glDrawElementsInstanced(mode.asGLMode, state.indexCount(), state.indexType().asGLType, 0L, instanceState.vertexCount());

        VertexBuffer.unbind();
    }

    public BufferBuilder builder() {
        return builder;
    }

    public InstancedBufferBuilder instance() {
        return INSTANCE_BUFFER;
    }
}
