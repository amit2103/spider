
package org.spider.lambda.function;

import java.util.Objects;
import java.util.function.Function;

import org.spider.lambda.tuple.Tuple2;

@FunctionalInterface
public interface Lambda2<T1, T2, R> extends Lambda<R>, java.util.function.BiFunction<T1, T2, R> {

    @Override
    R apply(T1 t1, T2 t2);

    @Override
    default int arity() {
        return 2;
    }

    @Override
    default Lambda1<T1, Lambda1<T2, R>> curried() {
        return t1 -> t2 -> apply(t1, t2);
    }

    @Override
    default Lambda1<Tuple2<T1, T2>, R> tupled() {
        return t -> apply(t.v1, t.v2);
    }

    @Override
    default Lambda2<T2, T1, R> reversed() {
        return (t2, t1) -> apply(t1, t2);
    }

    @Override
    default <V> Lambda2<T1, T2, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (t1, t2) -> after.apply(apply(t1, t2));
    }

}