package io.github.toapuro.derendered.api.render.queryculling;

import org.joml.Vector3f;

public record Boxf(Vector3f minVec, Vector3f maxVec) {

    public static Boxf ZERO = new Boxf(new Vector3f(), new Vector3f());

    public boolean isPoint() {
        return minVec.equals(maxVec);
    }

    public boolean isInvalid() {
        return minVec.x > maxVec.x || minVec.y > maxVec.y || minVec.z > maxVec.z;
    }

    public Vector3f getScale() {
        return maxVec.get(new Vector3f()).sub(minVec).max(new Vector3f());
    }

    public void expand(Boxf boxf) {
        minVec.min(boxf.minVec);
        maxVec.max(boxf.maxVec);
    }
}
