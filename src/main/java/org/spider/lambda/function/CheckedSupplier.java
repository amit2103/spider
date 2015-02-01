
package org.spider.lambda.function;

import java.util.function.Supplier;
/**
* A {@link Supplier} that allows for checked exceptions.

*/
@FunctionalInterface
public interface CheckedSupplier<T> {
/**
* Gets a result.
*
* @return a result
*/
T get() throws Throwable;
}
