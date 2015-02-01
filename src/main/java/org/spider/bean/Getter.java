package org.spider.bean;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Getter<T, V> extends Function<T, V> {

    static <T> Getter<T[], T> fromArray(int index) {
        return a -> a[index];
    }
    static <T> Getter<List<T>, T> fromList(int index) {
        return l -> l.get(index);
    }
    static <K, V> MaybeGetter<Map<K, V>, V> fromMap(K key) {
        return m -> Optional.ofNullable(m.get(key));
    }

    static <T, V> Getter<T, V> of(Function<T, V> f) {
        return f::apply;
    }

    static <T, V> MaybeGetter<T, V> ofNullable(Function<T, V> f) {
        return of(f).optional();
    }
    static <T, V> MaybeGetter<T, V> ofOptional(Function<T, Optional<V>> f) {
        return f::apply;
    }

    default Supplier<V> from(T target) {
        return () -> get(target);
    }

    default V get(T target) {
        return apply(target);
    }

    default MaybeGetter<T, V> optional() {
        return andThen(Optional::ofNullable)::apply;
    }

}
