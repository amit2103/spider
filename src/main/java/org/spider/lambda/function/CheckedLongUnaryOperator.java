
package org.spider.lambda.function;

import java.io.Serializable;
import java.util.Objects;

@FunctionalInterface
public interface CheckedLongUnaryOperator extends Serializable {

    long applyAsLong(long operand) throws Throwable;

    default CheckedLongUnaryOperator compose(CheckedLongUnaryOperator before) {
        Objects.requireNonNull(before);
        return (long v) -> applyAsLong(before.applyAsLong(v));
    }

    default CheckedLongUnaryOperator andThen(CheckedLongUnaryOperator after) {
        Objects.requireNonNull(after);
        return (long t) -> after.applyAsLong(applyAsLong(t));
    }

    static CheckedLongUnaryOperator identity() {
        return t -> t;
    }
}
