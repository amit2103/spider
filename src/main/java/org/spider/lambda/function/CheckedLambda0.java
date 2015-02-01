
package org.spider.lambda.function;

import java.util.Objects;
import java.util.function.Function;

import org.spider.lambda.tuple.Tuple;

@FunctionalInterface
public interface CheckedLambda0<R> extends Lambda<R> {

    R apply() throws Throwable;

    @Override
    default int arity() {
        return 0;
    }

    @Override
    default CheckedLambda1<Void, R> curried() {
        return v -> apply();
    }

    @Override
    default CheckedLambda1<Tuple, R> tupled() {
        return t -> apply();
    }

    @Override
    default CheckedLambda0<R> reversed() {
        return () -> apply();
    }

    @Override
    default <V> CheckedLambda0<V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return () -> after.apply(apply());
    }

}