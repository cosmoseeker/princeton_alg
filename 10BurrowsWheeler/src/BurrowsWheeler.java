import java.util.Arrays;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler encoding, reading from standard input and writing
    // to standard output
    public static void encode() {
        int first = 0;
        String str = BinaryStdIn.readString();
        int len = str.length();
        CircularSuffixArray csa = new CircularSuffixArray(str);
        for (; first < csa.length(); first++) {
            if (csa.index(first) == 0) {
                break;
            }
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < csa.length(); i++) {
            int idx = (csa.index(i) + len - 1) % len;
            BinaryStdOut.write(str.charAt(idx));
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing
    // to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int len = t.length();
        int[] next = new int[len];
        char[] sorted = t.toCharArray();
        Arrays.sort(sorted);
        int[] start = new int[R];
        Arrays.fill(start, -1);
        for (int i = 0; i < len; i++) {
            char c = sorted[i];
            next[i] = t.indexOf(c, start[c] + 1);
            start[c] = next[i];
        }
        for (int i = 0; i < len; i++) {
            BinaryStdOut.write(sorted[first]);
            first = next[next[first]];
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("usage: java BurrowsWheeler - < somefile.txt");
        } else if (args[0].equals("+")) {
            decode();
        } else if (args[0].equals("-")) {
            encode();
        } else {
            System.out
                    .printf("\n\nIllegial argument. Only \"+\" or \"-\""
                            + " are accepted.");
        }
    }
}
