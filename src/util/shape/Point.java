package util.shape;

public final class Point implements IPoint {
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    private final double x;
    private final double y;

    @Override
    public IPoint center() {
        return new Point(x, y);
    }

    @Override
    public IPolygon outline() {
        return new Polygon(x, y, x, y);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("Point(%s,%s)", x, y);
    }
}