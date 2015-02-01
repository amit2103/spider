
package org.spider.lambda.function;

import java.io.Serializable;


@FunctionalInterface
public interface CheckedObjIntConsumer<T> extends Serializable {

    void accept(T t, int value) throws Throwable;
}
