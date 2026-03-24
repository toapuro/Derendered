package io.github.toapuro.derendered.api.render.queryculling;

import lombok.Getter;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

@Getter
public class OcclusionQuery {

    private final int id;

    public OcclusionQuery() {
        id = GL15.glGenQueries();
    }

    public void begin(int target) {
        GL15.glBeginQuery(target, id);
    }

    public void end(int target) {
        GL15.glEndQuery(target);
    }

    public void free() {
        GL15.glDeleteQueries(id);
    }

    public int getResultUInt() {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            long ptr = stack.nmalloc(4);
            GL15.glGetQueryObjectuiv(id, GL15.GL_QUERY_RESULT, ptr);
            return MemoryUtil.memGetInt(ptr);
        }
    }
}
