package org.vinz243.tesa;

import org.vinz243.tesa.context.CommandContext;
import org.vinz243.tesa.helpers.TesaCursor;
import org.vinz243.tesa.helpers.TransformResult;
import org.vinz243.tesa.helpers.Vector;
import org.vinz243.tesa.masks.IMaskable;
import org.vinz243.tesa.masks.IntersectionMask;
import org.vinz243.tesa.masks.Mask;
import org.vinz243.tesa.masks.MaskFactory;
import org.vinz243.tesa.transforms.NoSuchTransformException;
import org.vinz243.tesa.transforms.Transform;
import org.vinz243.tesa.transforms.TransformRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class Tessellator implements IMaskable {
    private List<Transform> transforms = new ArrayList<>();
    private boolean chiselLocked = false;
    private Set<Integer> debouncedBlocks = new HashSet<>();

    private final MaskFactory inputMaskFactory = new MaskFactory();
    private final MaskFactory outputMaskFactory = new MaskFactory();

    private final TesaCursor cursor = new TesaCursor();

    public TesaCursor getCursor() {
        return cursor;
    }

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

            final Transform transform = transforms.get(i);
            final Mask inputMask = new IntersectionMask(getInputMaskFactory().get(), transform.getInputMaskFactory().get());
            final Mask outputMask = new IntersectionMask(getOutputMaskFactory().get(), transform.getOutputMaskFactory().get());

            if (!inputMask.test(pos)) return;

            transform.apply(pos).forEach((result) -> {
                final int hash = result.getVector().blockPosHashCode();
                if (placedBlocks.indexOf(hash) >= 0) {
                    return;
                }
                placedBlocks.add(hash);

                if (outputMask.test(result.getVector())) {
                    applyFunction.accept(result);
                }
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
        cursor.setCurrentTransform(null);
    }

    public void addTransform(Transform transform) {
        transforms.add(transform);
        cursor.setCurrentTransform(transform);
    }

    public Transform addTransform(String key, CommandContext context) throws InvocationTargetException, NoSuchTransformException, InstantiationException, IllegalAccessException {
        final Transform instantiate = TransformRegistry.getInstance().instantiate(key, context);
        addTransform(instantiate);
        return instantiate;
    }

    List<Transform> getTransforms() {
        return Collections.unmodifiableList(transforms);
    }

    @Override
    public MaskFactory getInputMaskFactory() {
        return inputMaskFactory;
    }

    @Override
    public MaskFactory getOutputMaskFactory() {
        return outputMaskFactory;
    }


    public void pop() {
        transforms.remove(transforms.size() - 1);
        cursor.setCurrentTransform(transforms.get(transforms.size() - 1));
    }
}
