package org.vinz243.tesa.transforms;

import org.vinz243.tesa.helpers.TransformResult;
import org.vinz243.tesa.helpers.Vector;

public interface Transform {
    Iterable<TransformResult> apply(Vector in);
}
