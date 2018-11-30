package org.vinz243.tesa.masks;

import org.vinz243.tesa.helpers.Vector;

public class ComposedMaskFactory extends MaskFactory {

    private MaskFactory first;
    private MaskFactory second;

    public ComposedMaskFactory(MaskFactory first, MaskFactory second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public MaskFactory set(Mask mask) {
        first.set(mask);
        second.set(mask);

        return this;
    }

    @Override
    public MaskFactory set(Vector a, Vector b) {
        return this.set(new FlatMask(a, b));
    }

    @Override
    public MaskFactory add(Mask mask) {
        first.add(mask);
        second.add(mask);

        return this;
    }

    @Override
    public MaskFactory add(Vector a, Vector b) {
        return this.add(new FlatMask(a, b));
    }

    @Override
    public MaskFactory sub(Mask other) {
        first.sub(other);
        second.sub(other);

        return this;
    }

    @Override
    public MaskFactory sub(Vector a, Vector b) {
        return this.sub(new FlatMask(a, b));
    }

    @Override
    public MaskFactory intersection(Mask other) {
        first.intersection(other);
        second.intersection(other);

        return this;
    }

    @Override
    public MaskFactory intersection(Vector a, Vector b) {
        return this.intersection(new FlatMask(a, b));
    }

    @Override
    public Mask get() {
        return new IntersectionMask(first.get(), second.get());
    }
}
