package org.vinz243.tesa.helpers;


import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Matrix {
    public static final Matrix IDENTITY_1 = new Matrix(new double[][]{{1}});
    public static final Matrix IDENTITY_2 = new Matrix(new double[][]{{1, 0}, {1, 0}});
    public static final Matrix IDENTITY_3 = new Matrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});

    private final double[][] data;

    public Matrix(double[][] data) {
        this.data = data;
    }

    public Matrix(double m00, double m10, double m20, double m01, double m11, double m21, double m02, double m12, double m22) {
        this(new double[][]{{m00, m10, m20}, {m01, m11, m21}, {m02, m12, m22}});
    }

    public int getHeight() {
        return data.length;
    }

    public int getWidth() {
        return data[0].length;
    }

    public double get(int i, int j) {
        return data[i][j];
    }

    public double[] get(int i) {
        return data[i];
    }

    public boolean isSquared() {
        return getHeight() == getWidth();
    }

    public void forEach(MatrixVisitor fn) {
        IntStream.range(0, getHeight()).forEach(i -> IntStream.range(0, getWidth()).forEach(j -> {
            fn.apply(i, j, data[i][j]);
        }));
    }

    public Matrix scale(double scalar) {
        double[][] data = getEmptyMatchingArray();

        forEach((i, j, val) -> data[i][j] = scalar * val);

        return new Matrix(data);
    }


    public Matrix multiply(Matrix other) {
        assert (other.getHeight() == getWidth());

        double[][] data = new double[getHeight()][other.getWidth()];

        twoDimensionalForEach(getHeight(), other.getWidth(), (i, j) -> {
            data[i][j] = IntStream.range(0, getWidth()).mapToDouble((k) -> this.get(i, k) * other.get(k, j)).sum();
        });

        return new Matrix(data);
    }

    public Vector multiply(Vector other) {
        Matrix result = multiply(new Matrix(new double[][]{{other.getX()}, {other.getY()}, {other.getZ()}}));
        return new Vector(result.get(0, 0), result.get(1, 0), result.get(2, 0));
    }

    private double[][] getEmptyMatchingArray() {
        return new double[getHeight()][getWidth()];
    }

    public String toString() {
        return Arrays.stream(data).map(Arrays::toString).collect(Collectors.joining("; "));
    }

    private void twoDimensionalForEach(int u, int v, BiConsumer<Integer, Integer> consumer) {
        IntStream.range(0, u).forEach((i) -> IntStream.range(0, v).forEach(j -> consumer.accept(i, j)));
    }

}
