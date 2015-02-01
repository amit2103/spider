
package org.spider.lambda.function;

import java.util.function.BiFunction;

import org.spider.lambda.tuple.Tuple2;

/**
 * A function with 2 arguments
 *
 * @author Amit Pandey
 */
@FunctionalInterface
public interface Function2<T1, T2, R> extends BiFunction<T1, T2, R> {

    /**
     * Apply this function to the arguments.
     */
    default R apply(Tuple2<T1, T2> args) {
        return apply(args.v1, args.v2);
    }

    /**
     * Apply this function to the arguments.
     */
    @Override
    R apply(T1 v1, T2 v2);

    /**
     * Convert this function to a {@link java.util.function.BiFunction}
     */
    default BiFunction<T1, T2, R> toBiFunction() {
        return this::apply;
    }

    /**
     * Convert to this function to a {@link java.util.function.BiFunction}
     */
    static <T1, T2, R> Function2<T1, T2, R> from(BiFunction<T1, T2, R> function) {
        return function::apply;
    }
}
