package io.github.toapuro.derendered.api.render.buffer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.lwjgl.system.MemoryUtil;

@AllArgsConstructor
public final class NativeMemBuffer {

    @Getter
    private final long size;
    private long ptr;

    public NativeMemBuffer(long initialSize) {
        this(initialSize, MemoryUtil.nmemAlloc(initialSize));
    }

    public void ensureSize(long newSize) {
        if(newSize > size) {
            ptr = MemoryUtil.nmemRealloc(ptr, newSize);
        }
    }

    public void free() {
        MemoryUtil.nmemFree(ptr);
    }

    public long ptr() {
        return ptr;
    }
}
