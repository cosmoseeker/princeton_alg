package alg1;
import java.io.File;

import edu.princeton.cs.algs4.In;

public class QuickSort {

    public static void main(String[] args) {
        if (args.length == 1) {
            File file = new File(args[0]);
            if (file.exists()) {
                In in = new In(file);
                int[] nums = in.readAllInts();
                long inver = quickSort(nums, 0, nums.length);
                System.out.println(inver);
            }
        } else {
            int[] nums = new int[] { 1, 3, 5, 7, 2, 4, 6 };
            int len = 7;
            long inver = quickSort(nums, 0, len);
            System.out.println(inver);
        }
    }

    private static long quickSort(int[] nums, int low, int high) {
        int pivot;
        long lCount = 0, rCount = 0, count = high - low - 1;
        if (low >= high) {
            return 0;
        }
        pivot = partition(nums, low, high);
        lCount = quickSort(nums, low, pivot);
        rCount = quickSort(nums, pivot + 1, high);
        return count + lCount + rCount;
    }

    private static int partition(int[] nums, int low, int high) {
        // swap(nums, low, high - 1); //user last as pivot
        median(nums, low, high); // use median as pivot
        int pivot = nums[low];
        int smallerIndex = low + 1, largerIndex = low + 1;
        for (; largerIndex < high; ++largerIndex) {
            if (nums[largerIndex] < pivot) {
                swap(nums, smallerIndex, largerIndex);
                ++smallerIndex;
            }
        }
        swap(nums, low, smallerIndex - 1);
        return smallerIndex - 1;
    }

    private static void median(int[] nums, int low, int high) {
        int median = (low + high - 1) / 2, pivot = low;
        if (mid(nums, median, low, high - 1)) {
            pivot = median;
            swap(nums, low, pivot);
        } else if (mid(nums, high - 1, low, median)) {
            pivot = high - 1;
            swap(nums, low, pivot);
        }
    }

    private static boolean mid(int[] nums, int mid, int x1, int x2) {
        return ((nums[mid] >= nums[x1] && nums[mid] <= nums[x2]) || (nums[mid] <= nums[x1] && nums[mid] >= nums[x2]));
    }

    private static void swap(int[] nums, int smallerIndex, int largerIndex) {
        int tmp = nums[smallerIndex];
        nums[smallerIndex] = nums[largerIndex];
        nums[largerIndex] = tmp;
        /*
         * nums[smallerIndex] ^= nums[largerIndex]; nums[largerIndex] ^=
         * nums[smallerIndex]; nums[smallerIndex] ^= nums[largerIndex];
         */
    }

}
