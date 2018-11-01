package org.vinz243.chiseledit.tess;

import net.minecraft.util.math.Vec3d;

@FunctionalInterface
public interface ITransform {
    Vec3d apply(Vec3d in);
}
