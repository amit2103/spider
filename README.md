# spider

Spider is a helper library for Java 8 designed to help with functional programming.
Spider provides the <b>Sequence</b> class which enhances the Stream class with various utility functionas like
join,foldLeft, foldRight etc.


```java
        org.spider.lambda.Sequence<Integer> seq = org.spider.lambda.Sequence.of(1, 2, 3, 4, 4);
        List<Integer> list = seq.distinct().collect(Collectors.toList());
        Assert.assertTrue(list.size() == 4);
        org.spider.lambda.Sequence<String> seq1 = Sequence.of("abc", "xyz", "mnb");
        String str = seq1.findFirst((s) -> s.equals("abc")).get();
        Stream<Integer> s1 = Sequence.of(1, 2, 3, 4).concat(5);
        list = s1.collect(Collectors.toList());
        Assert.assertTrue(list.contains(5));
        

```
