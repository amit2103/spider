package org.spider.lambda.util;

import java.io.Serializable;

import org.spider.lambda.tuple.Tuple;

/**
 * Value Objects are immutable, decomposable objects with an identity based on their type and encapsulated values.
 */
public interface ValueObject extends Serializable {

    /**
     * Decomposes this object into its parts.
     *
     * @return A Tuple of parts of the construction of this object.
     */
    Tuple unapply();

    // -- Object.*

    /**
     * Checks if o equals this.
     *
     * <pre>
     *     <code>
     *     if (o == this) {
     *         return true;
     *     } else if (!(o instanceof CurrentType)) {
     *         return false;
     *     } else {
     *         final CurrentType that = (CurrentType) o;
     *         return ...; // check if values of this and that are pairwise equal
     *     }
     *     </code>
     * </pre>
     *
     * @param o An object, may be null.
     * @return true, if o equals this, false otherwise.
     */
    @Override
    boolean equals(Object o);

    /**
     * Needs to be overridden because of equals.
     *
     * @return The hashCode of this object.
     */
    @Override
    int hashCode();

    /**
     * Returns a String representation of this object including type and state.
     *
     * @return A String representation of this object.
     */
    @Override
    String toString();
}
