package org.vinz243.tesa.masks;

import org.vinz243.tesa.helpers.Vector;

public class VoidMask implements Mask {
    @Override
    public boolean test(Vector pos) {
        return true;
    }
}
