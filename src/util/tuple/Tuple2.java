package util.tuple;

public final class Tuple2<T1, T2> {
    public static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return new Tuple2<T1, T2>(t1, t2);
    }

    private Tuple2(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    private final T1 t1;
    private final T2 t2;

    public T1 t1() {
        return t1;
    }

    public T2 t2() {
        return t2;
    }
}
