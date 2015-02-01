# spider

Spider is a helper library for Java 8 designed to help with functional programming.
Spider provides the <b>Sequence</b> class which enhances the Stream class with various utility functionas like
join,foldLeft, foldRight etc.

For example to get only distinct elements from a List use 
```java
        org.spider.lambda.Sequence<Integer> seq = org.spider.lambda.Sequence.of(1, 2, 3, 4, 4);
        List<Integer> list = seq.distinct().collect(Collectors.toList());
        Assert.assertTrue(list.size() == 4);
```
If you want to use foldLeft or foldRight you might want to use :- 

```java
        ArrayList<String> list2 = new ArrayList<>();
        list2.add("A");
        list2.add("m");
        list2.add("i");
        list2.add("t");
        System.out.println(list2);
        Sequence<String> seq3 = Sequence.sequence(list2);
        String name = seq3.foldLeft("", (x,y) -> x+y);
        System.out.println(name);
        Assert.assertTrue(name.equals("Amit"));
```
