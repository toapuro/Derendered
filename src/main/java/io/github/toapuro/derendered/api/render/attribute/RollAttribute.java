package io.github.toapuro.derendered.api.render.attribute;

import org.lwjgl.system.MemoryUtil;

public final class RollAttribute {

    public static void set(long ptr, byte roll) {
        MemoryUtil.memPutByte(ptr, roll);
    }
}
