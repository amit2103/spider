package org.spider.lambda;



public interface HigherKinded1<T1, TYPE extends HigherKinded1<?, TYPE>> {

    // used for type declaration only
}