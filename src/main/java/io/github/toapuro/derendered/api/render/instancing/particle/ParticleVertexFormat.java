package io.github.toapuro.derendered.api.render.instancing.particle;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import io.github.toapuro.derendered.api.render.instancing.DivisorVertexFormat;
import io.github.toapuro.derendered.api.render.instancing.DivisorVertexFormat.VertexAttributeKey;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.*;

public class ParticleVertexFormat {
    // Normalized (Accuracy 1/255)
    public static final VertexFormatElement ELEMENT_LOCAL_UV0 = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.NORMAL, 4);
    public static final VertexFormatElement ELEMENT_INSTANCE_POS = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.POSITION, 3);
    public static final VertexFormatElement ELEMENT_QUATERNION = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC, 4);
    public static final VertexFormatElement ELEMENT_SIZE = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC, 1);
    public static final VertexFormatElement ELEMENT_ROLL = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.GENERIC, 1);

    public static final VertexFormat PARTICLE_VBO = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
            .put("Position", ELEMENT_POSITION)
            .put("UV0", ELEMENT_UV0)
            .put("UV2", ELEMENT_UV2) // packedLight
            .build());

    public static final VertexFormat PARTICLE_ARRAY = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
            .put("Color", ELEMENT_COLOR)
            .put("LocalUV0", ELEMENT_LOCAL_UV0)
            .put("InstancePos", ELEMENT_INSTANCE_POS)
            .put("Size", ELEMENT_SIZE)
            .put("Roll", ELEMENT_ROLL)
            .build());

    public static final DivisorVertexFormat PARTICLE_ARRAY_DIVS = new DivisorVertexFormat(ImmutableList.<VertexAttributeKey>builder()
            .add(VertexAttributeKey.of(ELEMENT_COLOR,0, 1))
            .add(VertexAttributeKey.of(ELEMENT_LOCAL_UV0,1, 1))
            .add(VertexAttributeKey.of(ELEMENT_INSTANCE_POS,2, 1))
            .add(VertexAttributeKey.of(ELEMENT_SIZE,3, 1))
            .add(VertexAttributeKey.of(ELEMENT_ROLL,4, 1))
            .build());


    public static final VertexFormat PARTICLE_FULL = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
            .putAll(PARTICLE_VBO.getElementMapping())
            .putAll(PARTICLE_ARRAY.getElementMapping())
            .build());
}
