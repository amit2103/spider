
package org.spider.lambda;

import org.spider.lambda.function.Function1;


public interface Monad2<T1, T2, M extends HigherKinded2<?, ?, M>> extends Function1<T1, T2>, HigherKinded2<T1, T2, M> {

    <U1, U2, MONAD extends HigherKinded2<U1, U2, M>> Monad2<U1, U2, M> flatMap(java.util.function.BiFunction<? super T1, ? super T2, MONAD> f);
}