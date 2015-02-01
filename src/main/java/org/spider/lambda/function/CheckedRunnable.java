
package org.spider.lambda.function;

import java.io.Serializable;

/**
 * Checked version of java.lang.Runnable.
 * Essentially the same as {@code CheckedFunction0<Void>}, or short {@code X0<Void>}.
 */
@FunctionalInterface
public interface CheckedRunnable extends Serializable {

    void run() throws Throwable;
}
