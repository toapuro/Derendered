package io.github.toapuro.derendered.api.config.option;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record ErrorResult(@Nullable Component errorComponent, Optional<Boolean> flagModification) {

    public static ErrorResult keep(@Nullable Component errorComponent) {
        return new ErrorResult(errorComponent, Optional.empty());
    }

    public static ErrorResult forceTrue(@Nullable Component errorComponent) {
        return new ErrorResult(errorComponent, Optional.of(true));
    }

    public static ErrorResult forceFalse(@Nullable Component errorComponent) {
        return new ErrorResult(errorComponent, Optional.of(false));
    }
}
