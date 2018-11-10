package org.vinz243.tesa.transforms;

import org.vinz243.tesa.annotations.Coordinates;
import org.vinz243.tesa.annotations.InstantiableTransform;
import org.vinz243.tesa.annotations.IntParam;
import org.vinz243.tesa.annotations.Source;
import org.vinz243.tesa.helpers.Matrix;
import org.vinz243.tesa.helpers.Vector;
import org.vinz243.tesa.helpers.YRotationMatrix;
import org.vinz243.tesa.visu.AxisVisualizer;
import org.vinz243.tesa.visu.Visualizer;

public class YRevolutionTransform extends MultipleAffineTransform {

    private Vector axis;
    private int count;

    @InstantiableTransform
    public YRevolutionTransform(
            @Coordinates(from = Source.Player) Vector axis,
            @IntParam(required = false, defaultValue = 3, min = 1, max = 3) int count
    ) {
        this.axis = axis.multiply(1, 0, 1);
        this.count = count;
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
        return count;
    }

    @Override
    public String toString() {
        return "YRevolutionTransform{" +
                "axis=" + axis +
                '}';
    }

    @Override
    public Visualizer getVisualizer() {
        return new AxisVisualizer(axis);
    }
}
