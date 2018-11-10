package org.vinz243.tesa.transforms;

import org.apache.commons.lang3.NotImplementedException;
import org.vinz243.tesa.annotations.*;
import org.vinz243.tesa.context.CommandContext;
import org.vinz243.tesa.helpers.Axis;
import org.vinz243.tesa.helpers.Vector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class TransformRegistry {
    private static final TransformRegistry transformRegistry = new TransformRegistry();
    private final Map<String, Class<? extends Transform>> registry = new HashMap<>();

    public static TransformRegistry getInstance() {
        return transformRegistry;
    }

    public TransformRegistry register(String key, Class<? extends Transform> transform) {
        registry.put(key, transform);
        return this;
    }

    public Transform instantiate(String key, CommandContext context) throws NoSuchTransformException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends Transform> aClass = registry.get(key);

        if (aClass == null) throw new NoSuchTransformException();

        Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();

        Optional<Constructor<?>> first = Arrays.stream(declaredConstructors).filter((constr) -> {
            return constr.getAnnotation(InstantiableTransform.class) != null;
        }).findFirst();

        if (first.isPresent()) {
            Constructor<?> transform = first.get();
            AtomicInteger argCursor = new AtomicInteger(1);
            String[] args = context.getRemainingArgs();

            transform.setAccessible(true);

            final Object[] initargs = Arrays.stream(transform.getParameterAnnotations()).map(annotations -> {
                if (annotations.length != 1) throw new AssertionError();
                Annotation annotation = annotations[0];
                if (annotation instanceof Coordinates) {
                    Source source = ((Coordinates) annotation).from();
                    if (source == Source.Player) {
                        return new Vector(context.getPlayer().getPosition());
                    } else {
                        int pos = argCursor.getAndAdd(3);
                        return new Vector(
                                Integer.valueOf(args[pos]),
                                Integer.valueOf(args[pos + 1]),
                                Integer.valueOf(args[pos + 2])
                        );
                    }
                } else if (annotation instanceof Direction) {
                    final Source from = ((Direction) annotation).from();
                    if (from == Source.Player) {
                        switch (context.getPlayer().getHorizontalFacing().getAxis()) {
                            case X:
                                return Axis.X;
                            case Y:
                                return Axis.Y;
                            case Z:
                                return Axis.Z;
                        }
                    } else {
                        throw new NotImplementedException("Not implemented");
                    }
                } else if (annotation instanceof IntParam) {
                    final boolean required = ((IntParam) annotation).required();
                    final int def = ((IntParam) annotation).defaultValue();
                    final int max = ((IntParam) annotation).max();
                    final int min = ((IntParam) annotation).min();
                    int value = def;
                    try {
                        value = Integer.parseInt(context.getRemainingArgs()[argCursor.getAndAdd(1)]);
                    } catch (ArrayIndexOutOfBoundsException ignored) {
                        if (required) throw new AssertionError();
                    }

                    if (value > max || value < min) throw new AssertionError();
                    return value;
                }
                return null;
            }).toArray();

            return (Transform) transform.newInstance(initargs);
        } else {
            return null;
        }
    }
}
