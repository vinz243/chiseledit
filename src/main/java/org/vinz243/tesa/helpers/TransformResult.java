package org.vinz243.tesa.helpers;

public class TransformResult {
    private final Vector vector, input;
    private final Matrix chiselTransform;
    private boolean masked;

    public TransformResult(Vector vector, Vector input, Matrix chiselTransform) {
        this.vector = vector;
        this.input = input;
        this.chiselTransform = chiselTransform;
    }

    public TransformResult(TransformResult a, TransformResult b) {
        this.vector = a.getVector();
        this.input = a.getInput();
        this.chiselTransform = a.getChiselTransform();
        this.masked = a.masked || b.masked;
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

    @Override
    public String toString() {
        return "TransformResult{" +
                "vector=" + vector +
                ", input=" + input +
                ", chiselTransform=" + chiselTransform +
                '}';
    }

    public boolean isMasked() {
        return masked;
    }

    public void setMasked(boolean masked) {
        this.masked = masked;
    }
}
