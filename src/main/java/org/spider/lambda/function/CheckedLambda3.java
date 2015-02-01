
package org.spider.lambda.function;


import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface CheckedLambda3<T1, T2, T3, R> extends Lambda<R> {

    R apply(T1 t1, T2 t2, T3 t3) throws Throwable;

    @Override
    default int arity() {
        return 3;
    }

    @Override
    default CheckedLambda1<T1, CheckedLambda1<T2, CheckedLambda1<T3, R>>> curried() {
        return t1 -> t2 -> t3 -> apply(t1, t2, t3);
    }

    @Override
    default CheckedLambda1<org.spider.lambda.tuple.Tuple3<T1, T2, T3>, R> tupled() {
        return t -> apply(t.v1, t.v2, t.v3);
    }

    @Override
    default CheckedLambda3<T3, T2, T1, R> reversed() {
        return (t3, t2, t1) -> apply(t1, t2, t3);
    }

    @Override
    default <V> CheckedLambda3<T1, T2, T3, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2, t3) -> after.apply(apply(t1, t2, t3));
    }

}