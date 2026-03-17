package io.github.toapuro.derendered.api.render.util;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class TextureAtlasSpriteUtil {

    public static float getSpriteU(TextureAtlasSprite sprite, float atlasU) {
        return (atlasU - sprite.getU0()) / (sprite.getU1() - sprite.getU0());
    }

    public static float getSpriteV(TextureAtlasSprite sprite, float atlasV) {
        return (atlasV - sprite.getV0()) / (sprite.getV1() - sprite.getV0());
    }
}
