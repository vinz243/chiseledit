package org.vinz243.tesa2.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Direction {
    Source from() default Source.Player;

    enum Axis {
        X, Y, Z
    }
}
