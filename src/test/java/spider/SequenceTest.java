package spider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.spider.lambda.Sequence;
import org.spider.lambda.tuple.Tuple;

public class SequenceTest {

    @Test
    public void test() {
        org.spider.lambda.Sequence<Integer> seq = org.spider.lambda.Sequence.of(1, 2, 3, 4, 4);
        List<Integer> list = seq.distinct().collect(Collectors.toList());
        Assert.assertTrue(list.size() == 4);
        org.spider.lambda.Sequence<String> seq1 = Sequence.of("abc", "xyz", "mnb");
        String str = seq1.findFirst((s) -> s.equals("abc")).get();
        Stream<Integer> s1 = Sequence.of(1, 2, 3, 4).concat(5);
        list = s1.collect(Collectors.toList());
        Assert.assertTrue(list.contains(5));

        Sequence<Integer> seq2 = Sequence.ofType(list.stream(), Integer.class);
        int sum = seq2.foldLeft(0, (x,y) -> x +y);
        Assert.assertTrue(sum==15);

        testfoldRight();

    }
    @Test
    public void testfoldRight() {
        ArrayList<String> list2 = new ArrayList<>();
        list2.add("A");
        list2.add("m");
        list2.add("i");
        list2.add("t");
        System.out.println(list2);
        Sequence<String> seq3 = Sequence.sequence(list2);
        String name = seq3.foldLeft("s", (x,y) -> x+y);
        System.out.println(name);
        Assert.assertTrue(name.equals("Amit"));
    }

    @Test
    public void testFold() {
        Sequence<Integer> seq = Sequence.of(1,2,3,4,5,6,7);
        int sum = seq.map((x)-> x * 2).foldLeft(0,(x,y) -> x +y);
        Assert.assertTrue(sum == 56);
        seq = Sequence.sequence(Stream.of(1,2,3,4,5,6,7,8,9));
        seq = Sequence.cycle(Stream.of(1,2,3));
        String join = seq.limit(10).join();
        Assert.assertTrue(join.length() > 9);


        Sequence seq3 = Sequence.unfold(20, i -> i >= 6 ? Optional.of(Tuple.tuple(i, i - 1)) : Optional.empty());
        seq3.forEach(i -> System.out.print(i));
    }

}
