package io.github.toapuro.derendered.mixin.queryculling;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.toapuro.derendered.api.config.ModConfig;
import io.github.toapuro.derendered.api.config.RuntimeOption;
import io.github.toapuro.derendered.api.render.queryculling.IMutableQueryBox;
import io.github.toapuro.derendered.api.render.queryculling.IQueryHolder;
import io.github.toapuro.derendered.api.render.queryculling.OcclusionQueryCuller;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockEntityRenderDispatcher.class)
public class MixinBlockEntityRenderDispatcher {

    @WrapOperation(method = "setupAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderer;render(Lnet/minecraft/world/level/block/entity/BlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V"))
    private static <T extends BlockEntity> void render(BlockEntityRenderer<T> instance, T blockEntity, float v, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Operation<Void> original) {
        if(!ModConfig.get().getRuntime().isEnabled(RuntimeOption.OCCLUSION_CULLING)) {
            original.call(instance, blockEntity, v, poseStack, bufferSource, packedLight, packedOverlay);
            return;
        }

        IMutableQueryBox mutableQueryBox = (IMutableQueryBox) blockEntity;
        IQueryHolder holder = (IQueryHolder) blockEntity;

        OcclusionQueryCuller.render(
                holder.derendered$retrieveQuery(), mutableQueryBox, poseStack, bufferSource,
                source -> original.call(instance, blockEntity, v, poseStack, source, packedLight, packedOverlay)
        );
    }
}
