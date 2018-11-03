package org.vinz243.tesa2.transforms;

import org.vinz243.tesa2.annotations.Coordinates;
import org.vinz243.tesa2.annotations.InstantiableTransform;
import org.vinz243.tesa2.annotations.Source;
import org.vinz243.tesa2.helpers.Matrix;
import org.vinz243.tesa2.helpers.Vector;
import org.vinz243.tesa2.helpers.YRotationMatrix;

public class YRevolutionTransform extends MultipleAffineTransform {

    private Vector axis;

    @InstantiableTransform
    public YRevolutionTransform(@Coordinates(from = Source.Player) Vector axis) {
        this.axis = axis.multiply(1, 0, 1);
    }

    @Override
    Vector getOffset(int i) {
        return this.axis;
    }

    @Override
    Matrix getMatrix(int i) {
        return new YRotationMatrix((i + 1) * Math.PI / 2);
    }

    @Override
    Matrix getChiselMatrix(int i) {
        return new YRotationMatrix((i + 1) * Math.PI / 2);
    }

    @Override
    int getIterations() {
        return 3;
    }
}
