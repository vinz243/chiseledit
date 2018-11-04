package org.vinz243.tesa.visu;

import org.vinz243.tesa.helpers.Vector;

public interface Visualizer {
    Iterable<Vector> getVertices(Vector min, Vector max);
}
