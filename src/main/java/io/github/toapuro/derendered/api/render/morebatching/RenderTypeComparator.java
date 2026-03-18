package io.github.toapuro.derendered.api.render.morebatching;

import net.minecraft.client.renderer.RenderType;

import java.util.Objects;

public final class RenderTypeComparator {

    @SuppressWarnings("RedundantIfStatement")
    public static boolean contentEquals(RenderType a, RenderType b) {
        if(Objects.equals(a, b)) return true;

        if(a.format() != b.format()) return false;
        if(a.mode() != b.mode()) return false;
        if(a.affectsCrumbling() != b.affectsCrumbling()) return false;
        if(a.sortOnUpload != b.sortOnUpload) return false;
        if(a.getChunkLayerId() != b.getChunkLayerId()) return false;
        if(a.getClass() != b.getClass()) return false;

        if(a instanceof RenderType.CompositeRenderType compositeA &&
            b instanceof RenderType.CompositeRenderType compositeB &&
            !Objects.equals(compositeA.state.states, compositeB.state.states)) {
            return false;
        }
        return true;
    }
}
