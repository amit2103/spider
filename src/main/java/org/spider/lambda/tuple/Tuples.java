package org.spider.lambda.tuple;

/**
 * @author Amit Pandey
 */
final class Tuples {

    @SuppressWarnings("unchecked")
    static <T> int compare(T t1, T t2) {
        return t1 == null && t2 == null
             ? 0
             : t1 == null
             ? 1
             : t2 == null
             ? -1
             : ((Comparable<T>) t1).compareTo(t2);
    }
}
