package io.github.toapuro.derendered.api.config.registry;

import net.minecraft.network.chat.Component;

import java.util.Optional;

public interface IOptionFlagsValidator {

    Optional<Component> checkError(String name, boolean flag);
}
