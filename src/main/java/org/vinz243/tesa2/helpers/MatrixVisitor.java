package org.vinz243.tesa2.helpers;

@FunctionalInterface
public interface MatrixVisitor {
    void apply(int i, int j, double value);
}
