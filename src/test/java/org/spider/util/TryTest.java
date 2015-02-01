/**
 * Copyright (c) 2014-2015, Data Geekery GmbH, contact@datageekery.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
