package org.vinz243.chiseledit.tess;

import net.minecraft.util.math.Vec3d;
import org.vinz243.chiseledit.ChiselUtils;

public class RotationTransform extends MatrixTransform {
    public RotationTransform(int angle, Vec3d offset) {
        super(ChiselUtils.getMatrixForYRotation(angle), offset);
    }
}
