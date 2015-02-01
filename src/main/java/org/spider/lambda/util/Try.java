package org.spider.lambda.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.spider.lambda.HigherKinded1;
import org.spider.lambda.Monad1;
import org.spider.lambda.function.CheckedRunnable;
import org.spider.lambda.function.CheckedSupplier;
import org.spider.lambda.util.Valences.Bivalent;

/**
 * An implementation similar to Scala's Try control.According to Erik Meijer this can be treated as a monad.
 * However Odersky differs.
 */
public interface Try<T> extends Monad1<T, Try<?>>, ValueObject, Bivalent<T, Throwable> {

	static <T> Try<T> of(CheckedSupplier<T> supplier) {
		try {
			return new Success<>(supplier.get());
		} catch (Throwable t) {
			return new Failure<>(t);
		}
	}

	static <T> Try<Void> run(CheckedRunnable runnable) {
		try {
			runnable.run();
			return new Success<>(null); // null represents the absence of an value, i.e. Void
		} catch (Throwable t) {
			return new Failure<>(t);
		}
	}

	boolean isFailure();

	boolean isSuccess();

	Try<T> recover(Function<Throwable, ? extends T> f);

	Try<T> recoverWith(Function<Throwable, Try<T>> f);

	Try<Throwable> failed();

	Try<T> filter(Predicate<? super T> predicate);

	void forEach(Consumer<? super T> action);


	<U> Try<U> map(Function<? super T, ? extends U> mapper);

	@Override
	<U, TRY extends HigherKinded1<U, Try<?>>> Try<U> flatMap(Function<? super T, TRY> mapper);

	@Override
	boolean equals(Object o);

	@Override
	int hashCode();

	@Override
	String toString();
}
