package org.vinz243.tesa.visu;

import org.vinz243.tesa.helpers.Axis;
import org.vinz243.tesa.helpers.Vector;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlaneVisualizer implements Visualizer {

    private final Axis axis;
    private final int coordinate;

    public PlaneVisualizer(Axis axis, int coordinate) {
        this.axis = axis;
        this.coordinate = coordinate;
    }

    @Override
    public Iterable<Vector> getVertices(Vector min, Vector max) {
        if (axis == Axis.X) {
            if (min.getBlockZ() > max.getBlockZ()) return getVertices(max, min);
            int minY = Math.min(min.getBlockY(), max.getBlockY());
            int maxY = Math.max(min.getBlockY(), max.getBlockY());

            return IntStream.range(min.getBlockZ(), max.getBlockZ()).mapToObj(z -> {
                return IntStream.range(minY, maxY).mapToObj(y -> new Vector(coordinate + .5, y, z)).collect(Collectors.toList());
            }).flatMap(List::stream).collect(Collectors.toList());
        } else {
            if (min.getBlockX() > max.getBlockX()) return getVertices(max, min);
            int minY = Math.min(min.getBlockY(), max.getBlockY());
            int maxY = Math.max(min.getBlockY(), max.getBlockY());

            return IntStream.range(min.getBlockX(), max.getBlockX()).mapToObj(x -> {
                return IntStream.range(minY, maxY).mapToObj(y -> new Vector(x, y, coordinate + .5)).collect(Collectors.toList());
            }).flatMap(List::stream).collect(Collectors.toList());
        }
    }
}
