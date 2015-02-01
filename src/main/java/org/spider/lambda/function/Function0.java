
package org.spider.lambda.function;

import java.util.function.Supplier;

/**
 * A function with 0 arguments
 *
 * @author Amit Pandey
 */
@FunctionalInterface
public interface Function0<R> extends Supplier<R> {

    /**
     * Apply this function to the arguments.
     */
    default R apply() {
        return get();
    }

    /**
     * Apply this function to the arguments.
     */
    @Override
    R get();

    /**
     * Convert this function to a {@link java.util.function.Supplier}
     */
    default Supplier<R> toSupplier() {
        return this::apply;
    }

    /**
     * Convert to this function from a {@link java.util.function.Supplier}
     */
    static <R> Function0<R> from(Supplier<R> supplier) {
        return supplier::get;
    }

}
