package org.vinz243.tesa2.helpers;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Vector {
    private final double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(Vec3d vec) {
        this(vec.x, vec.y, vec.z);
    }

    public Vector(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public Vector multiply(double scalar) {
        return new Vector(scalar * x, scalar * y, scalar * z);
    }

    public Vector multiply(double x, double y, double z) {
        return new Vector(this.x * x, this.y * y, this.z * z);
    }

    public Vector add(Vector other) {
        return new Vector(other.x + x, other.y + y, other.z + z);
    }

    public Vector subtract(Vector other) {
        return add(other.multiply(-1));
    }

    public double dotProduct(Vector other) {
        return other.x * x + other.y * y + other.z * z;
    }

    public int getBlockX() {
        return (int) Math.floor(getX());
    }

    public int getBlockY() {
        return (int) Math.floor(getY());
    }

    public int getBlockZ() {
        return (int) Math.floor(getZ());
    }

    public BlockPos toBlockPos() {
        return new BlockPos(getBlockX(), getBlockY(), getBlockZ());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String toString() {
        return String.format("(%f, %f, %f)", x, y, z);
    }
}
