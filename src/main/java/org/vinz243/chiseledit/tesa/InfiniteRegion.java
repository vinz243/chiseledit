package org.vinz243.chiseledit.tesa;

import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public class InfiniteRegion implements IRegion {
    @Override
    public boolean contains(BlockPos pos) {
        return true;
    }

    @Override
    public BlockPos getMin() {
        return null;
    }

    @Override
    public BlockPos getMax() {
        return null;
    }

    @Override
    public void forEach(Consumer<BlockPos> function) {
        // no-op
    }

    @Override
    public IRegion transform(ITransform transform) {
        return new InfiniteRegion();
    }
}
