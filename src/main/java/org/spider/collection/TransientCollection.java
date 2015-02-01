package org.spider.collection;

/**
 * A {@link TransientCollection} is a collection that does not retain older
 * versions in order to gain efficient modifications.
 */
public interface TransientCollection<E> {
    /**
     * @return A new {@link TransientCollection} consisting of all the elements
     *         of the current collection together with the value val (no
     *         guarantees are made on the current collection).
     */
    TransientCollection<E> plus(E val);

    /**
     * @return A corresponding {@link PersistentCollection} consisting of the
     *         elements of the current {@link TransientCollection}.
     */
    PersistentCollection<E> persist();
}
