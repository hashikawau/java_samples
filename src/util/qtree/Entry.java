package util.qtree;

import util.shape.IShape;

public final class Entry<T> {
    public Entry(IShape geom, T value) {
        this.geom = geom;
        this.value = value;
    }

    private final IShape geom;
    private final T value;

    public IShape getGeom() {
        return geom;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Entry(geom=%s, value=%s)", geom, value);
    }
}