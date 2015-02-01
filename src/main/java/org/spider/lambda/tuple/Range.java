package org.spider.lambda.tuple;

import java.util.Optional;


public class Range<T extends Comparable<T>> extends Tuple2<T, T> {

    private static final long serialVersionUID = 1L;

    public Range(T v1, T v2) {
        super(r(v1, v2));
    }

    public Range(Tuple2<T, T> tuple) {
        this(tuple.v1, tuple.v2);
    }

    private static <T extends Comparable<T>> Tuple2<T, T> r(T t1, T t2) {
        return t1.compareTo(t2) <= 0 ? new Tuple2<>(t1, t2) : new Tuple2<>(t2, t1);
    }

    /**
     * Whether two ranges overlap.
     * <p>
     * <code><pre>
     * // true
     * range(1, 3).overlaps(range(2, 4))
     *
     * // false
     * range(1, 3).overlaps(range(5, 8))
     * </pre></code>
     */
    public boolean overlaps(Tuple2<T, T> other) {
        return Tuple2.overlaps(this, other);
    }

    /**
     * Whether two ranges overlap.
     * <p>
     * <code><pre>
     * // true
     * range(1, 3).overlaps(2, 4)
     *
     * // false
     * range(1, 3).overlaps(5, 8)
     * </pre></code>
     */
    public boolean overlaps(T t1, T t2) {
        return overlaps(new Range<>(t1, t2));
    }

    /**
     * The intersection of two ranges.
     * <p>
     * <code><pre>
     * // (2, 3)
     * range(1, 3).intersect(range(2, 4))
     *
     * // none
     * range(1, 3).intersect(range(5, 8))
     * </pre></code>
     */
    public Optional<Range<T>> intersect(Tuple2<T, T> other) {
        return Tuple2.intersect(this, other).map(Range::new);
    }

    /**
     * The intersection of two ranges.
     * <p>
     * <code><pre>
     * // (2, 3)
     * range(1, 3).intersect(2, 4)
     *
     * // none
     * range(1, 3).intersect(5, 8)
     * </pre></code>
     */
    public Optional<Range<T>> intersect(T t1, T t2) {
        return intersect(new Range<>(t1, t2));
    }
}
