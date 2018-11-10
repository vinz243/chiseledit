package org.vinz243.tesa.helpers;

import org.vinz243.tesa.masks.IMaskable;
import org.vinz243.tesa.masks.MaskFactory;

public class MaskableContainer implements IMaskable {
    private final MaskFactory factory = new MaskFactory();

    @Override
    public MaskFactory getMaskFactory() {
        return factory;
    }
}
