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
Lets suppose you need to Zip two streasm into one. The JDK Streams Api does not provide anything for it. You can however use the Spider Sequence.zip method for it. The code below provides an example :- 

```java
Sequence.of(1, 2, 3).zip(Sequence.of("amit", "babi", "arav")) 
//produces  (tuple(1, "amit"), tuple(2, "babi"), tuple(3, "arav"))

```

To unfold a function into a stream, use the code below:- 
```java
  Sequence seq3 = Sequence.unfold(20, i -> i >= 6 ? Optional.of(Tuple.tuple(i, i - 1)) : Optional.empty());
  seq3.forEach(i -> System.out.println(i));
  //this prints 20191817161514131211109876 (sorry for no spaces)

```

Generally, Exceptions are not suitable for functional programming. So you can use the Either class. You can also use the Try structure (this is heaviliy inspired by the Scala Try ) to help in these kinds of scnerios. The Java code below gives an idea about how to use Try structure :-

```java
        //when called this method might produce failue (in this case always produces failue :) )
        private Try<String> failure() {
        return Try.of(() -> {
            throw new RuntimeException();
        });
    }
    //this will always return a success.
    private Try<String> ok() {
        return Try.of(() -> "this should be returned");
    }
    
    Try<String> tr = failure();
    Try<String> tr1 = ok();
    String result = tr.orElse("hello");
    Assert.assertTrue(result.equals("hello"));
    Assert.assertTrue(tr1.get().equals("this should be returned"));
    
```






