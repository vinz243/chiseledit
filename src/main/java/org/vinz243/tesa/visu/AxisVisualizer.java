package org.vinz243.tesa.visu;

import org.vinz243.tesa.helpers.Vector;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AxisVisualizer implements Visualizer {
    private final Vector axis;

    public AxisVisualizer(Vector axis) {
        this.axis = axis;
    }

    @Override
    public Iterable<Vector> getVertices(Vector min, Vector max) {
        return IntStream.range(min.getBlockY(), max.getBlockY())
                .mapToObj(y -> this.axis.add(new Vector(.5, y, .5)))
                .collect(Collectors.toList());
    }
}
