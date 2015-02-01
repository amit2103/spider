package org.spider.lambda.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class is not intended to be extended nor it is intended to be public API.
 */
final class Valences {

    /**
     * This class is not intended to be instantiated.
     */
    private Valences() {
        throw new AssertionError(Valences.class.getName() + " is not intended to be instantiated.");
    }

    // has one (primary) value
    static interface Univalent<T> {

        T get();

        T orElse(T other);

        T orElseGet(Supplier<? extends T> other);

        <X extends Throwable> T orElseThrow(Supplier<X> exceptionSupplier) throws X;

        Optional<T> toOption();
    }

    // has two values (, one is primary)
    static interface Bivalent<T, U> {

        T get();

        T orElse(T other);

        T orElseGet(Function<? super U, ? extends T> other);

        void orElseRun(Consumer<? super U> action);

        <X extends Throwable> T orElseThrow(Function<? super U, X> exceptionProvider) throws X;

        Optional<T> toOption();

        // order of generic parameters may vary (see Either.LeftProjection, Either.RightProjection)
        Either toEither();
    }
}
