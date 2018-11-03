package org.vinz243.tesa2.transforms;

import org.vinz243.tesa2.helpers.TransformResult;
import org.vinz243.tesa2.helpers.Vector;

public interface Transform {
    Iterable<TransformResult> apply(Vector in);
}
