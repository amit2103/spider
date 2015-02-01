package org.spider.lambda.function;

import org.spider.lambda.tuple.Tuple3;

/**
 * A function with 3 arguments
 *
 * @author Amit Pandey
 */
@FunctionalInterface
public interface Function3<T1, T2, T3, R> {
    /**
     * Apply this function to the arguments.
     */
    default R apply(Tuple3<T1, T2, T3> args) {
        return apply(args.v1, args.v2, args.v3);
    }

    /**
     * Apply this function to the arguments.
     */
    R apply(T1 v1, T2 v2, T3 v3);
}
