#version 150

#moj_import <fog.glsl>

in vec3 Position;
in uvec2 UV0;
in ivec2 UV2;

in vec3 InstancePos;
in vec4 Color;
in vec4 LocalUV0;
in float Size;
in float Roll;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform int FogShape;
uniform vec2 SpriteUV0;
uniform vec2 SpriteUV1;

out float vertexDistance;
out vec2 texCoord0;
out vec4 vertexColor;

vec3 rotateZ(vec3 v, float angle) {
    float c = cos(angle);
    float s = sin(angle);
    return vec3(
        v.x * c - v.y * s,
        v.x * s + v.y * c,
        v.z
    );
}

void main() {
    vec3 vertPosition = rotateZ((Position * Size) + InstancePos, Roll);

    gl_Position = ProjMat * ModelViewMat * vec4(vertPosition, 1.0);

    vertexDistance = fog_distance(ModelViewMat, vertPosition, FogShape);

    vec2 localUV = LocalUV0.xy + (LocalUV0.zw - LocalUV0.xy) * UV0;
    texCoord0 = SpriteUV0 + localUV * (SpriteUV1 - SpriteUV0);

    vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);
}
