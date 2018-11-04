package org.vinz243.tesa.helpers;

public class TransformResult {
    private final Vector vector;
    private final Matrix chiselTransform;

    public TransformResult(Vector vector, Matrix chiselTransform) {
        this.vector = vector;
        this.chiselTransform = chiselTransform;
    }

    public Vector getVector() {
        return vector;
    }

    public Matrix getChiselTransform() {
        return chiselTransform;
    }
}
