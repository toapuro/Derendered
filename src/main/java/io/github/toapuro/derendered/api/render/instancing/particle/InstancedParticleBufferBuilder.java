package io.github.toapuro.derendered.api.render.instancing.particle;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormatElement;

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
            this.putShort(0, (short) (u0*65535f));
            this.putShort(2, (short) (v0*65535f));
            this.putShort(4, (short) (u1*65535f));
            this.putShort(6, (short) (v1*65535f));
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

    public InstancedParticleBufferBuilder size(float size) {
        if (expectFormat(ParticleVertexFormat.ELEMENT_SIZE)) {
            this.putFloat(0, size);
            this.nextElement();
        }
        return this;
    }

    public InstancedParticleBufferBuilder roll(float roll) {
        if (expectFormat(ParticleVertexFormat.ELEMENT_ROLL)) {
            this.putByte(0, (byte) (roll*255f));
            this.nextElement();
        }
        return this;
    }
}
