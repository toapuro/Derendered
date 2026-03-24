package io.github.toapuro.derendered.mixin.sodium;

import io.github.toapuro.derendered.api.render.instancing.particle.ParticleVertexFormat;
import io.github.toapuro.derendered.libs.embeddium.CommonVertexAttributesExt;
import net.caffeinemc.mods.sodium.api.vertex.attributes.CommonVertexAttribute;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.*;

@Pseudo
@Mixin(value = CommonVertexAttribute.class, remap = false)
public class MixinCommonVertexAttribute {
    @Mutable
    @Shadow @Final
    public static int COUNT;

    @Mutable
    @Shadow @Final
    private static CommonVertexAttribute[] $VALUES;

    static {
        int ordinal = $VALUES.length;
        CommonVertexAttributesExt.BYTE_UV0 = CommonVertexAttributeAccessor.createCommonVertexElement("BYTE_UV0", ordinal++, ParticleVertexFormat.ELEMENT_BYTE_UV0);
        CommonVertexAttributesExt.LOCAL_UV0 = CommonVertexAttributeAccessor.createCommonVertexElement("LOCAL_UV0", ordinal++, ParticleVertexFormat.ELEMENT_LOCAL_UV0);
        CommonVertexAttributesExt.SIZE = CommonVertexAttributeAccessor.createCommonVertexElement("SIZE", ordinal++, ParticleVertexFormat.ELEMENT_SIZE);
        CommonVertexAttributesExt.ROLL = CommonVertexAttributeAccessor.createCommonVertexElement("ROLL", ordinal++, ParticleVertexFormat.ELEMENT_ROLL);
        $VALUES = ArrayUtils.addAll($VALUES,
                CommonVertexAttributesExt.BYTE_UV0,
                CommonVertexAttributesExt.LOCAL_UV0,
                CommonVertexAttributesExt.SIZE,
                CommonVertexAttributesExt.ROLL
        );
        COUNT = $VALUES.length;
    }
}
