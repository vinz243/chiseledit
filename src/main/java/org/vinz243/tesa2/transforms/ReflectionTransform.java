package org.vinz243.tesa2.transforms;

import org.vinz243.tesa2.annotations.Coordinates;
import org.vinz243.tesa2.annotations.Direction;
import org.vinz243.tesa2.annotations.InstantiableTransform;
import org.vinz243.tesa2.helpers.Matrix;
import org.vinz243.tesa2.helpers.Vector;

public class ReflectionTransform extends SimpleAffineTransform {

    private final Matrix matrix;
    private final Vector offset;

    @InstantiableTransform
    public ReflectionTransform(@Direction Vector direction, @Coordinates Vector pos) {
        if (direction.getX() > direction.getZ()) {
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
    }

    @Override
    Matrix getTransform() {
        return matrix;
    }

    @Override
    Vector getOffset() {
        return offset;
    }
}
