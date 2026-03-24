package io.github.toapuro.derendered.api.render.queryculling;

import io.github.toapuro.derendered.api.render.queryculling.buffer.BufferFlushListener;
import lombok.Getter;

import java.util.LinkedList;

public class BufferUploaderHost {

    @Getter
    private static final LinkedList<BufferFlushListener> flushListeners = new LinkedList<>();

    public static void pushFlushListener(BufferFlushListener listener) {
        flushListeners.push(listener);
    }

    public static void popFlushListener(BufferFlushListener listener) {
        if(flushListeners.peekFirst() == listener) {
            flushListeners.pop();
        }
    }
}
