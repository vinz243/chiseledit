package org.vinz243.tesa.transforms;

import org.vinz243.tesa.helpers.TransformResult;
import org.vinz243.tesa.helpers.Vector;
import org.vinz243.tesa.visu.Visualizer;

public interface Transform {
    Iterable<TransformResult> apply(Vector in);

    Visualizer getVisualizer();
}
