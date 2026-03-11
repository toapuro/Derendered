package io.github.toapuro.derendered.api.config;

import io.github.toapuro.derendered.lang.DerenderedLang;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public interface IOption {
    String getId();
    DerenderedLang getLang();
    boolean isDefaultEnabled();
    Optional<Component> validate(boolean flag);
}
