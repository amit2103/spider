
package org.spider.util;

import junit.framework.Assert;

import org.junit.Test;
import org.spider.lambda.util.Try;

public class TryTest {

    private void unitOfWork() {
        // System.out.
    }

    @Test
    public void testTry() {
        Try<String> tr = failure();

        Try<String> tr1 = ok();
        tr1.isSuccess();
        Assert.assertTrue(tr.isFailure());
        String result = tr.orElse("hello");
        Assert.assertTrue(result.equals("hello"));
    }

    private Try<String> failure() {
        return Try.of(() -> {
            throw new RuntimeException();
        });
    }

    private Try<String> ok() {
        return Try.of(() -> "this should be returned");
    }

}
