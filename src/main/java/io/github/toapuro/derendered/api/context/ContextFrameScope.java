package io.github.toapuro.derendered.api.context;

import java.util.ArrayList;
import java.util.List;

public class ContextFrameScope implements AutoCloseable {

    private final List<ContextFrame<?>> contextFrames = new ArrayList<>();

    public void addFrame(ContextFrame<?> local) {
        contextFrames.add(local);
    }

    @Override
    public void close() {
        for (ContextFrame<?> contextFrame : contextFrames) {
            contextFrame.close();
        }
    }
}
