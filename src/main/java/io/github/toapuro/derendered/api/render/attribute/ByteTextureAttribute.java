package io.github.toapuro.derendered.api.render.attribute;

import org.lwjgl.system.MemoryUtil;

public class ByteTextureAttribute {

    @SuppressWarnings("PointlessArithmeticExpression")
    public static void put(long ptr, byte u, byte v) {
        MemoryUtil.memPutByte(ptr + 0L, u);
        MemoryUtil.memPutByte(ptr + 1L, v);
    }
}
