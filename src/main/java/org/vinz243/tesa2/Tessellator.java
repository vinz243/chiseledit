package org.vinz243.tesa2;

import org.vinz243.tesa2.helpers.TransformResult;
import org.vinz243.tesa2.helpers.Vector;
import org.vinz243.tesa2.transforms.NoSuchTransformException;
import org.vinz243.tesa2.transforms.Transform;
import org.vinz243.tesa2.transforms.TransformRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Tessellator {
    private List<Transform> transforms = new ArrayList<>();
    private boolean chiselLocked = false;
    private Set<Integer> debouncedBlocks = new HashSet<>();

    void debouncedApply(Vector pos, Consumer<TransformResult> applyFunction) {
        if (debouncedBlocks.remove(pos.blockPosHashCode())) {
            return;
        }
        apply(pos, (res) -> {
            debouncedBlocks.add(res.getVector().blockPosHashCode());
            applyFunction.accept(res);
        });
    }

    void apply(Vector pos, Consumer<TransformResult> applyFunction) {
        final ArrayList<Integer> placedBlocks = new ArrayList<>();
        placedBlocks.add(pos.blockPosHashCode());
        apply(pos, applyFunction, transforms, placedBlocks);
    }

    private void apply(Vector pos, Consumer<TransformResult> applyFunction, List<Transform> transforms, List<Integer> placedBlocks) {
        IntStream.range(0, transforms.size()).forEach(i -> {
            List<Vector> children = new ArrayList<>();
            transforms.get(i).apply(pos).forEach((result) -> {
                final int hash = result.getVector().blockPosHashCode();
                if (placedBlocks.indexOf(hash) >= 0) {
                    System.out.println("Already placed " + result.getVector());
                    return;
                }
                placedBlocks.add(hash);
                applyFunction.accept(result);

                children.add(result.getVector());
            });
            children.forEach(v -> {
                apply(v, applyFunction, transforms.subList(i + 1, transforms.size()), placedBlocks);
            });
        });
    }


    public boolean isChiselLocked() {
        return chiselLocked;
    }

    public void setChiselLocked(boolean chiselLocked) {
        this.chiselLocked = chiselLocked;
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

    List<Transform> getTransforms() {
        return transforms;
    }
}
