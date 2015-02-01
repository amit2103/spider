package org.spider.collection;

import java.util.Collection;

/**
 * @param <E> The type of objects held in the collection.
 */
public interface PersistentCollection<E> extends Collection<E> {
    /**
     * @return An empty instance of this kind of collection.
     */
    PersistentCollection<E> zero();

    /**
     * @return A new collection consisting of all elements of the current
     *         collection together with the value val.
     */
    PersistentCollection<E> plus(E val);
}
