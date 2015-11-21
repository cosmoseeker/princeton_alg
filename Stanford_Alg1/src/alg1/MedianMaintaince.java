package alg1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.MinPQ;

public class MedianMaintaince {
    private static final int MOD = 10000;

    public static void main(String[] args) {
        In in = new In(args[0]);
        int[] arr = //{ 11, 3, 6, 9, 2, 8, 4, 10, 1, 12, 7, 5 };
        in.readAllInts();
        MinPQ<Integer> min = new MinPQ<Integer>();
        MaxPQ<Integer> max = new MaxPQ<Integer>();
        int num, result = 0;
        for (int i = 0; i < arr.length; ++i) {
            num = arr[i];
            if (min.size() == max.size()) {
                if (max.size() > 0 && num < max.max()) {
                    min.insert(max.delMax());
                    max.insert(num);
                } else {
                    min.insert(num);
                }
                result = (result + min.min()) % MOD;
                System.out.println(min.min());
            } else if (min.size() > max.size()) {
                if (num > min.min()) {
                    max.insert(min.delMin());
                    min.insert(num);
                } else {
                    max.insert(num);
                }
                result = (result + max.max()) % MOD;
                System.out.println(max.max());
            } else {
                System.out.println("in else");
            }

            /*
             * if(max.size() == 0 ||num < max.max()) max.insert(num); else
             * min.insert(num);
             * 
             * if(max.size() - min.size() > 1) { min.insert(max.max()); }
             * if(min.size() > max.size()) { max.insert(min.min()); }
             * 
             * result += max.max();
             */
        }
        System.out.println(result);
    }
}
