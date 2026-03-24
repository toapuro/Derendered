package io.github.toapuro.derendered.mixin.queryculling;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.toapuro.derendered.api.render.queryculling.Boxf;
import io.github.toapuro.derendered.api.render.queryculling.IMutableQueryBox;
import io.github.toapuro.derendered.api.render.queryculling.IQueryHolder;
import io.github.toapuro.derendered.api.render.queryculling.OcclusionQuery;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(BlockEntity.class)
public class MixinBlockEntity implements IMutableQueryBox, IQueryHolder {

    @Shadow
    @Nullable
    protected Level level;
    @Unique
    private Boxf derendered$box = new Boxf(new Vector3f(), new Vector3f());

    @Unique
    private OcclusionQuery derendered$query;

    @Override
    public Boxf derendered$getQueryBox() {
        return derendered$box;
    }

    @Override
    public void derendered$setQueryBox(Boxf boxf) {
        this.derendered$box = boxf;
    }

    @Override
    public OcclusionQuery derendered$retrieveQuery() {
        if(derendered$query == null) {
            derendered$query = new OcclusionQuery();
        }

        return derendered$query;
    }

    @Inject(method = "setRemoved", at = @At("HEAD"))
    public void setRemoved(CallbackInfo ci) {
        if(level == null || !level.isClientSide) return;
        RenderSystem.recordRenderCall(() -> derendered$retrieveQuery().free());
    }
}
