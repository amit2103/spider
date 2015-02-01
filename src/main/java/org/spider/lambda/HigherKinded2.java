
package org.spider.lambda;


public interface HigherKinded2<T1, T2, TYPE extends HigherKinded2<?, ?, TYPE>> {

    // used for type declaration only
}