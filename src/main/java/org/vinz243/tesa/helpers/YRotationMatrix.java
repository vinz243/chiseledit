package org.vinz243.tesa.helpers;

public class YRotationMatrix extends Matrix {
    public YRotationMatrix(double sin, double cos) {
        super(new double[][]{
                {cos, 0, sin},
                {0, 1, 0},
                {-sin, 0, cos}
        });
    }

    public YRotationMatrix(double theta) {
        this(Math.sin(theta), Math.cos(theta));
    }

    public YRotationMatrix(int angle) {
        this(angle * Math.PI / 180);
    }
}
