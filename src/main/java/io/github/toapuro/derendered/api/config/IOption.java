package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.lang.DerenderedLang;
import net.minecraft.network.chat.Component;

public interface IOption {
    String getId();
    DerenderedLang getLang();
    boolean isDefaultEnabled();
    Component getDescription();
}
