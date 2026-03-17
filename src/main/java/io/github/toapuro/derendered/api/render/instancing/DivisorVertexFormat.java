package io.github.toapuro.derendered.api.render.instancing;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.lwjgl.opengl.GL33;

import java.util.List;

public record DivisorVertexFormat(List<VertexAttributeKey> divisors) {

    public void setup(VertexFormat vertexFormat, int indexOffset) {
        if(divisors.isEmpty()) return;

        var elements = vertexFormat.getElements();

        for (VertexAttributeKey divisor : divisors) {
            if(elements.get(divisor.location).equals(divisor.element)) {
                GL33.glVertexAttribDivisor(divisor.location + indexOffset, divisor.divisor);
            } else {
                throw new RuntimeException(String.format("VertexElement(%d) does not match", divisor.location));
            }
        }
    }

    @RequiredArgsConstructor(staticName = "of")
    @EqualsAndHashCode
    public static class VertexAttributeKey {
        private final VertexFormatElement element;
        private final int location;
        private final int divisor;
    }
}
