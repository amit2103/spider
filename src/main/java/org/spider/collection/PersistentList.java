package org.spider.collection;

import java.util.List;

public interface PersistentList<E> extends PersistentStack<E>, List<E> {
    @Override
    PersistentList<E> zero();

    /**
     * @return A new {@link PersistentList} consisting of the value val followed
     *         by the element of the current {@link PersistentList}.
     */
    @Override
    PersistentList<E> plus(E val);

    /**
     * @return A new {@link PersistentList} consisting of the elements of the
     *         current {@link PersistentList} without its first element.
     */
    @Override
    PersistentList<E> minus();
}
