package io.github.toapuro.derendered.api.render.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

import java.util.List;

public class VertexFormatUtil {

    public static void setupBufferState(VertexFormat format, int indexOffset) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> _setupBufferState(format, indexOffset));
        } else {
            _setupBufferState(format, indexOffset);
        }
    }

    private static void _setupBufferState(VertexFormat format, int indexOffset) {
        int vertexSize = format.getVertexSize();
        List<VertexFormatElement> elements = format.getElements();

        for(int i = 0; i < elements.size(); ++i) {
            elements.get(i).setupBufferState(i + indexOffset, format.getOffset(i), vertexSize);
        }

    }
}
