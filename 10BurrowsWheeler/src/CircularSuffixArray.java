import java.util.Arrays;

public class CircularSuffixArray {
    private final int[] sufIdx;
    private final int len;
    private final char[] str;

    private class Suffix implements Comparable<Suffix> {
        private int begin;

        public Suffix(int b) {
            begin = b;
        }

        @Override
        public int compareTo(Suffix that) {
            for (int i = 0; i < len; i++) {
                char c1 = str[(this.begin + i) % len], c2 = str[(that.begin + i)
                        % len];
                if (c1 != c2) {
                    return c1 - c2;
                }
            }
            return 0;
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new java.lang.NullPointerException();
        }
        len = s.length();
        sufIdx = new int[len];
        str = s.toCharArray();
        // sort
        Suffix[] suf = new Suffix[len];
        for (int i = 0; i < len; i++) {
            suf[i] = new Suffix(i);
        }
        Arrays.sort(suf);
        for (int i = 0; i < len; i++) {
            sufIdx[i] = suf[i].begin; // sufIdx[suf[i].begin] = i;
        }
    }

    // length of s
    public int length() {
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= len) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        return sufIdx[i];
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String str = "AAABAAAABB";
        CircularSuffixArray csa = new CircularSuffixArray(str);
        System.out.printf("%2d : ", 0);
        System.out.println(str);
        for (int i = 0; i < csa.length(); i++) {
            System.out.printf("%2d : ", csa.index(i));
            System.out.print(str.substring(csa.index(i)));
            System.out.println(str.substring(0, csa.index(i)));
        }
    }
}
