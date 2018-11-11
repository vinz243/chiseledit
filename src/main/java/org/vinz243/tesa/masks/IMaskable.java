package org.vinz243.tesa.masks;

public interface IMaskable {
    MaskFactory getInputMaskFactory();

    MaskFactory getOutputMaskFactory();
}
