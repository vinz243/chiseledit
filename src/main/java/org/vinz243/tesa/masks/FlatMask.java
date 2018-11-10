package org.vinz243.tesa.masks;

import org.vinz243.tesa.helpers.Vector;

public class FlatMask implements Mask {
    private Vector min, max;

    public FlatMask(Vector min, Vector max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean test(Vector pos) {

        return pos.getX() <= Math.max(min.getX(), max.getX()) && pos.getX() >= Math.min(min.getX(), max.getX())
                && pos.getY() <= Math.max(min.getY(), max.getY()) && pos.getY() >= Math.min(min.getY(), max.getY())
                && pos.getZ() <= Math.max(min.getZ(), max.getZ()) && pos.getZ() >= Math.min(min.getZ(), max.getZ());
    }
}
