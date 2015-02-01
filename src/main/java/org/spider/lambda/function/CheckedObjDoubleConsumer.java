
package org.spider.lambda.function;

import java.io.Serializable;

/**
 * Checked version of java.util.function.ObjDoubleConsumer.
 * Essentially the same as {@code CheckedFunction2<T, Double, Void>}, or short {@code X2<T, Double, Void>}.
 *
 * @param <T> Argument type
 */
@FunctionalInterface
public interface CheckedObjDoubleConsumer<T> extends Serializable {

    void accept(T t, double value) throws Throwable;
}
