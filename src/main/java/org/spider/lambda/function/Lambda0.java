
package org.spider.lambda.function;

import java.util.Objects;
import java.util.function.Function;

import org.spider.lambda.tuple.Tuple;

@FunctionalInterface
public interface Lambda0<R> extends Lambda<R>, java.util.function.Supplier<R> {

    R apply();

    @Override
    default R get() {
        return apply();
    }

    @Override
    default int arity() {
        return 0;
    }

    @Override
    default Lambda1<Void, R> curried() {
        return v -> apply();
    }

    @Override
    default Lambda1<Tuple, R> tupled() {
        return t -> apply();
    }

    @Override
    default Lambda0<R> reversed() {
        return () -> apply();
    }

    @Override
    default <V> Lambda0<V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return () -> after.apply(apply());
    }

}
