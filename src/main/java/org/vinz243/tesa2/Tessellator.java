package org.vinz243.tesa2;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.vinz243.tesa2.helpers.Vector;
import org.vinz243.tesa2.transforms.NoSuchTransformException;
import org.vinz243.tesa2.transforms.Transform;
import org.vinz243.tesa2.transforms.TransformRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Tessellator {
    private List<Transform> transforms = new ArrayList<>();

    public void apply(World world, BlockPos pos, IBlockState state) {
        transforms.forEach(t -> {
            t.apply(new Vector(pos)).forEach((outPos) -> {
                world.setBlockState(outPos.getVector().toBlockPos(), state);
            });
        });
    }

    public void clear() {
        transforms.clear();
    }

    public void addTransform(Transform transform) {
        transforms.add(transform);
    }

    public void addTransform(String key, CommandContext context) throws InvocationTargetException, NoSuchTransformException, InstantiationException, IllegalAccessException {
        addTransform(TransformRegistry.getInstance().instantiate(key, context));
    }
}
