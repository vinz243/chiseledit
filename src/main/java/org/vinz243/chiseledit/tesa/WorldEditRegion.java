package org.vinz243.chiseledit.tesa;

import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.vinz243.chiseledit.ChiselUtils;

import java.util.function.Consumer;

public class WorldEditRegion implements IRegion {
    private Region region;

    public WorldEditRegion(Region region) {
        this.region = region;
    }

    @Override
    public boolean contains(BlockPos pos) {
        return region.contains(ChiselUtils.toVector(pos));
    }

    @Override
    public BlockPos getMin() {
        return ChiselUtils.toBlockPos(region.getMinimumPoint());
    }

    @Override
    public BlockPos getMax() {
        return ChiselUtils.toBlockPos(region.getMaximumPoint());
    }

    @Override
    public void forEach(Consumer<BlockPos> function) {
        this.region.forEach((blockVector -> {
            function.accept(new BlockPos(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ()));
        }));
    }

    @Override
    public IRegion transform(ITransform transform) {
        Vec3d start = transform.apply(ChiselUtils.toVec3d(getMin()));
        Vec3d end = transform.apply(ChiselUtils.toVec3d(getMax()));

        return new WorldEditRegion(new CuboidRegion(ChiselUtils.toVector(start), ChiselUtils.toVector(end)));
    }
}
