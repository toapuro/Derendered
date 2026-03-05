package io.github.toapuro.derendered.api.render;

import com.mojang.blaze3d.vertex.VertexFormatElement;

import java.util.Map;

public record DivisorVertexFormat(Map<VertexFormatElement, Integer> divisors) {
}
