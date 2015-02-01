/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package org.spider.lambda;

import org.spider.lambda.function.Function0;


/**
 * Defines a Monad by generalizing the flatMap and unit functions.
 *
 *
 * @param <T1> Component type of this Monad1.
 */
public interface Monad1<T1, M extends HigherKinded1<?, M>> extends Function0<T1>, HigherKinded1<T1, M> {

    <U1, MONAD extends HigherKinded1<U1, M>> Monad1<U1, M> flatMap(java.util.function.Function<? super T1, MONAD> f);
}