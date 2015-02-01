
package org.spider.lambda;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static org.spider.lambda.tuple.Tuple.tuple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.spider.lambda.tuple.Tuple;
import org.spider.lambda.tuple.Tuple2;


/**
 * A sequential, ordered {@link Stream} that adds all sorts of useful methods that work only because
 * it is sequential and ordered.
 *
 * @author Amit Pandey
 */
public interface Sequence<T> extends Stream<T>, Iterable<T> {

    /**
     * The underlying {@link Stream} implementation.
     */
    Stream<T> stream();

    /**
     * Sequence.of(1, 2, 3).concat(Sequence.of(4, 5, 6))
     * will give you a stream (1,2,3,4,5,6)
     */
    @SuppressWarnings({ "unchecked" })
    default Sequence<T> concat(Stream<T> other) {
        return Sequence.concat(new Stream[] { this, other });
    }

    /**
     * Concatenate two streams.
     * Sequence.of(1, 2, 3).concat(4)
     * will give a sequence of (1, 2, 3, 4)
     */
    default Sequence<T> concat(T other) {
        return concat(Sequence.of(other));
    }

    /**
     * Concatenate two streams.
     * Sequence.of(1, 2, 3).concat(4, 5, 6) will\
     *give // (1, 2, 3, 4, 5, 6)
     */
    @SuppressWarnings({ "unchecked" })
    default Sequence<T> concat(T... other) {
        return concat(Sequence.of(other));
    }

    /**
     * Repeat a stream infinitely.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 1, 2, 3, ...)
     * Seq.of(1, 2, 3).cycle();
     * </pre></code>
     *
     * @see #cycle(Stream)
     */
    default Sequence<T> cycle() {
        return cycle(this);
    }

    /**
     * Zip two streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     *
     * @see #zip(Stream, Stream)
     */
    default <U> Sequence<Tuple2<T, U>> zip(Sequence<U> other) {
        return zip(this, other);
    }

    /**
     * Fold a Stream to the left.
     */
    default <U> U foldLeft(U seed, BiFunction<U, ? super T, U> function) {
        return foldLeft(this, seed, function);
    }

    /**
     * Fold a Stream to the right.
     * <p>
     * <code><pre>
     * // "cba"
     * Seq.of("a", "b", "c").foldRight("", (t, u) -> u + t)
     * </pre></code>
     */
    default <U> U foldRight(U seed, BiFunction<? super T, U, U> function) {
        return foldRight(this, seed, function);
    }


    /**
     * Reverse a stream.
     * <p>
     * <code><pre>
     * // (3, 2, 1)
     * Seq.of(1, 2, 3).reverse()
     * </pre></code>
     */
    default Sequence<T> reverse() {
        return reverse(this);
    }


    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>true</code>.
     * @see #skipWhile(Stream, Predicate)
     */
    default Sequence<T> skipWhile(Predicate<? super T> predicate) {
        return skipWhile(this, predicate);
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>false</code>.
     * @see #skipUntil(Stream, Predicate)
     */
    default Sequence<T> skipUntil(Predicate<? super T> predicate) {
        return skipUntil(this, predicate);
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>.
     */
    default Sequence<T> getWhile(Predicate<? super T> predicate) {
        return limitWhile(this, predicate);
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to false
     * @see #limitUntil(Stream, Predicate)
     */
    default Sequence<T> getUntil(Predicate<? super T> predicate) {
        return limitUntil(this, predicate);
    }


    default Tuple2<Sequence<T>, Sequence<T>> partition(Predicate<? super T> predicate) {
        return partition(this, predicate);
    }


    /**
     * Split a stream at the head.
     * <p>
     * <code><pre>
     * // tuple(1, (2, 3, 4, 5, 6))
     * Seq.of(1, 2, 3, 4, 5, 6).splitHead(3)
     * </pre></code>
     *
     * @see #splitAt(Stream, long)
     */
    default Tuple2<Optional<T>, Sequence<T>> splitAtHead() {
        return splitAtHead(this);
    }


    default Optional<T> findFirst(Predicate<? super T> predicate) {
        for (T a : this) {
            if (predicate.test(a)) {
                return Optional.of(a); // may be Some(null)
            }
        }
        return Optional.empty();
    }

    default Optional<T> findLast(Predicate<? super T> predicate) {
        return reverse().findFirst(predicate);
    }

    default int length() {
        return foldLeft(0, (n, ignored) -> n + 1);
    }


    /**
     * Returns a limited interval from a given Stream.
     * <p>
     * <code><pre>
     * // (4, 5)
     * Seq.of(1, 2, 3, 4, 5, 6).slice(3, 5)
     * </pre></code>
     *
     * @see #slice(Stream, long, long)
     */
    default Sequence<T> slice(long from, long to) {
        return slice(this, from, to);
    }

    /**
     * Collect a Stream into a Collection.
     *
     * @see #toCollection(Stream, Supplier)
     */
    default <C extends Collection<T>> C toCollection(Supplier<C> collectionFactory) {
        return toCollection(this, collectionFactory);
    }

    /**
     * Collect a Stream into a List.
     *
     * @see #toList(Stream)
     */
    default List<T> toList() {
        return toList(this);
    }

    /**
     * Collect a Stream into a Set.
     *
     * @see #toSet(Stream)
     */
    default Set<T> toSet() {
        return toSet(this);
    }

    /**
     * Collect a Stream into a Map.
     *
     * @see #toMap(Stream, Function, Function)
     */
    default <K, V> Map<K, V> toMap(Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return toMap(this, keyMapper, valueMapper);
    }

    /**
     * Consume a stream and concatenate all elements using a separator.
     */
    default String toString(String separator) {
        return toString(this, separator);
    }

    /**
     * Get the maximum value by a function.
     */
    default <U extends Comparable<U>> Optional<T> minBy(Function<T, U> function) {
        return minBy(function, naturalOrder());
    }

    /**
     * Get the maximum value by a function.
     */
    default <U> Optional<T> minBy(Function<T, U> function, Comparator<? super U> comparator) {
        return map(t -> tuple(t, function.apply(t)))
              .min(comparing(Tuple2::v2, comparator))
              .map(t -> t.v1);
    }

    /**
     * Get the maximum value by a function.
     */
    default <U extends Comparable<U>> Optional<T> maxBy(Function<T, U> function) {
        return maxBy(function, naturalOrder());
    }

    /**
     * Get the maximum value by a function.
     */
    default <U> Optional<T> maxBy(Function<T, U> function, Comparator<? super U> comparator) {
        return map(t -> tuple(t, function.apply(t)))
              .max(comparing(Tuple2::v2, comparator))
              .map(t -> t.v1);
    }

    // Methods taken from LINQ
    // -----------------------

    /**
     * Keep only those elements in a stream that are of a given type.
     * <p>
     * <code><pre>
     * // (1, 2, 3)
     * Seq.of(1, "a", 2, "b", 3).ofType(Integer.class)
     * </pre></code>
     *
     * @see #ofType(Stream, Class)
     */
    default <U> Sequence<U> ofType(Class<U> type) {
        return ofType(this, type);
    }

    /**
     * Cast all elements in a stream to a given type, possibly throwing a {@link ClassCastException}.
     * <p>
     * <code><pre>
     * // ClassCastException
     * Seq.of(1, "a", 2, "b", 3).cast(Integer.class)
     * </pre></code>
     *
     * @see #cast(Stream, Class)
     */
    default <U> Sequence<U> cast(Class<U> type) {
        return cast(this, type);
    }

    // Shortcuts to Collectors
    // -----------------------

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#groupingBy(Function)} collector.
     */
    default <K> Map<K, List<T>> groupBy(Function<? super T, ? extends K> classifier) {
        return collect(Collectors.groupingBy(classifier));
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#groupingBy(Function, Collector)} collector.
     */
    default <K, A, D> Map<K, D> groupBy(Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream) {
        return collect(Collectors.groupingBy(classifier, downstream));
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#groupingBy(Function, Supplier, Collector)} collector.
     */
    default <K, D, A, M extends Map<K, D>> M groupBy(Function<? super T, ? extends K> classifier, Supplier<M> mapFactory, Collector<? super T, A, D> downstream) {
        return collect(Collectors.groupingBy(classifier, mapFactory, downstream));
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#joining()}
     * collector.
     */
    default String join() {
        return map(Objects::toString).collect(Collectors.joining());
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#joining(CharSequence)}
     * collector.
     */
    default String join(CharSequence delimiter) {
        return map(Objects::toString).collect(Collectors.joining(delimiter));
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#joining(CharSequence, CharSequence, CharSequence)}
     * collector.
     */
    default String join(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return map(Objects::toString).collect(Collectors.joining(delimiter, prefix, suffix));
    }

    /**
     * @see Stream#of(Object)
     */
    static <T> Sequence<T> of(T value) {
        return sequence(Stream.of(value));
    }

    /**
     * @see Stream#of(Object)
     */
    static <T> Sequence<T> of(Stream<T> stream) {
        return sequence(stream);
    }

    /**
     * @see Stream#of(Object[])
     */
    @SafeVarargs
    static <T> Sequence<T> of(T... values) {
        return sequence(Stream.of(values));
    }

    /**
     * @see Stream#empty()
     */
    static <T> Sequence<T> empty() {
        return sequence(Stream.empty());
    }

    /**
     * @see Stream#iterate(Object, UnaryOperator)
     */
    static <T> Sequence<T> iterate(final T seed, final UnaryOperator<T> f) {
        return sequence(Stream.iterate(seed, f));
    }

    /**
     * @see Stream#generate(Supplier)
     */
    static Sequence<Void> generate() {
        return generate(() -> null);
    }

    /**
     * @see Stream#generate(Supplier)
     */
    static <T> Sequence<T> generate(T value) {
        return generate(() -> value);
    }

    /**
     * @see Stream#generate(Supplier)
     */
    static <T> Sequence<T> generate(Supplier<T> s) {
        return sequence(Stream.generate(s));
    }

    /**
     * Wrap a Stream into a Seq.
     */
    static <T> Sequence<T> sequence(Stream<T> stream) {
        if (stream instanceof Sequence)
            return (Sequence<T>) stream;

        return new SequenceImpl<>(stream);
    }

    /**
     * Wrap an Iterable into a sequence.
     */
    static <T> Sequence<T> sequence(Iterable<T> iterable) {
        return sequence(iterable.iterator());
    }

    /**
     * Wrap an Iterator into a Seq.
     */
    static <T> Sequence<T> sequence(Iterator<T> iterator) {
        return sequence(StreamSupport.stream(spliteratorUnknownSize(iterator, ORDERED), false));
    }

    /**
     * Wrap an Optional into a Seq.
     */
    static <T> Sequence<T> sequence(Optional<T> optional) {
        return optional.map(Sequence::of).orElseGet(Sequence::empty);
    }

    /**
     * Repeat a stream infinitely.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 1, 2, 3, ...)
     * Seq.of(1, 2, 3).cycle();
     * </pre></code>
     */
    static <T> Sequence<T> cycle(Stream<T> stream) {
        final List<T> list = new ArrayList<>();

        class Cycle implements Iterator<T> {
            boolean cycled;
            Iterator<T> it;

            Cycle(Iterator<T> it) {
                this.it = it;
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                if (!it.hasNext()) {
                    cycled = true;
                    it = list.iterator();
                }

                T next = it.next();

                if (!cycled) {
                    list.add(next);
                }

                return next;
            }
        }

        return sequence(new Cycle(stream.iterator()));
    }


    /**
     * Zip two streams into one.
     * <p>
     * <code><pre>
     * // (tuple(1, "a"), tuple(2, "b"), tuple(3, "c"))
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"))
     * </pre></code>
     */
    static <T1, T2> Sequence<Tuple2<T1, T2>> zip(Stream<T1> left, Stream<T2> right) {
        return zip(left, right, Tuple::tuple);
    }

    /**
     * Zip two streams into one using a {@link BiFunction} to produce resulting values.
     * <p>
     * <code><pre>
     * // ("1:a", "2:b", "3:c")
     * Seq.of(1, 2, 3).zip(Seq.of("a", "b", "c"), (i, s) -> i + ":" + s)
     * </pre></code>
     */
    static <T1, T2, R> Sequence<R> zip(Stream<T1> left, Stream<T2> right, BiFunction<T1, T2, R> zipper) {
        final Iterator<T1> it1 = left.iterator();
        final Iterator<T2> it2 = right.iterator();

        class Zip implements Iterator<R> {
            @Override
            public boolean hasNext() {
                return it1.hasNext() && it2.hasNext();
            }

            @Override
            public R next() {
                return zipper.apply(it1.next(), it2.next());
            }
        }

        return sequence(new Zip());
    }

    /**
     * Fold a stream to the left.
     * <p>
     * <code><pre>
     * // "abc"
     * Seq.of("a", "b", "c").foldLeft("", (u, t) -> u + t)
     * </pre></code>
     */
    static <T, U> U foldLeft(Stream<T> stream, U seed, BiFunction<U, ? super T, U> function) {
        final Iterator<T> it = stream.iterator();
        U result = seed;

        while (it.hasNext())
            result = function.apply(result, it.next());

        return result;
    }

    default boolean contains(T element) {
        return findFirst(e -> java.util.Objects.equals(e, element)).isPresent();
    }

    /**
     * Fold a stream to the right.
     * <p>
     * <code><pre>
     * // "cba"
     * Seq.of("a", "b", "c").foldRight("", (t, u) -> u + t)
     * </pre></code>
     */
    static <T, U> U foldRight(Stream<T> stream, U seed, BiFunction<? super T, U, U> function) {
        return sequence(stream).reverse().foldLeft(seed, (u, t) -> function.apply(t, u));
    }

    /**
     * Unfold a function into a stream.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5)
     * Seq.unfold(1, i -> i <= 6 ? Optional.of(tuple(i, i + 1)) : Optional.empty())
     * </pre></code>
     */
    static <T, U> Sequence<T> unfold(U seed, Function<U, Optional<Tuple2<T, U>>> unfolder) {
        class Unfold implements Iterator<T> {
            U u;
            Optional<Tuple2<T, U>> unfolded;

            public Unfold(U u) {
                this.u = u;
            }

            void unfold() {
                if (unfolded == null)
                    unfolded = unfolder.apply(u);
            }

            @Override
            public boolean hasNext() {
                unfold();
                return unfolded.isPresent();
            }

            @Override
            public T next() {
                unfold();

                try {
                    return unfolded.get().v1;
                }
                finally {
                    u = unfolded.get().v2;
                    unfolded = null;
                }
            }
        }

        return sequence(new Unfold(seed));
    }

    /**
     * Reverse a stream.
     * <p>
     * <code><pre>
     * // (3, 2, 1)
     * Seq.of(1, 2, 3).reverse()
     * </pre></code>
     */
    static <T> Sequence<T> reverse(Stream<T> stream) {
        List<T> list = toList(stream);
        Collections.reverse(list);
        return sequence(list);
    }

    /**
     * Shuffle a stream
     * <p>
     * <code><pre>
     * // e.g. (2, 3, 1)
     * Seq.of(1, 2, 3).shuffle()
     * </pre></code>
     */
    static <T> Sequence<T> shuffle(Stream<T> stream) {
        List<T> list = toList(stream);
        Collections.shuffle(list);
        return sequence(list);
    }

    /**
     * Concatenate a number of streams.
     * <p>
     * <code><pre>
     * // (1, 2, 3, 4, 5, 6)
     * Seq.of(1, 2, 3).concat(Seq.of(4, 5, 6))
     * </pre></code>
     */
    @SafeVarargs
    static <T> Sequence<T> concat(Stream<T>... streams) {
        if (streams == null || streams.length == 0)
            return Sequence.empty();

        if (streams.length == 1)
            return sequence(streams[0]);

        Stream<T> result = streams[0];
        for (int i = 1; i < streams.length; i++)
            result = Stream.concat(result, streams[i]);

        return sequence(result);
    }

    /**
     * Duplicate a Streams into two equivalent Streams.
     * <p>
     * <code><pre>
     * // tuple((1, 2, 3), (1, 2, 3))
     * Seq.of(1, 2, 3).duplicate()
     * </pre></code>
     */
    static <T> Tuple2<Sequence<T>, Sequence<T>> duplicate(Stream<T> stream) {
        final LinkedList<T> gap = new LinkedList<>();
        final Iterator<T> it = stream.iterator();

        @SuppressWarnings({"unchecked"})
        final Iterator<T>[] ahead = new Iterator[] { null };

        class Duplicate implements Iterator<T> {
            @Override
            public boolean hasNext() {
                if (ahead[0] == null || ahead[0] == this)
                    return it.hasNext();

                return !gap.isEmpty();
            }

            @Override
            public T next() {
                if (ahead[0] == null)
                    ahead[0] = this;

                if (ahead[0] == this) {
                    T value = it.next();
                    gap.offer(value);
                    return value;
                }

                return gap.poll();
            }
        }

        return tuple(sequence(new Duplicate()), sequence(new Duplicate()));
    }

    /**
     * Consume a stream and concatenate all elements.
     */
    static String toString(Stream<?> stream) {
        return toString(stream, "");
    }

    /**
     * Consume a stream and concatenate all elements using a separator.
     */
    static String toString(Stream<?> stream, String separator) {
        return stream.map(Objects::toString).collect(Collectors.joining(separator));
    }

    /**
     * Collect a Stream into a List.
     */
    static <T, C extends Collection<T>> C toCollection(Stream<T> stream, Supplier<C> collectionFactory) {
        return stream.collect(Collectors.toCollection(collectionFactory));
    }

    /**
     * Collect a Stream into a List.
     */
    static <T> List<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toList());
    }

    /**
     * Collect a Stream into a Set.
     */
    static <T> Set<T> toSet(Stream<T> stream) {
        return stream.collect(Collectors.toSet());
    }

    /**
     * Collect a Stream of {@link Tuple2} into a Map.
     */
    static <T, K, V> Map<K, V> toMap(Stream<Tuple2<K, V>> stream) {
        return stream.collect(Collectors.toMap(Tuple2::v1, Tuple2::v2));
    }

    /**
     * Collect a Stream into a Map.
     */
    static <T, K, V> Map<K, V> toMap(Stream<T> stream, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return stream.collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * Returns a limited interval from a given Stream.
     * <p>
     * <code><pre>
     * // (4, 5)
     * Seq.of(1, 2, 3, 4, 5, 6).slice(3, 5)
     * </pre></code>
     */
    static <T> Sequence<T> slice(Stream<T> stream, long from, long to) {
        long f = Math.max(from, 0);
        long t = Math.max(to - f, 0);

        return sequence(stream.skip(f).limit(t));
    }

    /**
     * Returns a stream with n elements skipped.
     * <p>
     * <code><pre>
     * // (4, 5, 6)
     * Seq.of(1, 2, 3, 4, 5, 6).skip(3)
     * </pre></code>
     */
    static <T> Sequence<T> skip(Stream<T> stream, long elements) {
        return sequence(stream.skip(elements));
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (3, 4, 5)
     * Seq.of(1, 2, 3, 4, 5).skipWhile(i -> i < 3)
     * </pre></code>
     */
    static <T> Sequence<T> skipWhile(Stream<T> stream, Predicate<? super T> predicate) {
        return skipUntil(stream, predicate.negate());
    }

    /**
     * Returns a stream with all elements skipped for which a predicate evaluates to <code>false</code>.
     * <p>
     * <code><pre>
     * // (3, 4, 5)
     * Seq.of(1, 2, 3, 4, 5).skipUntil(i -> i == 3)
     * </pre></code>
     */
    @SuppressWarnings("unchecked")
    static <T> Sequence<T> skipUntil(Stream<T> stream, Predicate<? super T> predicate) {
        final Iterator<T> it = stream.iterator();

        class SkipUntil implements Iterator<T> {
            T next = (T) SequenceImpl.NULL;
            boolean test = false;

            void skip() {
                while (next == SequenceImpl.NULL && it.hasNext()) {
                    next = it.next();

                    if (test || (test = predicate.test(next)))
                        break;
                    else
                        next = (T) SequenceImpl.NULL;
                }
            }

            @Override
            public boolean hasNext() {
                skip();
                return next != SequenceImpl.NULL;
            }

            @Override
            public T next() {
                if (next == SequenceImpl.NULL)
                    throw new NoSuchElementException();

                try {
                    return next;
                }
                finally {
                    next = (T) SequenceImpl.NULL;
                }
            }
        }

        return sequence(new SkipUntil());
    }

    /**
     * Returns a stream limited to n elements.
     * <p>
     * <code><pre>
     * // (1, 2, 3)
     * Seq.of(1, 2, 3, 4, 5, 6).limit(3)
     * </pre></code>
     */
    static <T> Sequence<T> limit(Stream<T> stream, long elements) {
        return sequence(stream.limit(elements));
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (1, 2)
     * Seq.of(1, 2, 3, 4, 5).limitWhile(i -> i < 3)
     * </pre></code>
     */
    static <T> Sequence<T> limitWhile(Stream<T> stream, Predicate<? super T> predicate) {
        return limitUntil(stream, predicate.negate());
    }

    /**
     * Returns a stream limited to all elements for which a predicate evaluates to <code>true</code>.
     * <p>
     * <code><pre>
     * // (1, 2)
     * Seq.of(1, 2, 3, 4, 5).limitUntil(i -> i == 3)
     * </pre></code>
     */
    @SuppressWarnings("unchecked")
    static <T> Sequence<T> limitUntil(Stream<T> stream, Predicate<? super T> predicate) {
        final Iterator<T> it = stream.iterator();

        class LimitUntil implements Iterator<T> {
            T next = (T) SequenceImpl.NULL;
            boolean test = false;

            void test() {
                if (!test && next == SequenceImpl.NULL && it.hasNext()) {
                    next = it.next();

                    if (test = predicate.test(next))
                        next = (T) SequenceImpl.NULL;
                }
            }

            @Override
            public boolean hasNext() {
                test();
                return next != SequenceImpl.NULL;
            }

            @Override
            public T next() {
                if (next == SequenceImpl.NULL)
                    throw new NoSuchElementException();

                try {
                    return next;
                }
                finally {
                    next = (T) SequenceImpl.NULL;
                }
            }
        }

        return sequence(new LimitUntil());
    }

    /**
     * Partition a stream into two given a predicate.
     * <p>
     * <code><pre>
     * // tuple((1, 3, 5), (2, 4, 6))
     * Seq.of(1, 2, 3, 4, 5, 6).partition(i -> i % 2 != 0)
     * </pre></code>
     */
    static <T> Tuple2<Sequence<T>, Sequence<T>> partition(Stream<T> stream, Predicate<? super T> predicate) {
        final Iterator<T> it = stream.iterator();
        final LinkedList<T> buffer1 = new LinkedList<>();
        final LinkedList<T> buffer2 = new LinkedList<>();

        class Partition implements Iterator<T> {

            final boolean b;

            Partition(boolean b) {
                this.b = b;
            }

            void fetch() {
                while (buffer(b).isEmpty() && it.hasNext()) {
                    T next = it.next();
                    buffer(predicate.test(next)).offer(next);
                }
            }

            LinkedList<T> buffer(boolean test) {
                return test ? buffer1 : buffer2;
            }

            @Override
            public boolean hasNext() {
                fetch();
                return !buffer(b).isEmpty();
            }

            @Override
            public T next() {
                return buffer(b).poll();
            }
        }

        return tuple(sequence(new Partition(true)), sequence(new Partition(false)));
    }



    /**
     * Split a stream at the head.
     * <p>
     * <code><pre>
     * // tuple(1, (2, 3, 4, 5, 6))
     * Seq.of(1, 2, 3, 4, 5, 6).splitHead(3)
     * </pre></code>
     */
    static <T> Tuple2<Optional<T>, Sequence<T>> splitAtHead(Stream<T> stream) {
        Iterator<T> it = stream.iterator();
        return tuple(it.hasNext() ? Optional.of(it.next()) : Optional.empty(), sequence(it));
    }

    // Methods taken from LINQ
    // -----------------------

    /**
     * Keep only those elements in a stream that are of a given type.
     * <p>
     * <code><pre>
     * // (1, 2, 3)
     * Seq.of(1, "a", 2, "b", 3).ofType(Integer.class)
     * </pre></code>
     */
    @SuppressWarnings("unchecked")
    static <T, U> Sequence<U> ofType(Stream<T> stream, Class<U> type) {
        return sequence(stream).filter(type::isInstance).map(t -> (U) t);
    }

    /**
     * Cast all elements in a stream to a given type, possibly throwing a {@link ClassCastException}.
     * <p>
     * <code><pre>
     * // ClassCastException
     * Seq.of(1, "a", 2, "b", 3).cast(Integer.class)
     * </pre></code>
     */
    static <T, U> Sequence<U> cast(Stream<T> stream, Class<U> type) {
        return sequence(stream).map(type::cast);
    }

    // Shortcuts to Collectors
    // -----------------------

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#groupingBy(Function)} collector.
     */
    static <T, K> Map<K, List<T>> groupBy(Stream<T> stream, Function<? super T, ? extends K> classifier) {
        return sequence(stream).groupBy(classifier);
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#groupingBy(Function, Collector)} collector.
     */
    static <T, K, A, D> Map<K, D> groupBy(Stream<T> stream, Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream) {
        return sequence(stream).groupBy(classifier, downstream);
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#groupingBy(Function, Supplier, Collector)} collector.
     */
    static <T, K, D, A, M extends Map<K, D>> M groupBy(Stream<T> stream, Function<? super T, ? extends K> classifier, Supplier<M> mapFactory, Collector<? super T, A, D> downstream) {
        return sequence(stream).groupBy(classifier, mapFactory, downstream);
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#joining()}
     * collector.
     */
    static String join(Stream<?> stream) {
        return sequence(stream).join();
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#joining(CharSequence)}
     * collector.
     */
    static String join(Stream<?> stream, CharSequence delimiter) {
        return sequence(stream).join(delimiter);
    }

    /**
     * Shortcut for calling {@link Stream#collect(Collector)} with a
     * {@link Collectors#joining(CharSequence, CharSequence, CharSequence)}
     * collector.
     */
    static String join(Stream<?> stream, CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return sequence(stream).join(delimiter, prefix, suffix);
    }

    // Covariant overriding of Stream return types
    // -------------------------------------------

    @Override
    Sequence<T> filter(Predicate<? super T> predicate);

    @Override
    <R> Sequence<R> map(Function<? super T, ? extends R> mapper);

    @Override
    IntStream mapToInt(ToIntFunction<? super T> mapper);

    @Override
    LongStream mapToLong(ToLongFunction<? super T> mapper);

    @Override
    DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper);

    @Override
    <R> Sequence<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);

    @Override
    IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper);

    @Override
    LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper);

    @Override
    DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper);

    @Override
    Sequence<T> distinct();

    @Override
    Sequence<T> sorted();

    @Override
    Sequence<T> sorted(Comparator<? super T> comparator);

    @Override
    Sequence<T> peek(Consumer<? super T> action);

    @Override
    Sequence<T> limit(long maxSize);

    @Override
    Sequence<T> skip(long n);

    @Override
    Sequence<T> onClose(Runnable closeHandler);

    @Override
    void close();

    // These methods have no effect
    // ----------------------------

    @Override
    default Sequence<T> sequential() {
        return this;
    }

    @Override
    default Sequence<T> parallel() {
        return this;
    }

    @Override
    default Sequence<T> unordered() {
        return this;
    }

    @Override
    default Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }
}
