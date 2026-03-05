package io.github.toapuro.derendered.api.mixin;

import io.github.toapuro.derendered.api.config.PreloadOption;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RequirePreloadOptions.class)
public @interface RequirePreloadOption {
    PreloadOption value();
}
