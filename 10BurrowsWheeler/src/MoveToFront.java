import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static int R = 256;
    private static char[] char2code = new char[R];
    private static char[] code2char = new char[R];

    // apply move-to-front encoding, reading from standard input and writing to
    // standard output
    public static void encode() {
        // init
        initCode();
        while (!BinaryStdIn.isEmpty()) { // while has input
            // write
            char c = BinaryStdIn.readChar();
            char code = char2code[c];
            BinaryStdOut.write(code);
            // move
            maintain(c, code);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to
    // standard output
    public static void decode() {
        initCode();
        while (!BinaryStdIn.isEmpty()) { // while has input
            // write
            char code = BinaryStdIn.readChar();
            char c = code2char[code];
            BinaryStdOut.write(c);
            // move
            maintain(c, code);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    private static void initCode() {
        for (char i = 0; i < R; i++) {
            char2code[i] = i;
            code2char[i] = i;
        }
    }

    private static void maintain(char c, char code) {
        if (code > 0) {
            // code2char
            System.arraycopy(code2char, 0, code2char, 1, code);
            code2char[0] = c;
            // char2code
            for (char pos = 0; pos <= code; pos++) {
                char2code[code2char[pos]]= pos; // f(g(x)) = x
            }
        }
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
    }
}
