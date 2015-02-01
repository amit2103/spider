package org.spider.bean;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface BeanMethods<T, V> extends Getter<T, V>, Setter<T, V> {

    static <T> BeanMethods<T[], T> forArray(int index) {
        return BeanMethods.of(Getter.fromArray(index), Setter.toArray(index));
    }

    static <T> BeanMethods<List<T>, T> forList(int index) {
        return BeanMethods.of(Getter.fromList(index), Setter.toList(index));
    }

    static <K, V> MaybeAccessor<Map<K, V>, V> forMap(K key) {
        return MaybeAccessor.wrapping(Getter.<K, V>fromMap(key), Setter.<K, V>toMap(key));
    }

    static<T, V> BeanMethods<T, V> of(Function<T, V> getter, BiConsumer<T, V> setter) {
        return new BeanMethods<T, V>() {
            @Override public void accept(T target, V newValue) {
                setter.accept(target, newValue);
            }

            @Override public V apply(T target) {
                return getter.apply(target);
            }
        };
    }

    static <T, V> MaybeAccessor<T, V> ofNullable(Function<T, V> getter, BiConsumer<T, V> setter) {
        return MaybeAccessor.<T, V>wrapping(Getter.ofNullable(getter), Setter.ofNullable(setter));
    }

    static <T, V> MaybeAccessor<T, V> ofOptional(Function<T, Optional<V>> getter, BiConsumer<T, Optional<V>> setter) {
        return MaybeAccessor.<T, V>wrapping(getter, setter);
    }

    default V getAndUpdate(T target, UnaryOperator<V> updater) {
        V oldValue = get(target);
        set(target, updater.apply(oldValue));
        return oldValue;
    }

    default <V2> BeanMethods<T, V2> join(BeanMethods<V, V2> next) {
        return of(t -> next.apply(get(t)),
            (t, v2) -> next.accept(get(t), v2));
    }

    default V swap(T target, V newValue) {
        V oldValue = get(target);
        set(target, newValue);
        return oldValue;
    }

    default Ref<V> toRef(T target) {
        return new Ref<V>() {
            @Override
            public void accept(V v) {
                BeanMethods.this.set(target, v);
            }

            @Override
            public V get() {
                return BeanMethods.this.get(target);
            }
        };
    }

    default void update(T target, UnaryOperator<V> updater) {
        set(target, updater.apply(get(target)));
    }

    default V updateAndGet(T target, UnaryOperator<V> updater) {
        V newValue = updater.apply(get(target));
        set(target, newValue);
        return newValue;
    }

}
