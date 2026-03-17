package io.github.toapuro.derendered.api.context;

public final class ContextFrame<T> implements AutoCloseable {
    private final ContextStack<T> parent;
    private final ThreadLocal<T> object;

    public ContextFrame(ContextStack<T> parent, T object) {
        this.parent = parent;
        this.object = new ThreadLocal<>();
        this.object.set(object);
    }

    public T get() {
        return this.object.get();
    }

    @Override
    public void close() {
        this.parent.close(this);
    }
}
