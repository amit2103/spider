
package org.spider.lambda.function;

import java.io.Serializable;

/**
 * Unchecked short to R function.
 * Essentially the same as {@code Function1<Short, R>}, or short {@code λ1<Short, R>}.
 *
 * @param <R> Return value type
 */
@FunctionalInterface
public interface ShortFunction<R> extends Serializable {

    R apply(short s);
}
