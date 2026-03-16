package io.github.toapuro.derendered.api.render.mem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.lwjgl.system.MemoryUtil;

@AllArgsConstructor
public class MemBufferCache {

    @Getter
    private final long size;
    private long ptr;

    public MemBufferCache(long initialSize) {
        this(initialSize, MemoryUtil.nmemAlloc(initialSize));
    }

    public void ensureSize(long newSize) {
        if(newSize > size) {
            ptr = MemoryUtil.nmemRealloc(ptr, newSize);
        }
    }

    public void dispose() {
        MemoryUtil.nmemFree(ptr);
    }

    public long ptr() {
        return ptr;
    }
}
