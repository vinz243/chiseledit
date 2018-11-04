package org.vinz243.tesa.helpers;

public class ZRotationMatrix extends Matrix {
    public ZRotationMatrix(double sin, double cos) {
        super(new double[][]{
                {cos, -sin, 0},
                {sin, cos, 0},
                {0, 0, 1}
        });
    }

    public ZRotationMatrix(double theta) {
        this(Math.sin(theta), Math.cos(theta));
    }

    public ZRotationMatrix(int angle) {
        this(angle * Math.PI / 180);
    }
}
