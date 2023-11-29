package d3pack;

/**
 * Circle
 */
public class Circle {
    public double x;
    public double y;
    public double r;

    /**
     * create a circle object
     * 
     * @param x x
     * @param y y
     * @param r r
     */
    Circle(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    /**
     * create a circle object
     * 
     * @param x x
     * @param y y
     * @param r r
     */
    Circle(float x, float y, float r) {
        this((double) x, (double) y, (double) r);
    }

    /**
     * create a circle object
     * 
     * @param x x
     * @param y y
     * @param r r
     */
    Circle(int x, int y, int r) {
        this((double) x, (double) y, (double) r);
    }

    /**
     * return circle in a flat array
     * @return
     */
    float[] _toFloatArray() {
        return new float[] { (float) this.x, (float) this.y, (float) this.r };
    }

    /**
     * return circle in a flat array
     * @return
     */
    double[] _toArray() {
        return new double[] { this.x, this.y, this.r };
    }
}