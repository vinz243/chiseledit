package org.vinz243.tesa.transforms;

import org.vinz243.tesa.helpers.Matrix;
import org.vinz243.tesa.helpers.TransformResult;
import org.vinz243.tesa.helpers.Vector;
import org.vinz243.tesa.masks.MaskFactory;

import java.util.Collections;

public abstract class SimpleAffineTransform implements Transform {

    private final MaskFactory maskFactory = new MaskFactory();

    @Override
    public Iterable<TransformResult> apply(Vector in) {
        return Collections.singleton(new TransformResult(getTransform().multiply(in.subtract(getOffset())).add(getOffset()), in, getChiselTransform()));
    }

    abstract Matrix getTransform();

    abstract Vector getOffset();

    Matrix getChiselTransform() {
        return Matrix.IDENTITY_3;
    }

    @Override
    public MaskFactory getMaskFactory() {
        return maskFactory;
    }
}
