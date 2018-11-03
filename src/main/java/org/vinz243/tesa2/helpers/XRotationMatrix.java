package org.vinz243.tesa2.helpers;

public class XRotationMatrix extends Matrix {
    public XRotationMatrix(double theta) {
        super(new double[][]{
                {1, 0, 0},
                {0, Math.cos(theta), -Math.sin(theta)},
                {0, Math.sin(theta), Math.cos(theta)}
        });
    }

    public XRotationMatrix(int angle) {
        this(angle * Math.PI / 180);
    }
}
