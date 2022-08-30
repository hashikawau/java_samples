package util.qtree;

import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import util.shape.Point;
import util.shape.Polygon;

class QtreeTest {

    @Test
    void test_1() {
        int depth = 3;
        int indexX = 3;
        int indexY = 6;

        int result = 0;
        for (int i = 0; i < depth; i++) {
            result |= ((indexX >> i) & 0b01) << 2 * i;
            result |= ((indexY >> i) & 0b01) << 2 * i + 1;
        }
        // System.out.println(result);
    }

    @Test
    void test_2() {
        QTree<String> qtree = new QTree<>(
            new Range(-100, -100, 100, 100),
            3
        );
        qtree.insert(new Point(10, 10), "1");
        qtree.insert(new Point(10, 10), "2");
        qtree.insert(new Point(20, 20), "");
        qtree.insert(new Point(60, 60), "");
        // qtree.insert(new Point(45, -60), "45, -60");
        // qtree.insert(new Point(90, -20), "90, -20");
        // qtree.insert(new Point(0, -99), "0,-99");
        // qtree.insert(new Point(99, -99), "99,-99");
        // qtree.insert(new Point(-99, 0), "-99,-99");
        // qtree.insert(new Point(0, 0), "0,-99");
        // qtree.insert(new Point(99, 0), "99,-99");
        // qtree.insert(new Point(-99, 99), "-99,-99");
        // qtree.insert(new Point(0, 99), "0,-99");
        // qtree.insert(new Point(99, 99), "99,-99");
        qtree.insert(new Polygon(-45, -60, -90, -20), "");
        qtree.insert(new Polygon(10, 10, 80, 80), "");
        Stream<Entry<String>> stream = qtree.findAll(new Point(10, 10));
        stream.forEach(e -> System.out.println(e));
        // System.out.println(qtree.findAll(new Point(12.25, 12.25)));
        // System.out.println(qtree.scan(new Polygon(0, 0, 20, 20)));
        // System.out.println(qtree.scan(new Polygon(0, 0, 30, 30)));
        // System.out.println(qtree.scan(new Polygon(0, 0, 60, 60)));
        // System.out.println(qtree.scan(new Polygon(0, 0, 90, 90)));
        // System.out.println(qtree.scan(new Polygon(-99, -99, 99, 99)));
    }

    @Test
    void test_3() {
        long start = System.currentTimeMillis();
        QTree<String> qtree = new QTree<>(
            new Range(-100, -100, 100, 100),
            5
        );
        int num = 1000;
        Random rand = new Random();
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                int x1 = rand.nextInt(200) - 100;
                int y1 = rand.nextInt(200) - 100;
                int x2 = rand.nextInt(200) - 100;
                int y2 = rand.nextInt(200) - 100;
                qtree.insert(new Polygon(x1, y1, x2, y2), "");
            }
        }
        System.out.printf("elapsed=%d[ms]\n", System.currentTimeMillis() - start);
        start = System.currentTimeMillis();
        Stream<Entry<String>> stream = qtree.findAll(new Point(10, 10));
        long count = stream.count();
        System.out.println(count);
        System.out.printf("elapsed=%d[ms]\n", System.currentTimeMillis() - start);
    }
}
