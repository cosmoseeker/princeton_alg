package alg1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.MinPQ;

public class MedianMaintaince {
    private static final int MOD = 10000;

    public static void main(String[] args) {
        In in = new In(args[0]);
        int[] arr = // { 11, 3, 6, 9, 2, 8, 4, 10, 1, 12, 7, 5 };
        in.readAllInts();
        MinPQ<Integer> min = new MinPQ<Integer>();
        MaxPQ<Integer> max = new MaxPQ<Integer>();
        int num, result = 0;
        for (int i = 0; i < arr.length; ++i) {
            num = arr[i];
            if (min.size() == max.size()) {
                min.insert(num);
                max.insert(min.delMin());
            } else if (min.size() < max.size()) {
                max.insert(num);
                min.insert(max.delMax());
            }
            result = (result + max.max()) % MOD;
        }
        System.out.println(result);
    }
}
