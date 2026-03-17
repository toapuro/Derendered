package io.github.toapuro.derendered.api.render.buffer;

import com.mojang.blaze3d.platform.GlStateManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;

@RequiredArgsConstructor
@Getter
public final class GpuBuffer {

    private final int bufferId = GlStateManager._glGenBuffers();
    private final Usage usage;

    public void upload(int target, ByteBuffer buffer) {
        GlStateManager._glBufferData(target, buffer, usage.id);
    }

    public void bind(int target) {
        GlStateManager._glBindBuffer(target, this.bufferId);
    }

    public void release() {
        GlStateManager._glDeleteBuffers(this.bufferId);
    }

    @OnlyIn(Dist.CLIENT)
    @Getter
    public enum Usage {
        STATIC(GL20.GL_STATIC_DRAW),
        DYNAMIC(GL20.GL_DYNAMIC_DRAW);

        private final int id;

        Usage(int pId) {
            this.id = pId;
        }
    }
}
