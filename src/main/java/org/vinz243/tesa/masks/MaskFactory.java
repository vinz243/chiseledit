package org.vinz243.tesa.masks;

import org.vinz243.tesa.helpers.Vector;

public class MaskFactory {
    private Mask mask;

    private MaskFactory(Mask mask) {
        this.mask = mask;
    }

    public MaskFactory() {
        this(new VoidMask());
    }

    public MaskFactory set(Mask mask) {
        this.mask = mask;
        return this;
    }

    public MaskFactory set(Vector a, Vector b) {
        return set(new FlatMask(a, b));
    }


    public MaskFactory add(Mask mask) {
        this.mask = new AddMask(this.mask, mask);
        return this;
    }

    public MaskFactory add(Vector a, Vector b) {
        return add(new FlatMask(a, b));
    }

    public MaskFactory sub(Mask other) {
        this.mask = new SubMask(this.mask, mask);
        return this;
    }

    public MaskFactory sub(Vector a, Vector b) {
        return sub(new FlatMask(a, b));
    }

    public MaskFactory intersection(Mask other) {
        this.mask = new IntersectionMask(this.mask, mask);
        return this;
    }

    public MaskFactory intersection(Vector a, Vector b) {
        return intersection(new FlatMask(a, b));
    }

    public Mask get() {
        return mask;
    }
}
