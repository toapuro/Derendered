package io.github.toapuro.derendered.api.render.instancing.particle;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.joml.Quaternionf;

@SuppressWarnings("UnusedReturnValue")
public class InstancedParticleBufferBuilder extends BufferBuilder {

    public InstancedParticleBufferBuilder(int pCapacity) {
        super(pCapacity);
    }

    private boolean expectFormat(VertexFormatElement element) {
        VertexFormatElement vertexformatelement = this.currentElement();
        if (vertexformatelement.getUsage() != element.getUsage()) {
            return false;
        } else if (vertexformatelement.getType() == element.getType() && vertexformatelement.getCount() == element.getCount()) {
            return true;
        } else {
            throw new IllegalStateException();
        }
    }

    public InstancedParticleBufferBuilder localUV(float u0, float v0, float u1, float v1) {
        if (expectFormat(ParticleVertexFormat.ELEMENT_LOCAL_UV0)) {
            this.putFloat(0, u0);
            this.putFloat(4, v0);
            this.putFloat(8, u1);
            this.putFloat(12, v1);
            this.nextElement();
        }
        return this;
    }

    public InstancedParticleBufferBuilder instancedPos(float x, float y, float z) {
        if (expectFormat(ParticleVertexFormat.ELEMENT_INSTANCE_POS)) {
            this.putFloat(0, x);
            this.putFloat(4, y);
            this.putFloat(8, z);
            this.nextElement();
        }
        return this;
    }

    public InstancedParticleBufferBuilder quaternion(Quaternionf quaternionf) {
        if (expectFormat(ParticleVertexFormat.ELEMENT_QUATERNION)) {
            this.putFloat(0, quaternionf.x);
            this.putFloat(4, quaternionf.y);
            this.putFloat(8, quaternionf.z);
            this.putFloat(12, quaternionf.w);
            this.nextElement();
        }
        return this;
    }

    public InstancedParticleBufferBuilder size(float size) {
        if (expectFormat(ParticleVertexFormat.ELEMENT_SIZE)) {
            this.putFloat(0, size);
            this.nextElement();
        }
        return this;
    }

    public InstancedParticleBufferBuilder roll(float roll) {
        if (expectFormat(ParticleVertexFormat.ELEMENT_ROLL)) {
            this.putFloat(0, roll);
            this.nextElement();
        }
        return this;
    }
}
