package org.spider.lambda.util;

import java.util.Objects;

import org.spider.lambda.tuple.Tuple;
import org.spider.lambda.tuple.Tuple1;

public final class Right<L, R> implements Either<L, R> {

    private static final long serialVersionUID = 6037923230455552437L;

    final R right;

    public Right(R right) {
        this.right = right;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public Tuple1<R> unapply() {
        return Tuple.tuple(right);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof Right && Objects.equals(right, ((Right<?, ?>) obj).right));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(right);
    }

    @Override
    public String toString() {
        return String.format("Right(%s)", right);
    }
}
