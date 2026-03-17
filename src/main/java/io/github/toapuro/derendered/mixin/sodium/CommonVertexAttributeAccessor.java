package io.github.toapuro.derendered.mixin.sodium;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.caffeinemc.mods.sodium.api.vertex.attributes.CommonVertexAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = CommonVertexAttribute.class, remap = false)
public interface CommonVertexAttributeAccessor {

    @Invoker("<init>")
    static CommonVertexAttribute createCommonVertexElement(String name, int ordinal, VertexFormatElement element) {
        throw new AssertionError();
    }
}
