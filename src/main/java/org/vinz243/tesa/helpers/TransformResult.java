package org.vinz243.tesa.helpers;

public class TransformResult {
    private final Vector vector, input;
    private final Matrix chiselTransform;

    public TransformResult(Vector vector, Vector input, Matrix chiselTransform) {
        this.vector = vector;
        this.input = input;
        this.chiselTransform = chiselTransform;
    }

    public Vector getVector() {
        return vector;
    }

    public Matrix getChiselTransform() {
        return chiselTransform;
    }

    public Vector getInput() {
        return input;
    }
}
