package org.spider.lambda.util;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.spider.lambda.util.Valences.Bivalent;

public interface Either<L, R> extends ValueObject {

	boolean isLeft();

	boolean isRight();

	default LeftProjection<L, R> left() {
		return new LeftProjection<>(this);
	}

	default RightProjection<L, R> right() {
		return new RightProjection<>(this);
	}

	// -- Object.*

	@Override
	boolean equals(Object o);

	@Override
	int hashCode();

	@Override
	String toString();

	// -- Left/Right projections

	static final class LeftProjection<L, R> implements Bivalent<L, R> {

		private final Either<L, R> either;

		LeftProjection(Either<L, R> either) {
			this.either = either;
		}

		@Override
        public L get() {
			if (either.isLeft()) {
				return asLeft();
			} else {
				throw new NoSuchElementException("Either.left().get() on Right");
			}
		}

		@Override
		public L orElse(L other) {
			return either.isLeft() ? asLeft() : other;
		}

		@Override
		public L orElseGet(Function<? super R, ? extends L> other) {
			if (either.isLeft()) {
				return asLeft();
			} else {
				return other.apply(asRight());
			}
		}

		@Override
		public void orElseRun(Consumer<? super R> action) {
			if (either.isRight()) {
				action.accept(asRight());
			}
		}

		@Override
		public <X extends Throwable> L orElseThrow(Function<? super R, X> exceptionFunction) throws X {
			if (either.isLeft()) {
				return asLeft();
			} else {
				throw exceptionFunction.apply(asRight());
			}
		}

		@Override
		public Optional<L> toOption() {
			if (either.isLeft()) {
				return Optional.of(asLeft());
			} else {
				return Optional.empty();
			}
		}

		@Override
		public Either<L, R> toEither() {
			return either;
		}

		public Optional<Either<L, R>> filter(Predicate<? super L> predicate) {
			Objects.requireNonNull(predicate);
			if (either.isLeft() && predicate.test(asLeft())) {
				return Optional.of(either);
			} else {
				return Optional.empty();
			}
		}

		public void forEach(Consumer<? super L> action) {
			Objects.requireNonNull(action);
			if (either.isLeft()) {
				action.accept(asLeft());
			}
		}

		@SuppressWarnings("unchecked")
		public <U> Either<U, R> map(Function<? super L, ? extends U> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isLeft())
				return new Left<>(mapper.apply(asLeft()));
			else {
				return (Either<U,R>) either;
			}
		}

		@SuppressWarnings("unchecked")
		public <U> Either<U, R> flatMap(Function<? super L, ? extends Either<U, R>> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isLeft()) {
				return mapper.apply(asLeft());
			} else {
				return (Either<U,R>) either;
			}
		}

		@Override
		public boolean equals(Object obj) {
			return (obj == this)|| (obj instanceof LeftProjection && Objects.equals(either, ((LeftProjection<?, ?>) obj).either));
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(either);
		}

		@Override
		public String toString() {
			return String.format("LeftProjection(%s)", either);
		}

		private L asLeft() {
			return ((Left<L, R>) either).left;
		}

		private R asRight() {
			return ((Right<L, R>) either).right;
		}
	}

	static final class RightProjection<L, R> implements Bivalent<R, L> {

		private final Either<L, R> either;

		RightProjection(Either<L, R> either) {
			this.either = either;
		}

		@Override
		public R get() {
			if (either.isRight()) {
				return asRight();
			} else {
				throw new NoSuchElementException("Either.right().get() on Left");
			}
		}

		@Override
		public R orElse(R other) {
			return either.isRight() ? asRight() : other;
		}

		@Override
		public R orElseGet(Function<? super L, ? extends R> other) {
			if (either.isRight()) {
				return asRight();
			} else {
				return other.apply(asLeft());
			}
		}

		@Override
		public void orElseRun(Consumer<? super L> action) {
			if (either.isLeft()) {
				action.accept(asLeft());
			}
		}

		@Override
		public <X extends Throwable> R orElseThrow(Function<? super L, X> exceptionFunction) throws X {
			if (either.isRight()) {
				return asRight();
			} else {
				throw exceptionFunction.apply(asLeft());
			}
		}

		@Override
		public Optional<R> toOption() {
			if (either.isRight()) {
				return Optional.of(asRight());
			} else {
				return Optional.empty();
			}
		}

		@Override
		public Either<L, R> toEither() {
			return either;
		}

		public Optional<Either<L, R>> filter(Predicate<? super R> predicate) {
			Objects.requireNonNull(predicate);
			if (either.isRight() && predicate.test(asRight())) {
				return Optional.of(either);
			} else {
				return Optional.empty();
			}
		}

		public void forEach(Consumer<? super R> action) {
			Objects.requireNonNull(action);
			if (either.isRight()) {
				action.accept(asRight());
			}
		}

		@SuppressWarnings("unchecked")
		public <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isRight())
				return new Right<>(mapper.apply(asRight()));
			else {
				return (Either<L, U>) either;
			}
		}

		@SuppressWarnings("unchecked")
		public <U> Either<L, U> flatMap(Function<? super R, ? extends Either<L, U>> mapper) {
			Objects.requireNonNull(mapper);
			if (either.isRight())
				return mapper.apply(asRight());
			else {
				return (Either<L, U>) either;
			}
		}

		@Override
		public boolean equals(Object obj) {
			return (obj == this)|| (obj instanceof RightProjection && Objects.equals(either, ((RightProjection<?, ?>) obj).either));
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(either);
		}

		@Override
		public String toString() {
			return String.format("RightProjection(%s)", either);
		}

		private L asLeft() {
			return ((Left<L, R>) either).left;
		}

		private R asRight() {
			return ((Right<L, R>) either).right;
		}
	}
}
