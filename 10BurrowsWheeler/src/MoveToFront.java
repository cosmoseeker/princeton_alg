import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to
    // standard output
    public static void encode() {
        char[] code2char = new char[R];
        for (char i = 0; i < R; i++) {
            code2char[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) { // while has input
            char c = BinaryStdIn.readChar();
            char code = 0, prevc = code2char[code], tmp;
            while (prevc != c) {
                code++;
                tmp = code2char[code];
                code2char[code] = prevc;
                prevc = tmp;
            }
            BinaryStdOut.write(code);
            if (code > 0) {
                code2char[0] = c;
            }
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to
    // standard output
    public static void decode() {
        char[] code2char = new char[R];
        for (char i = 0; i < R; i++) {
            code2char[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) { // while has input
            char code = BinaryStdIn.readChar();
            char c = code2char[code];
            BinaryStdOut.write(c);
            if (code > 0) {
                System.arraycopy(code2char, 0, code2char, 1, code);
                code2char[0] = c;
            }
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}
