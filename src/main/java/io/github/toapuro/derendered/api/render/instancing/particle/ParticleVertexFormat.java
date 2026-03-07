package io.github.toapuro.derendered.api.render.instancing.particle;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import io.github.toapuro.derendered.api.render.instancing.DivisorVertexFormat;
import io.github.toapuro.derendered.api.render.instancing.DivisorVertexFormat.VertexAttributeKey;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.*;

public class ParticleVertexFormat {
    public static final VertexFormatElement ELEMENT_ALPHA = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC, 1);
    public static final VertexFormatElement ELEMENT_INSTANCE_POS = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.POSITION, 3);
    public static final VertexFormatElement ELEMENT_QUATERNION = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC, 4);
    public static final VertexFormatElement ELEMENT_SIZE = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC, 1);

    public static final VertexFormat PARTICLE_VBO = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
            .put("Position", ELEMENT_POSITION)
            .put("UV0", ELEMENT_UV0)
            .put("Color", ELEMENT_COLOR)
            .build());

    public static final VertexFormat PARTICLE_ARRAY = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
            .put("UV2", ELEMENT_UV2) // packedLight
            .put("Alpha", ELEMENT_ALPHA)
            .put("InstancePos", ELEMENT_INSTANCE_POS)
            .put("Quaternion", ELEMENT_QUATERNION)
            .put("Size", ELEMENT_SIZE)
            .build());

    public static final DivisorVertexFormat PARTICLE_ARRAY_DIVS = new DivisorVertexFormat(ImmutableList.<VertexAttributeKey>builder()
            .add(VertexAttributeKey.of(ELEMENT_UV2, 0, 1))
            .add(VertexAttributeKey.of(ELEMENT_ALPHA, 1, 1))
            .add(VertexAttributeKey.of(ELEMENT_INSTANCE_POS, 2, 1))
            .add(VertexAttributeKey.of(ELEMENT_QUATERNION, 3, 1))
            .add(VertexAttributeKey.of(ELEMENT_SIZE, 4, 1))
            .build());


    public static final VertexFormat PARTICLE_FULL = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
            .putAll(PARTICLE_VBO.getElementMapping())
            .putAll(PARTICLE_ARRAY.getElementMapping())
            .build());
}
