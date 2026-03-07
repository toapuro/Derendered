package io.github.toapuro.derendered.api.render.util;

public enum RenderResult {
    SUCCESS,
    PASS,
    FAILURE;

    public boolean success() {
        return this == SUCCESS;
    }

    public boolean pass() {
        return this == SUCCESS || this == PASS;
    }

    public boolean failure() {
        return this == FAILURE;
    }
}
