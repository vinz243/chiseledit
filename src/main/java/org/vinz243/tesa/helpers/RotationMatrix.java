package org.vinz243.tesa.helpers;

public class RotationMatrix extends Matrix {
    private RotationMatrix(double[][] data) {
        super(data);
    }

    private RotationMatrix(double cos, double sin, double ux, double uy, double uz) {
        this(new double[][]{
                {cos + ux * ux * (1 - cos), ux * uy * (1 - cos) - uz * sin, ux * uz * (1 - cos) + uy * sin},
                {uy * ux * (1 - cos) + uz * sin, cos + uy * uy * (1 - cos), uy * uz * (1 - cos) - ux * sin},
                {uz * ux * (1 - cos) - uy * sin, uz * uy * (1 - cos) + ux * sin, cos + uz * uz * (1 - cos)}
        });
    }

    public RotationMatrix(double theta, Vector axis) {
        this(Math.cos(theta), Math.sin(theta), axis.getX(), axis.getY(), axis.getZ());
    }
}
