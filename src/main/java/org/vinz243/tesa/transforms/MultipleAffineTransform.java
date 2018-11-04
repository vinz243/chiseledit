package org.vinz243.tesa.transforms;

import org.vinz243.tesa.helpers.Matrix;
import org.vinz243.tesa.helpers.TransformResult;
import org.vinz243.tesa.helpers.Vector;

public abstract class MultipleAffineTransform extends MultipleTransform {

    abstract Vector getOffset(int i);

    abstract Matrix getMatrix(int i);

    Matrix getChiselMatrix(int i) {
        return Matrix.IDENTITY_3;
    }

    @Override
    TransformResult transform(int i, Vector in) {
        Vector offset = getOffset(i);
        return new TransformResult(
                getMatrix(i).multiply(in.subtract(offset)).add(offset),
                getChiselMatrix(i)
        );
    }
}
