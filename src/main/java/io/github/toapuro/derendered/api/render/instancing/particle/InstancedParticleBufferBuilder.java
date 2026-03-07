package io.github.toapuro.derendered.api.render.instancing.particle;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.joml.Quaternionf;

@SuppressWarnings("UnusedReturnValue")
public class InstancedParticleBufferBuilder extends BufferBuilder {

    public InstancedParticleBufferBuilder(int pCapacity) {
        super(pCapacity);
    }

    private boolean expectFormat(VertexFormatElement.Usage usage, VertexFormatElement.Type elementType, int count) {
        VertexFormatElement vertexformatelement = this.currentElement();
        if (vertexformatelement.getUsage() != usage) {
            return false;
        } else if (vertexformatelement.getType() == elementType && vertexformatelement.getCount() == count) {
            return true;
        } else {
            throw new IllegalStateException();
        }
    }

    public InstancedParticleBufferBuilder alpha(float alpha) {
        if (expectFormat(VertexFormatElement.Usage.GENERIC, VertexFormatElement.Type.FLOAT, 1)) {
            this.putFloat(0, alpha);
            this.nextElement();
        }
        return this;
    }

    public InstancedParticleBufferBuilder instancedPos(float x, float y, float z) {
        if (expectFormat(VertexFormatElement.Usage.POSITION, VertexFormatElement.Type.FLOAT, 3)) {
            this.putFloat(0, x);
            this.putFloat(4, y);
            this.putFloat(8, z);
            this.nextElement();
        }
        return this;
    }

    public InstancedParticleBufferBuilder quaternion(Quaternionf quaternionf) {
        if (expectFormat(VertexFormatElement.Usage.GENERIC, VertexFormatElement.Type.FLOAT, 4)) {
            this.putFloat(0, quaternionf.x);
            this.putFloat(4, quaternionf.y);
            this.putFloat(8, quaternionf.z);
            this.putFloat(12, quaternionf.w);
            this.nextElement();
        }
        return this;
    }

    public InstancedParticleBufferBuilder size(float size) {
        if (expectFormat(VertexFormatElement.Usage.GENERIC, VertexFormatElement.Type.FLOAT, 1)) {
            this.putFloat(0, size);
            this.nextElement();
        }
        return this;
    }
}
