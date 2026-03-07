package io.github.toapuro.derendered.api.render.instancing;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.joml.Quaternionf;

@SuppressWarnings("UnusedReturnValue")
public class InstancedBufferBuilder extends BufferBuilder {

    public InstancedBufferBuilder(int pCapacity) {
        super(pCapacity);
    }

    public InstancedBufferBuilder instancedPos(float x, float y, float z) {
        VertexFormatElement vertexformatelement = this.currentElement();
        if (vertexformatelement.getUsage() != VertexFormatElement.Usage.POSITION) {
            return this;
        } else if (vertexformatelement.getType() == VertexFormatElement.Type.FLOAT && vertexformatelement.getCount() == 3) {
            this.putFloat(0, x);
            this.putFloat(4, y);
            this.putFloat(8, z);
            this.nextElement();
            return this;
        } else {
            throw new IllegalStateException();
        }
    }

    public InstancedBufferBuilder quaternion(Quaternionf quaternionf) {
        VertexFormatElement vertexformatelement = this.currentElement();
        if (vertexformatelement.getUsage() != VertexFormatElement.Usage.GENERIC) {
            return this;
        } else if (vertexformatelement.getType() == VertexFormatElement.Type.FLOAT && vertexformatelement.getCount() == 4) {
            this.putFloat(0, quaternionf.x);
            this.putFloat(4, quaternionf.y);
            this.putFloat(8, quaternionf.z);
            this.putFloat(12, quaternionf.w);
            this.nextElement();
            return this;
        } else {
            throw new IllegalStateException();
        }
    }
}
