#version 150

#moj_import <fog.glsl>

in vec3 Position;
in vec2 UV0;
in ivec2 UV2;

in vec4 Color;
in vec4 LocalUV0;
in vec3 InstancePos;
in float Size;
in float Roll;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform int FogShape;
uniform vec2 SpriteUV0;
uniform vec2 SpriteUV1;

out vec3 tf_Position;
out vec2 tf_UV0;
out vec4 tf_Color;
out ivec2 tf_UV2;

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
    tf_Position = rotateZ((Position * Size) + InstancePos, Roll);

    vec2 localUV = LocalUV0.xy + (LocalUV0.zw - LocalUV0.xy) * UV0;
    tf_UV0 = SpriteUV0 + localUV * (SpriteUV1 - SpriteUV0);

    tf_Color = Color;
    tf_UV2 = UV2;
}
