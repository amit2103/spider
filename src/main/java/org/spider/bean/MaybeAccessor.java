package org.spider.bean;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface MaybeAccessor<T, V> extends BeanMethods<T, Optional<V>> {
    static<T, V> org.spider.bean.MaybeAccessor<T, V> wrapping(Function<T, Optional<V>> getter, BiConsumer<T, Optional<V>> setter) {
        return new org.spider.bean.MaybeAccessor<T, V>() {
            @Override public void accept(T target, Optional<V> newValue) {
                setter.accept(target, newValue);
            }

            @Override public Optional<V> apply(T target) {
                return getter.apply(target);
            }
        };
    }

    default BeanMethods<T, V> ensuringPresent(Supplier<V> missingValueSupplier) {
        return BeanMethods.of(t -> {
            Optional<V> maybeValue = get(t);
            if (!maybeValue.isPresent()) {
                maybeValue = Optional.of(missingValueSupplier.get());
                set(t, maybeValue);
            }
            return maybeValue.orElse(null);
        },
        (t, v) -> set(t, Optional.of(v)));
    }

    default <V2> org.spider.bean.MaybeAccessor<T, V2> flatMap(BeanMethods<V, Optional<V2>> next, Supplier<V> missingValueSupplier) {
        return wrapping(
            t -> get(t).flatMap(next),
            (t, v2) -> next.set(ensuringPresent(missingValueSupplier).get(t), v2));
    }

    default <V2> org.spider.bean.MaybeAccessor<T, V2> map(BeanMethods<V, V2> next, Supplier<V> missingValueSupplier, V2 defaultValue) {
        return wrapping(
            t -> get(t).map(next),
            (t, v2) -> next.set(ensuringPresent(missingValueSupplier).get(t), v2.orElse(defaultValue)));
    }
}
