package io.github.toapuro.derendered.api.render.attribute;

import org.lwjgl.system.MemoryUtil;

public final class ShortTextureRangeAttribute {

    @SuppressWarnings("PointlessArithmeticExpression")
    public static void put(long ptr, short u0, short v0, short u1, short v1) {
        MemoryUtil.memPutShort(ptr + 0L, u0);
        MemoryUtil.memPutShort(ptr + 2L, v0);
        MemoryUtil.memPutShort(ptr + 4L, u1);
        MemoryUtil.memPutShort(ptr + 6L, v1);
    }
}
