package org.vinz243.tesa2.transforms;

import org.vinz243.tesa2.helpers.TransformResult;
import org.vinz243.tesa2.helpers.Vector;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class MultipleTransform implements Transform {
    @Override
    public Iterable<TransformResult> apply(Vector in) {
        return IntStream.range(0, getIterations()).mapToObj(i -> transform(i, in)).collect(Collectors.toList());
    }

    abstract int getIterations();

    abstract TransformResult transform(int i, Vector in);
}
