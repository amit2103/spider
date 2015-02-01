package org.spider.bean;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface MaybeGetter<T, V> extends Getter<T, Optional<V>> {
    default Getter<T, V> assertPresent() { return t -> apply(t).orElseThrow(NullPointerException::new); }
    default <V2> org.spider.bean.MaybeGetter<T, V2> flatMap(Function<V, Optional<V2>> next) {
        return t -> apply(t).flatMap(next);
    }

    default <V2> org.spider.bean.MaybeGetter<T, V2> map(Function<V, V2> next) {
        return t -> apply(t).map(next);
    }

    default Getter<T, V> orElse(V defaultValue) {
        return t -> apply(t).orElse(defaultValue);
    }

    default Getter<T, V> orElseGet(Supplier<V> defaultValueSupplier) {
        return t -> apply(t).orElseGet(defaultValueSupplier);
    }
}
