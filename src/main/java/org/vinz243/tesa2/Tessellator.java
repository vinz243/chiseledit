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
import java.util.stream.IntStream;

public class Tessellator {
    private List<Transform> transforms = new ArrayList<>();

    public void apply(World world, BlockPos pos, IBlockState state) {
        apply(world, new Vector(pos), state, transforms);
    }

    private void apply(World world, Vector pos, IBlockState state, List<Transform> transforms) {
        System.out.println("transform " + pos);
        IntStream.range(0, transforms.size()).forEach(i -> {
            transforms.get(i).apply(pos).forEach((result) -> {
                final Vector vector = result.getVector();
                System.out.println("    -> " + vector);
                world.setBlockState(vector.toBlockPos(), state);
                apply(world, vector, state, transforms.subList(i + 1, transforms.size()));
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
