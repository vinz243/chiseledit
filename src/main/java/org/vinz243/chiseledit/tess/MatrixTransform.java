package org.vinz243.chiseledit.tess;

import net.minecraft.util.math.Vec3d;

public class MatrixTransform implements ITransform {
    private int[][] transform;
    private Vec3d offset;

    public MatrixTransform(int[][] transform, Vec3d offset) {
        this.transform = transform;
        this.offset = offset;
    }

    @Override
    public Vec3d apply(Vec3d in) {
        return new Vec3d(
                (transform[0][0] * (in.x - offset.x) + transform[0][1] * (in.y - offset.y) + transform[0][2] * (in.z - offset.z)) + offset.x,
                (transform[1][0] * (in.x - offset.x) + transform[1][1] * (in.y - offset.y) + transform[1][2] * (in.z - offset.z)) + offset.y,
                (transform[2][0] * (in.x - offset.x) + transform[2][1] * (in.y - offset.y) + transform[2][2] * (in.z - offset.z)) + offset.z
        );
    }
}
