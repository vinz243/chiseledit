package org.vinz243.tesa.masks;

import org.vinz243.tesa.helpers.Vector;

public class SubMask extends ComposedMask {
    public SubMask(Mask a, Mask b) {
        super(a, b);
    }

    @Override
    public boolean test(Vector pos) {
        return a.test(pos) && !b.test(pos);
    }
}
