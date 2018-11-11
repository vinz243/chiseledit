package org.vinz243.tesa.transforms;

import org.vinz243.tesa.helpers.TransformResult;
import org.vinz243.tesa.helpers.Vector;
import org.vinz243.tesa.masks.MaskFactory;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class MultipleTransform implements Transform {
    private final MaskFactory outputMaskFactory = new MaskFactory();
    private final MaskFactory inputMaskFactory = new MaskFactory();

    @Override
    public Iterable<TransformResult> apply(Vector in) {
        return IntStream.range(0, getIterations()).mapToObj(i -> transform(i, in)).collect(Collectors.toList());
    }

    abstract int getIterations();

    abstract TransformResult transform(int i, Vector in);

    @Override
    public MaskFactory getInputMaskFactory() {
        return inputMaskFactory;
    }

    @Override
    public MaskFactory getOutputMaskFactory() {
        return outputMaskFactory;
    }

}
