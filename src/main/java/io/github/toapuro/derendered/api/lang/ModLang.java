package io.github.toapuro.derendered.api.lang;

import io.github.toapuro.derendered.Derendered;
import lombok.AllArgsConstructor;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;

@AllArgsConstructor
public enum ModLang {
    ;

    private final String type;
    private final String id;

    public Component component() {
        return Component.translatable(Derendered.MODID, Util.makeDescriptionId(type, Derendered.getResource(id)));
    }
}
