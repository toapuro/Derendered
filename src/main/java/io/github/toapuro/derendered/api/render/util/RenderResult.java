package io.github.toapuro.derendered.api.render.util;

public enum RenderResult {
    SUCCESS,
    PASS,
    FAILURE;

    public boolean successful() {
        return this == SUCCESS;
    }

    public boolean failure() {
        return this == FAILURE;
    }
}
