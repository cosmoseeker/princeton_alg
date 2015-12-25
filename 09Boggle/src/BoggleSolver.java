import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private static final int[] SCORE = { 0, 0, 0, 1, 1, 2, 3, 5 };
    private final Trie dic;
    private Trie valids;
    private char[][] boardArr;
    private boolean[][] visited;
    private int rows, cols;

    // Initializes the data structure using the given array of strings as the
    // dictionary.
    // (You can assume each word in the dictionary contains only the uppercase
    // letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dic = new Trie(dictionary);
    }

    // Returns the set of all valid words in the given Boggle board, as an
    // Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        List<String> words = new LinkedList<String>();
        valids = new Trie();
        rows = board.rows();
        cols = board.cols();
        boardArr = new char[rows + 2][cols + 2];
        visited = new boolean[rows + 2][cols + 2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boardArr[i + 1][j + 1] = board.getLetter(i, j);
            }
        }
        for (int i = 0; i < cols + 2; i++) {
            visited[0][i] = true;
            visited[rows + 1][i] = true;
        }
        for (int j = 1; j < rows + 1; j++) {
            visited[j][0] = true;
            visited[j][cols + 1] = true;
        }
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                StringBuilder sb = new StringBuilder();
                dfs(sb, i, j, words);
            }
        }
        valids = null;
        return words;
    }

    private void dfs(StringBuilder word, int row, int col, List<String> words) {
        visited[row][col] = true;
        char dice = boardArr[row][col];
        word.append(dice);
        int appendLen = 1;
        if (dice == 'Q') {
            word.append('U');
            appendLen = 2;
        }
        String prefix = word.toString();
        int check = dic.checkWord(prefix);
        if (check != 0) {
            if (check == 1)
                addWord(words, prefix);
            //int rowBorder, colBorder;
            for (int i = row > 1 ? row - 1 : 1; i <= row + 1; i++) {
                for (int j = col > 1 ? col - 1 : 1; j <= col + 1; j++) {
                    if (!visited[i][j]) {
                        dfs(word, i, j, words);
                    }
                }
            }
        }
        visited[row][col] = false;
        word.delete(word.length() - appendLen, word.length());
    }

    private void addWord(List<String> words, String word) {
        if (word.length() >= 3 && !valids.search(word))
            words.add(word);
        valids.insert(word);
    }

    /*private boolean valid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }*/

    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise.
    // (You can assume the word contains only the uppercase letters A through
    // Z.)
    public int scoreOf(String word) {
        int score = 0;
        if (dic.search(word)) {
            score = calScore(word);
        }
        return score;
    }

    private int calScore(String word) {
        int len = word.length();
        if (len > SCORE.length - 1) {
            return 11;
        }
        return SCORE[len];
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int count = 1;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(count++ + ": " + word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
