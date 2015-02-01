
package org.spider.lambda.function;

import java.util.function.Function;

import org.spider.lambda.tuple.Tuple1;

/**
 * A function with 1 arguments
 *
 * @author Amit Pandey
 */
@FunctionalInterface
public interface Function1<T1, R> extends Function<T1, R> {

    /**
     * Apply this function to the arguments.
     */
    default R apply(Tuple1<T1> args) {
        return apply(args.v1);
    }

    /**
     * Apply this function to the arguments.
     */
    @Override
    R apply(T1 v1);

    /**
     * Convert this function to a {@link java.util.function.Function}
     */
    default Function<T1, R> toFunction() {
        return this::apply;
    }

    /**
     * Convert to this function from a {@link java.util.function.Function}
     */
    static <T1, R> Function1<T1, R> from(Function<T1, R> function) {
        return function::apply;
    }
}
