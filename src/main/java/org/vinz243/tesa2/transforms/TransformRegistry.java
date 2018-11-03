package org.vinz243.tesa2.transforms;

import org.vinz243.tesa2.CommandContext;
import org.vinz243.tesa2.annotations.Coordinates;
import org.vinz243.tesa2.annotations.InstantiableTransform;
import org.vinz243.tesa2.helpers.Vector;

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
            AtomicInteger argCursor = new AtomicInteger();
            String[] args = context.getRemainingArgs();

            transform.setAccessible(true);

            final Object[] initargs = Arrays.stream(transform.getParameterAnnotations()).map((annotations -> {
                assert (annotations.length == 1);
                Annotation annotation = annotations[0];
                if (annotation instanceof Coordinates) {
                    Coordinates.Source source = ((Coordinates) annotation).from();
                    if (source == Coordinates.Source.Player) {
                        return new Vector(context.getPlayer().getPosition());
                    } else {
                        int pos = argCursor.getAndAdd(3);
                        return new Vector(
                                Integer.valueOf(args[pos]),
                                Integer.valueOf(args[pos + 1]),
                                Integer.valueOf(args[pos + 2])
                        );
                    }
                }
                return null;
            })).toArray();
            return (Transform) transform.newInstance(initargs);
        } else {
            return null;
        }
    }
}
