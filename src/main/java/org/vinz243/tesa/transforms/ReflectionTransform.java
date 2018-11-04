package org.vinz243.tesa.transforms;

import org.vinz243.tesa.annotations.Coordinates;
import org.vinz243.tesa.annotations.Direction;
import org.vinz243.tesa.annotations.InstantiableTransform;
import org.vinz243.tesa.helpers.Axis;
import org.vinz243.tesa.helpers.Matrix;
import org.vinz243.tesa.helpers.Vector;
import org.vinz243.tesa.visu.PlaneVisualizer;
import org.vinz243.tesa.visu.Visualizer;

public class ReflectionTransform extends SimpleAffineTransform {

    private final Matrix matrix;
    private final Vector offset;
    private final PlaneVisualizer visualizer;

    @InstantiableTransform
    public ReflectionTransform(@Direction Axis direction, @Coordinates Vector pos) {
        if (direction == Axis.X) {
            this.matrix = new Matrix(
                    -1, 0, 0,
                    0, 1, 0,
                    0, 0, 1
            );
        } else {
            this.matrix = new Matrix(
                    1, 0, 0,
                    0, 1, 0,
                    0, 0, -1
            );
        }

        this.offset = pos;
        this.visualizer = new PlaneVisualizer(direction, direction == Axis.X ? pos.getBlockX() : pos.getBlockZ());
    }

    @Override
    Matrix getTransform() {
        return matrix;
    }

    @Override
    Matrix getChiselTransform() {
        return matrix;
    }

    @Override
    Vector getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "ReflectionTransform{" +
                "matrix=" + matrix +
                ", offset=" + offset +
                '}';
    }

    @Override
    public Visualizer getVisualizer() {
        return this.visualizer;
    }
}
