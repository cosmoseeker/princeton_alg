package alg1;
import java.io.File;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;

public class Inversions {
    public static void main(String[] args) {
        if (args.length == 1) {
            File file = new File(args[0]);
            if (file.exists()) {
                In in = new In(file);
                int[] nums = in.readAllInts();
                long inver = inversions(nums, 0, nums.length);
                System.out.println(inver);
            }
        } else {
            int[] nums = new int[] { 1, 3, 5, 7, 2, 4, 6 };
            int len = 7;
            long inver = inversions(nums, 0, len);
            System.out.println(inver);
        }
    }

    private static long inversions(int[] nums, int low, int high) {
        if (high - low <= 1)
            return 0;
        int mid = (low + high) / 2;
        long inverL = inversions(nums, low, mid);
        long inverR = inversions(nums, mid, high);
        long inver = mergeInver(nums, low, mid, high);
        return inverL + inverR + inver;
    }

    private static long mergeInver(int[] nums, int low, int mid, int high) {
        if (high - low <= 1)
            return 0;
        int[] left = Arrays.copyOfRange(nums, low, mid);
        int[] right = Arrays.copyOfRange(nums, mid, high);
        long inver = 0;
        int l = 0, r = 0, index = low;
        high -= mid;
        mid -= low;
        while (l < mid && r < high) {
            if (left[l] <= right[r]) {
                /*for (int k = 0; k < r; k++) {
                    System.out.println(left[l] + ", " + right[k] + ", "
                            + (left[l] > right[k]));
                }*/
                nums[index++] = left[l++];
                inver += r;
            } else {
                nums[index++] = right[r++];
            }
        }
        if (l == mid) {// left end
            while (r < high)
                nums[index++] = right[r++];
            return inver;
        }
        if (r == high) {// right end
            while (l < mid) {
                /*for (int k = 0; k < high; k++) {
                    System.out.println(left[l] + ", " + right[k] + ", "
                            + (left[l] > right[k]));
                }*/
                nums[index++] = left[l++];
                inver += right.length;
            }
            return inver;
        }
        return inver;
    }
}
