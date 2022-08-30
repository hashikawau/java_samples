package util.shape;

public final class Polygon implements IPolygon {
    public Polygon(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    @Override
    public IPoint center() {
        return new Point((maxX - minX) / 2.0, (maxY - minY) / 2.0);
    }

    @Override
    public IPolygon outline() {
        return new Polygon(minX, minY, maxX, maxY);
    }

    @Override
    public double minX() {
        return minX;
    }

    @Override
    public double minY() {
        return minY;
    }

    @Override
    public double maxX() {
        return maxX;
    }

    @Override
    public double maxY() {
        return maxY;
    }

    @Override
    public String toString() {
        return String.format("Polygon((%s,%s),(%s,%s))", minX, minY, maxX, maxY);
    }
}