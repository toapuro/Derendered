package io.github.toapuro.derendered.api.context;

import com.mojang.blaze3d.vertex.VertexBuffer;

import java.util.function.Consumer;

public class MixinContexts {
    public static final ContextStack<Consumer<VertexBuffer>> GL_DRAW_FUNCTION = new ContextStack<>();
}
