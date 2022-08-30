package util.qtree;

public final class Range {
    public Range(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    public double minX() {
        return minX;
    }

    public double minY() {
        return minY;
    }

    public double maxX() {
        return maxX;
    }

    public double maxY() {
        return maxY;
    }

    public boolean contains(double x, double y) {
        return minX <= x && x <= maxX
            && minY <= y && y <= maxY;
    }

    @Override
    public String toString() {
        return String.format("Range(%s, %s, %s, %s)", minX, minY, maxX, maxY);
    }
}