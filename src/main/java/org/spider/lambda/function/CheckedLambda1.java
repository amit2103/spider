
package org.spider.lambda.function;

import java.util.Objects;
import java.util.function.Function;

import org.spider.lambda.tuple.Tuple1;

@FunctionalInterface
public interface CheckedLambda1<T1, R> extends Lambda<R> {

    static <T> CheckedLambda1<T, T> identity() {
        return t -> t;
    }

    R apply(T1 t1) throws Throwable;

    @Override
    default int arity() {
        return 1;
    }

    @Override
    default CheckedLambda1<T1, R> curried() {
        return t1 -> apply(t1);
    }

    @Override
    default CheckedLambda1<Tuple1<T1>, R> tupled() {
        return t -> apply(t.v1);
    }

    @Override
    default CheckedLambda1<T1, R> reversed() {
        return (t1) -> apply(t1);
    }

    @Override
    default <V> CheckedLambda1<T1, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1) -> after.apply(apply(t1));
    }

    default <V> CheckedLambda1<V, R> compose(Function<? super V, ? extends T1> before) {
        Objects.requireNonNull(before);
        return v -> apply(before.apply(v));
    }
}