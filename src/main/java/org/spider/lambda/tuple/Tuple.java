/**
 * Copyright (c) 2014-2015, Data Geekery GmbH, contact@datageekery.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.spider.lambda.tuple;

import java.util.List;
import java.util.stream.Collector;

/**
 * A tuple.
 *
 * @author Amit Pandey
 */
public interface Tuple extends Iterable<Object> {

    /**
     * Construct a tuple of degree 1.
     */

    Tuple1 tuple1 = new Tuple1();
    static <T1> Tuple1<T1> tuple(T1 v1) {
        return new Tuple1<>(v1);
    }

    /**
     * Construct a tuple of degree 2.
     */
    static <T1, T2> Tuple2<T1, T2> tuple(T1 v1, T2 v2) {
        return new Tuple2<>(v1, v2);
    }


    /**
     * Construct a tuple collector of degree 1.
     */
    static <T, A1, D1> Collector<T, Tuple1<A1>, Tuple1<D1>> collectors(
        Collector<T, A1, D1> collector1
    ) {
        return Collector.of(
            () -> tuple(
                collector1.supplier().get()
            ),
            (a, t) -> {
                collector1.accumulator().accept(a.v1, t);
            },
            (a1, a2) -> tuple(
                collector1.combiner().apply(a1.v1, a2.v1)
            ),
            a -> tuple(
                collector1.finisher().apply(a.v1)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 2.
     */
    static <T, A1, A2, D1, D2> Collector<T, Tuple2<A1, A2>, Tuple2<D1, D2>> collectors(
        Collector<T, A1, D1> collector1
      , Collector<T, A2, D2> collector2
    ) {
        return Collector.of(
            () -> tuple(
                collector1.supplier().get()
              , collector2.supplier().get()
            ),
            (a, t) -> {
                collector1.accumulator().accept(a.v1, t);
                collector2.accumulator().accept(a.v2, t);
            },
            (a1, a2) -> tuple(
                collector1.combiner().apply(a1.v1, a2.v1)
              , collector2.combiner().apply(a1.v2, a2.v2)
            ),
            a -> tuple(
                collector1.finisher().apply(a.v1)
              , collector2.finisher().apply(a.v2)
            )
        );
    }

    /**
     * Construct a tuple collector of degree 3.
     */

    /**
     * Construct a tuple collector of degree 4.
     */

    /**
     * Create a new range.
     */
    static <T extends Comparable<T>> Range<T> range(T t1, T t2) {
        return new Range<>(t1, t2);
    }

    /**
     * Get an array representation of this tuple.
     */
    Object[] array();

    /**
     * Get a list representation of this tuple.
     */
    List<?> list();

    /**
     * The degree of this tuple.
     */
    int degree();

    static Tuple empty() {
        return tuple1;
    }
}
