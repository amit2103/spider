package org.spider.lambda.util;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.spider.lambda.HigherKinded1;
import org.spider.lambda.tuple.Tuple;
import org.spider.lambda.tuple.Tuple1;

public final class Success<T> implements Try<T> {

    private static final long serialVersionUID = 9157097743377386892L;

    private T value;

    public Success(T value) {
        this.value = value;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public T orElse(T other) {
        return value;
    }

    @Override
    public T orElseGet(Function<? super Throwable, ? extends T> other) {
        return value;
    }

    @Override
    public void orElseRun(Consumer<? super Throwable> action) {
        // nothing to do
    }

    @Override
    public <X extends Throwable> T orElseThrow(Function<? super Throwable, X> exceptionProvider) throws X {
        return value;
    }

    @Override
    public Try<T> recover(Function<Throwable, ? extends T> f) {
        return this;
    }

    @Override
    public Try<T> recoverWith(Function<Throwable, Try<T>> f) {
        return this;
    }

    @Override
    public Optional<T> toOption() {
        return Optional.of(value);
    }

    @Override
    public Either<Throwable, T> toEither() {
        return new Right<>(value);
    }

    @Override
    public Try<T> filter(Predicate<? super T> predicate) {
        try {
            if (predicate.test(value)) {
                return this;
            } else {
                return new Failure<>(new NoSuchElementException("Predicate does not hold for " + value));
            }
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public Try<Throwable> failed() {
        return new Failure<>(new UnsupportedOperationException("Success.failed()"));
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        action.accept(value);
    }

    @Override
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        try {
            return new Success<>(mapper.apply(value));
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U, TRY extends HigherKinded1<U, Try<?>>> Try<U> flatMap(Function<? super T, TRY> mapper) {
        try {
            return (Try<U>) mapper.apply(value);
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    public Tuple1<T> unapply() {
        return Tuple.tuple(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Success)) {
            return false;
        }
        final Success<?> success = (Success<?>) obj;
        return Objects.equals(value, success.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return String.format("Success(%s)", value);
    }
}
