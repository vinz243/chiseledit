package org.vinz243.tesa.transforms;

import org.vinz243.tesa.annotations.Coordinates;
import org.vinz243.tesa.annotations.InstantiableTransform;
import org.vinz243.tesa.annotations.Source;
import org.vinz243.tesa.helpers.Matrix;
import org.vinz243.tesa.helpers.Vector;
import org.vinz243.tesa.helpers.YRotationMatrix;

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

    @Override
    public String toString() {
        return "YRevolutionTransform{" +
                "axis=" + axis +
                '}';
    }
}
