import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.vinz243.tesa2.helpers.Matrix;

public class MatrixTest {
    private Matrix foo;

    @Before
    public void setup() {
        foo = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    }

    @Test
    public void test_getWidth() {
        Assert.assertEquals(new Matrix(new double[][]{{5, 5}, {5, 5}, {420, 420}}).getWidth(), 2);
    }

    @Test
    public void test_getHeight() {
        Assert.assertEquals(new Matrix(new double[][]{{5, 5}, {5, 5}, {420, 420}}).getHeight(), 3);
    }

    @Test
    public void test_scale() {
        final Matrix scaled = foo.scale(10);

        Assert.assertArrayEquals(scaled.get(0), new double[]{10, 20, 30}, 0.01d);
    }

    @Test
    public void test_identity_multiply() {
        final Matrix a = new Matrix(
                new double[][]{
                        {1, 0},
                        {0, 1}
                }
        );

        final Matrix b = new Matrix(
                new double[][]{
                        {21, 22},
                        {100, 50},
                }
        );

        final Matrix ab = a.multiply(b);

        Assert.assertArrayEquals(ab.get(0), new double[]{21, 22}, 0.01d);
        Assert.assertArrayEquals(ab.get(1), new double[]{100, 50}, 0.01d);
    }

    @Test
    public void test_two_multiply() {
        final Matrix a = new Matrix(
                new double[][]{
                        {1, 0},
                        {2, -1}
                }
        );

        final Matrix b = new Matrix(
                new double[][]{
                        {3, 4},
                        {-2, -3},
                }
        );

        final Matrix ab = a.multiply(b);

        Assert.assertArrayEquals(ab.get(0), new double[]{3, 4}, 0.01d);
        Assert.assertArrayEquals(ab.get(1), new double[]{8, 11}, 0.01d);
    }


    @Test
    public void test_multiply() {
        final Matrix a = new Matrix(
                new double[][]{{1, 2, 0}, {4, 3, -1}}
        );

        final Matrix b = new Matrix(
                new double[][]{
                        {5, 1},
                        {2, 3},
                        {3, 4}
                }
        );

        final Matrix ab = a.multiply(b);

        Assert.assertArrayEquals(new double[]{9, 7}, ab.get(0), 0.01d);
        Assert.assertArrayEquals(new double[]{23, 9}, ab.get(1), 0.01d);
    }
}
