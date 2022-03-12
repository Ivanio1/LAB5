package data;

public class Coordinates {
    private long x;
    private long y;

    public Coordinates(long x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return X-coordinate.
     */
    public long getX() {
        return x;
    }

    /**
     * @return Y-coordinate.
     */
    public long getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
