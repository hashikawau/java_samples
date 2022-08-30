package util.qtree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Stream;

import util.shape.IPoint;
import util.shape.IPolygon;
import util.shape.IShape;

public class QTree<T> {
    /**
     * 
     * @param range
     * @param depth hierarchy. depth >= 0.
     */
    public QTree(Range range, int depth) {
        this.logic = new Logic(range, depth);
        this.container = new CellContainer<T>(Logic.countCells(depth));
    }

    private final Logic logic;
    private final CellContainer<T> container;

    public void insert(IShape shape, T value) {
        CellIndex index = logic.cellIndex(shape);
        // System.out.printf("insert: size=%d, index=%s, shape=%s\n", container.size(), index, shape);
        container.insert(index, shape, value);
    }

    public Stream<Entry<T>> findAll(IShape shape) {
        CellIndex index = logic.cellIndex(shape);
        // System.out.printf("findAll: size=%d, index=%s, shape=%s\n", container.size(), index, shape);
        return logic
            .candidates(index)
            .map(i -> container.get(i))
            .filter(cell -> cell != null)
            .flatMap(cell -> cell.elems());
    }

    private static final class Logic {
        public Logic(Range range, int treeDepth) {
            this.range = range;
            this.treeDepth = treeDepth;
            this.numSplit = Math.pow(2, treeDepth);
        }

        private final Range range;
        private final int treeDepth;
        private final double numSplit;

        public static int countCells(int depth) {
            return (int) Math.floor((Math.pow(4, depth + 1) - 1) / 3);
        }

        public CellIndex cellIndex(IShape shape) {
            try {
                if (shape instanceof IPoint) {
                    return cellIndex(shape.center());
                } else {
                    return cellIndex(shape.outline());
                }
            } catch (Exception e) {
                throw new RuntimeException("invalid shape: " + shape, e);
            }
        }

        public CellIndex cellIndex(IPoint p) {
            int mo = mortonOrder(p.x(), p.y());
            return new CellIndex(treeDepth, mo);
        }

        public CellIndex cellIndex(IPolygon p) {
            int moMin = mortonOrder(p.minX(), p.minY());
            int moMax = mortonOrder(p.maxX(), p.maxY());
            int moMixed = moMin ^ moMax;
            int count = 0;
            while (moMixed > 0) {
                moMixed >>= 2;
                ++count;
            }
            int mo = moMin >> 2 * count;
            return new CellIndex(treeDepth - count, mo);
        }

        private int mortonOrder(double x, double y) {
            if (!range.contains(x, y)) {
                throw new IllegalArgumentException(String.format("point out of range: point=(%f, %f), range=%s", x, y, range));
            }
            int nx = zeroPaddedIndex(x, range.minX(), range.maxX());
            int ny = zeroPaddedIndex(y, range.minY(), range.maxY());
            return nx | (ny << 1);
        }

        private int zeroPaddedIndex(double p, double min, double max) {
            int index = (int) Math.floor(numSplit * (p - min) / (max - min));
            // e.g. index=11 -> result=0101
            int result = 0;
            for (int i = 0;; i++) {
                result |= (index & 0b01) << i * 2;
                index >>= 1;
                if (index <= 0) {
                    return result;
                }
            }
        }

        public Stream<CellIndex> candidates(CellIndex index) {
            return Stream.concat(
                parents(index),
                selfAndChildren(index)
            );
        }

        private Stream<CellIndex> parents(CellIndex index) {
            LinkedList<CellIndex> result = new LinkedList<>();
            CellIndex curr = index.parent();
            while (curr.depth >= 0) {
                result.addFirst(curr);
                curr = curr.parent();
            }
            return result.stream();
        }

        private Stream<CellIndex> selfAndChildren(CellIndex index) {
            LinkedList<CellIndex> result = new LinkedList<>();
            LinkedList<CellIndex> queue = new LinkedList<>();
            queue.add(index);
            while (!queue.isEmpty()) {
                CellIndex curr = queue.pop();
                result.add(curr);
                if (curr.depth < treeDepth) {
                    queue.addAll(curr.children());
                }
            }
            return result.stream();
        }
    }

    private static final class CellIndex {
        CellIndex(int depth, int mortonOrder) {
            this.depth = depth;
            this.mortonOrder = mortonOrder;
            this.containerIndex = Logic.countCells(depth - 1) + mortonOrder;
        }

        private int depth;
        private int mortonOrder;
        private int containerIndex;

        public Collection<CellIndex> children() {
            return Arrays.asList(
                child(0),
                child(1),
                child(2),
                child(3)
            );
        }

        /**
         * 
         * @param  childIndex index. 0 <= index < 4
         * @return
         */
        public CellIndex child(int childIndex) {
            return new CellIndex(
                depth + 1,
                (mortonOrder << 2) | childIndex
            );
        }

        public CellIndex parent() {
            return new CellIndex(
                depth - 1,
                mortonOrder >> 2
            );
        }

        @Override
        public String toString() {
            return String.format("CellIndex(depth=%s, mortonOrder=%s, containerIndex=%s)", depth, mortonOrder, containerIndex);
        }
    }

    private static final class CellContainer<T> {
        @SuppressWarnings("unchecked")
        public CellContainer(int size) {
            this.cells = new Cell[size];
        }

        private final Cell<T>[] cells;

        public void insert(CellIndex index, IShape shape, T value) {
            Cell<T> cell = cells[index.containerIndex];
            if (cell == null) {
                cell = new Cell<>();
                cells[index.containerIndex] = cell;
            }
            cell.insert(shape, value);
        }

        public Cell<T> get(CellIndex index) {
            return cells[index.containerIndex];
        }
    }

    private static final class Cell<T> {
        private ArrayList<Entry<T>> elems = new ArrayList<>();

        public void insert(IShape shape, T value) {
            elems.add(new Entry<T>(shape, value));
        }

        public Stream<Entry<T>> elems() {
            return elems.stream();
        }

        @Override
        public String toString() {
            return String.format("Cell(elems=%s)", elems);
        }
    }
}