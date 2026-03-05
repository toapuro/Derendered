package io.github.toapuro.derendered.api.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.*;

public class ParticleVertexFormat {
    public static final VertexFormatElement ELEMENT_INSTANCE_POS = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.POSITION, 3);
    public static final VertexFormatElement ELEMENT_QUATERNION = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC, 4);

    public static final VertexFormat PARTICLE_INSTANCED = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
            .put("Position", ELEMENT_POSITION)
            .put("UV0", ELEMENT_UV0)
            .put("Color", ELEMENT_COLOR)
            .put("UV2", ELEMENT_UV2)
            .put("InstancePos", ELEMENT_INSTANCE_POS)
            .put("Quaternion", ELEMENT_QUATERNION)
            .build());

    public static final VertexFormat PARTICLE_INSTANCED_EXTRAS = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
            .put("UV2", ELEMENT_UV2) // packedLight
            .put("InstancePos", ELEMENT_INSTANCE_POS)
            .put("Quaternion", ELEMENT_QUATERNION)
            .build());

    public static final DivisorVertexFormat PARTICLE_INSTANCED_EXTRAS_DIVS = new DivisorVertexFormat(ImmutableMap.<VertexFormatElement, Integer>builder()
            .put(ELEMENT_UV2, 0)
            .put(ELEMENT_INSTANCE_POS, 0)
            .put(ELEMENT_QUATERNION, 0)
            .build());
}
