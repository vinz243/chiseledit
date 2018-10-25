package org.vinz243.chiseledit;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.math.transform.AffineTransform;
import mod.chiselsandbits.api.APIExceptions;
import mod.chiselsandbits.api.IBitAccess;
import mod.chiselsandbits.api.IBitBrush;
import mod.chiselsandbits.api.IChiselAndBitsAPI;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import scala.Int;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class ChiselUtils {

    public static final double OFFSET = 7.5;

    static int[][] identitMatrixTimes (int factor, int[][] b) {
        int[][] result = new int[b.length][b.length];
        iterator3d(new Vec3i(b.length, b.length, 1), (vec) -> {
            result[vec.getX()][vec.getY()] = factor * b[vec.getX()][vec.getY()];
        });
        return result;
    }

    static int[][] getMatrixForXRotation (int angle) {
        double theta = angle * Math.PI / 180;
        return new int[][] {
                {1, 0,                     0                     },
                {0, (int) Math.cos(theta), (int) -Math.sin(theta)},
                {0, (int) Math.sin(theta), (int) Math.cos(theta) }
        };
    }

    static int[][] getMatrixForYRotation (int angle) {
        double theta = angle * Math.PI / 180;
        int cos = (int) Math.cos(theta);
        int sin = (int) Math.sin(theta);
        return new int[][] {
                { cos, 0, sin},
                { 0,   1, 0  },
                {-sin, 0, cos}
        };
    }

    static int[][] getMatrixForZRotation (int angle) {
        double theta = angle * Math.PI / 180;
        int cos = (int) Math.cos(theta);
        int sin = (int) Math.sin(theta);
        return new int[][] {
                { cos, -sin, 0},
                { sin, cos,  0},
                { 0,   0,    1}
        };
    }

    public static void iterator3d (Vec3i max, Consumer<Vec3i> consumer) {
        IntStream.range(0, max.getX()).forEach((x) -> {
            IntStream.range(0, max.getY()).forEach((y) -> {
                IntStream.range(0, max.getZ()).forEach((z) -> {
                    consumer.accept(new Vec3i(x, y, z));
                });
            });
        });
    }

    static boolean rotateBlock (World world, BlockPos pos, int[][] rot) {
        IChiselAndBitsAPI chiselAndBitsAPI = ChiselAPIAccess.apiInstance;
        if (chiselAndBitsAPI.isBlockChiseled(world, pos))
        {
            IBitAccess bitAccess;
            try {
                bitAccess = chiselAndBitsAPI.getBitAccess(world, pos);
            } catch (APIExceptions.CannotBeChiseled e) {
                e.printStackTrace();
                return false;
            }

            List<Tuple<Vec3i, IBitBrush>> bits = new ArrayList<>();

            iterator3d(new Vec3i(16,16,16 ), (vec) -> {
                bits.add(new Tuple<>(vec, bitAccess.getBitAt(vec.getX(), vec.getY(), vec.getZ())));
            });
            bits.forEach((t) -> {
                Vec3i in = t.getFirst();
                Vec3i out = new Vec3i(
                        (rot[0][0] * (in.getX() - OFFSET) + rot[0][1] * (in.getY() - OFFSET) + rot[0][2] * (in.getZ() - OFFSET)) + OFFSET,
                        (rot[1][0] * (in.getX() - OFFSET) + rot[1][1] * (in.getY() - OFFSET) + rot[1][2] * (in.getZ() - OFFSET)) + OFFSET,
                        (rot[2][0] * (in.getX() - OFFSET) + rot[2][1] * (in.getY() - OFFSET) + rot[2][2] * (in.getZ() - OFFSET)) + OFFSET
                );
                try {
                    bitAccess.setBitAt(out.getX(), out.getY(), out.getZ(), t.getSecond());
                } catch (APIExceptions.SpaceOccupied spaceOccupied) {
                    spaceOccupied.printStackTrace();
                }
            });

            bitAccess.commitChanges(true);
        }
        return true;
    }

    static boolean transformBlock (World world, BlockPos pos, AffineTransform transform) {
        IChiselAndBitsAPI chiselAndBitsAPI = ChiselAPIAccess.apiInstance;
        if (chiselAndBitsAPI.isBlockChiseled(world, pos))
        {
            IBitAccess bitAccess;
            try {
                bitAccess = chiselAndBitsAPI.getBitAccess(world, pos);
            } catch (APIExceptions.CannotBeChiseled e) {
                e.printStackTrace();
                return false;
            }

            List<Tuple<Vec3i, IBitBrush>> bits = new ArrayList<>();

            iterator3d(new Vec3i(16,16,16 ), (vec) -> {
                bits.add(new Tuple<>(vec, bitAccess.getBitAt(vec.getX(), vec.getY(), vec.getZ())));
            });
            bits.forEach((t) -> {
                Vec3i in = t.getFirst();
                Vector tr = transform.apply(new Vector(in.getX() - OFFSET, in.getY() - OFFSET, in.getZ() - OFFSET))
                        .add(OFFSET, OFFSET, OFFSET);

                try {
                    bitAccess.setBitAt(tr.getBlockX(), tr.getBlockY(), tr.getBlockZ(), t.getSecond());
                } catch (APIExceptions.SpaceOccupied spaceOccupied) {
                    spaceOccupied.printStackTrace();
                }
            });

            bitAccess.commitChanges(true);
        }
        return true;
    }
}
