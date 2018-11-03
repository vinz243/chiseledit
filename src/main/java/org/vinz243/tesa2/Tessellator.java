package org.vinz243.tesa2;

import org.vinz243.tesa2.helpers.TransformResult;
import org.vinz243.tesa2.helpers.Vector;
import org.vinz243.tesa2.transforms.NoSuchTransformException;
import org.vinz243.tesa2.transforms.Transform;
import org.vinz243.tesa2.transforms.TransformRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Tessellator {
    private List<Transform> transforms = new ArrayList<>();

    public void apply(Vector pos, Consumer<TransformResult> applyFunction) {
        apply(pos, applyFunction, transforms);
    }

    private void apply(Vector pos, Consumer<TransformResult> applyFunction, List<Transform> transforms) {
        IntStream.range(0, transforms.size()).forEach(i -> {
            transforms.get(i).apply(pos).forEach((result) -> {
                applyFunction.accept(result);
                apply(result.getVector(), applyFunction, transforms.subList(i + 1, transforms.size()));
            });
        });
    }

    public void clear() {
        transforms.clear();
    }

    public void addTransform(Transform transform) {
        transforms.add(transform);
    }

    public void addTransform(String key, CommandContext context) throws InvocationTargetException, NoSuchTransformException, InstantiationException, IllegalAccessException {
        addTransform(TransformRegistry.getInstance().instantiate(key, context));
    }
}
