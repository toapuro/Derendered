package io.github.toapuro.derendered.api.render.queryculling.buffer;

import com.mojang.blaze3d.vertex.BufferBuilder;

@FunctionalInterface
public interface BufferFlushListener {
    void onFlush(BufferBuilder.RenderedBuffer builder);
}
