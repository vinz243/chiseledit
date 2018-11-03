package org.vinz243.tesa2.transforms;

import org.vinz243.tesa2.helpers.Matrix;
import org.vinz243.tesa2.helpers.TransformResult;
import org.vinz243.tesa2.helpers.Vector;

import java.util.Collections;

public abstract class SimpleAffineTransform implements Transform {

    @Override
    public Iterable<TransformResult> apply(Vector in) {
        return Collections.singleton(new TransformResult(getTransform().multiply(in.subtract(getOffset())).add(getOffset()), getChiselTransform()));
    }

    abstract Matrix getTransform();

    abstract Vector getOffset();

    Matrix getChiselTransform() {
        return Matrix.IDENTITY_3;
    }
}
