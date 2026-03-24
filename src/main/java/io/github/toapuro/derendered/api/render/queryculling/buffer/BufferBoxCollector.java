package io.github.toapuro.derendered.api.render.queryculling.buffer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import io.github.toapuro.derendered.api.render.queryculling.Boxf;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public class BufferBoxCollector {

    public static Boxf getBoxFromBuffer(BufferBuilder.RenderedBuffer builder, Matrix4f pose) {
        Vector3f min = new Vector3f(Float.MAX_VALUE);
        Vector3f max = new Vector3f(Float.MIN_VALUE);

        BufferBuilder.DrawState state = builder.drawState();

        ByteBuffer buffer = builder.vertexBuffer();
        long ptr = MemoryUtil.memAddress(buffer);

        int vertexSize = state.format().getVertexSize();
        int vertices = state.vertexCount();

        if(vertices == 0) {
            return Boxf.ZERO;
        }

        Matrix4f invertedPose = pose.invert();
        for (int i = 0; i < vertices; i++) {
            float x = MemoryUtil.memGetFloat(ptr + (long) i * vertexSize);
            float y = MemoryUtil.memGetFloat(ptr + (long) i * vertexSize + 4L);
            float z = MemoryUtil.memGetFloat(ptr + (long) i * vertexSize + 8L);

            Vector4f transform = invertedPose.transform(new Vector4f(x, y, z, 1.0f));
            Vector3f vec = new Vector3f(transform.x, transform.y, transform.z);
            min.min(vec);
            max.max(vec);
        }

        return new Boxf(min, max);
    }
}
