package org.vinz243.tesa.masks;

public abstract class ComposedMask implements Mask {
    Mask a, b;

    public ComposedMask(Mask a, Mask b) {
        this.a = a;
        this.b = b;
    }
}
