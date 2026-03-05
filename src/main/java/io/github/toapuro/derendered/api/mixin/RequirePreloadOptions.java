package io.github.toapuro.derendered.api.mixin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePreloadOptions {
    RequirePreloadOption[] value();
}
