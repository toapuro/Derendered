package io.github.toapuro.derendered.api.render.instancing.particle;

import io.github.toapuro.derendered.api.render.attribute.ByteTextureAttribute;
import io.github.toapuro.derendered.api.render.attribute.RollAttribute;
import io.github.toapuro.derendered.api.render.attribute.ShortTextureRangeAttribute;
import io.github.toapuro.derendered.api.render.attribute.SizeAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.ColorAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.LightAttribute;
import net.caffeinemc.mods.sodium.api.vertex.attributes.common.PositionAttribute;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatRegistry;

public class InstancedParticleVertex {

    public static final VertexFormatDescription VBO_FORMAT = VertexFormatRegistry.instance().get(ParticleVertexFormat.PARTICLE_VBO);
    public static final VertexFormatDescription INSTANCE_FORMAT = VertexFormatRegistry.instance().get(ParticleVertexFormat.PARTICLE_ARRAY);
    public static final int VBO_STRIDE = 18;
    public static final int INSTANCE_STRIDE = 29;

    @SuppressWarnings("PointlessArithmeticExpression")
    public static void putVBO(long ptr, float x, float y, float z, byte u, byte v, int light) {
        PositionAttribute.put(ptr + 0L, x, y, z); // 12
        ByteTextureAttribute.put(ptr + 12L, u, v); // 2
        LightAttribute.set(ptr + 14L, light); // 4
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    public static void putInstance(long ptr, float x, float y, float z, int color, short localU0, short localV0, short localU1, short localV1, float size, byte roll) {
        PositionAttribute.put(ptr + 0L, x, y, z); // 12
        ColorAttribute.set(ptr + 12L, color); // 4
        ShortTextureRangeAttribute.put(ptr + 16L, localU0, localV0, localU1, localV1); // 8
        SizeAttribute.set(ptr + 24L, size); // 4
        RollAttribute.set(ptr + 28L, roll); // 1
    }
}
