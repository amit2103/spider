package org.spider.lambda.util;

import java.util.Objects;

import org.spider.lambda.tuple.Tuple;
import org.spider.lambda.tuple.Tuple1;

public final class Left<L, R> implements Either<L, R> {

    private static final long serialVersionUID = 3297057402720487673L;

    final L left;

    public Left(L left) {
        this.left = left;
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public Tuple1<L> unapply() {
        return Tuple.tuple(left);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof Left && Objects.equals(left, ((Left<?, ?>) obj).left));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(left);
    }

    @Override
    public String toString() {
        return String.format("Left(%s)", left);
    }
}
