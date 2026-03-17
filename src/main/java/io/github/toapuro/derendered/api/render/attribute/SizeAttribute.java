package io.github.toapuro.derendered.api.render.attribute;

import org.lwjgl.system.MemoryUtil;

public final class SizeAttribute {

    public static void set(long ptr, float size) {
        MemoryUtil.memPutFloat(ptr, size);
    }
}
