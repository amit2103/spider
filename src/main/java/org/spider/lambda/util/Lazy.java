package org.spider.lambda.util;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Represents a lazy evaluated value. Compared to a Supplier, Lazy evaluates only once and therefore is referential transparent.
 * <pre>
 * <code>
 * final Lazy<Double> lazyDouble = Lazy.of(Math::random)
 * lazyDouble.get() // returns a random double, e.g. 0.123
 * lazyDouble.get() // returns the memoized value, e.g. 0.123
 * </code>
 * </pre>
 */
public final class Lazy<T> implements Supplier<T> {

    private Supplier<T> supplier;

    // read http://javarevisited.blogspot.de/2014/05/double-checked-locking-on-singleton-in-java.html
    private volatile Optional<T> value = Optional.empty();

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    @Override
    public T get() {
        if (!value.isPresent()) {
            synchronized (this) {
                if (!value.isPresent()) {
                    value = Optional.of(supplier.get());
                    supplier = null; // free mem
                }
            }
        }
        return value.get();
    }
}
