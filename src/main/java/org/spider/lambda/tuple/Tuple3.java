package org.spider.lambda.tuple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.spider.lambda.function.Function3;

/**
 * A tuple of degree 3.
 *
 */
public class Tuple3<T1, T2, T3> implements Tuple, Comparable<Tuple3<T1, T2, T3>>, Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public final T1           v1;
    public final T2           v2;
    public final T3           v3;

    public T1 v1() {
        return v1;
    }

    public T2 v2() {
        return v2;
    }

    public T3 v3() {
        return v3;
    }

    public Tuple3(Tuple3<T1, T2, T3> tuple) {
        this.v1 = tuple.v1;
        this.v2 = tuple.v2;
        this.v3 = tuple.v3;
    }

    public Tuple3(T1 v1, T2 v2, T3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }



    /**
     * Apply this tuple as arguments to a function.
     */
    public final <R> R map(Function3<T1, T2, T3, R> function) {
        return function.apply(this);
    }



    @Override
    public final Object[] array() {
        return new Object[] { v1, v2, v3 };
    }

    @Override
    public final List<?> list() {
        return Arrays.asList(array());
    }

    /**
     * The degree of this tuple: 3.
     */
    @Override
    public final int degree() {
        return 3;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<Object> iterator() {
        return (Iterator<Object>) list().iterator();
    }

    @Override
    public int compareTo(Tuple3<T1, T2, T3> other) {
        int result;
        result = Tuples.compare(v1, other.v1);
        if (result != 0)
            return result;
        result = Tuples.compare(v2, other.v2);
        if (result != 0)
            return result;
        result = Tuples.compare(v3, other.v3);
        if (result != 0)
            return result;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Tuple3))
            return false;
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Tuple3<T1, T2, T3> that = (Tuple3) o;
        if (!Objects.equals(v1, that.v1))
            return false;
        if (!Objects.equals(v2, that.v2))
            return false;
        if (!Objects.equals(v3, that.v3))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
        result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
        result = prime * result + ((v3 == null) ? 0 : v3.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "("
            + v1
            + ", " + v2
            + ", " + v3
            + ")";
    }

    @Override
    public Tuple3<T1, T2, T3> clone() {
        return new Tuple3<>(this);
    }
}
