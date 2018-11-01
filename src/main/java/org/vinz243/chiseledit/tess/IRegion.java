package org.vinz243.chiseledit.tess;

import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public interface IRegion {
    boolean contains(BlockPos pos);

    BlockPos getMin();

    BlockPos getMax();

    void forEach(Consumer<BlockPos> function);

    IRegion transform(ITransform transform);
}
