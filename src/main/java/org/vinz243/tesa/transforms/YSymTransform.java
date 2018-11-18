package org.vinz243.tesa.transforms;

import org.vinz243.tesa.annotations.Coordinates;
import org.vinz243.tesa.annotations.InstantiableTransform;
import org.vinz243.tesa.annotations.Source;
import org.vinz243.tesa.helpers.Matrix;
import org.vinz243.tesa.helpers.Vector;
import org.vinz243.tesa.visu.AxisVisualizer;
import org.vinz243.tesa.visu.Visualizer;

public class YSymTransform extends MultipleAffineTransform {

    private final Vector axis;
    private final Matrix matrix;

    @InstantiableTransform
    public YSymTransform(
            @Coordinates(from = Source.Player) Vector axis
    ) {
        this.axis = axis.multiply(1, 0, 1);
        this.matrix = new Matrix(
                -1, 0, 0,
                0, 1, 0,
                0, 0, -1
        );
    }

    @Override
    Vector getOffset(int i) {
        return this.axis;
    }

    @Override
    Matrix getMatrix(int i) {
        return matrix;
    }

    @Override
    Matrix getChiselMatrix(int i) {
        return matrix;
    }

    @Override
    public String toString() {
        return "YSymTransform{" +
                "axis=" + axis +
                ", matrix=" + matrix +
                '}';
    }

    @Override
    public Visualizer getVisualizer() {
        return new AxisVisualizer(axis);
    }

    @Override
    int getIterations() {
        return 1;
    }
}
