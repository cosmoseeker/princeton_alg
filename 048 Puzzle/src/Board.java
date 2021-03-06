import java.util.Arrays;

import edu.princeton.cs.algs4.Queue;

public class Board {
    private final int[][] blocks;
    private final int     dimension;

    /*
     * construct a board from an N-by-N array of blocks (where blocks[i][j] =
     * block in row i, column j)
     */
    public Board(int[][] blocks) {
        this.dimension = blocks.length;
        // this.board = (int[][]) blocks.clone();
        this.blocks = new int[dimension][dimension];
        for (int i = 0; i < dimension; ++i)
            for (int j = 0; j < dimension; ++j) {
                this.blocks[i][j] = blocks[i][j];
            }
    }

    // board dimension N
    public int dimension() {
        return this.dimension;
    }

    // number of blocks out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension && i + j < 2 * this.dimension - 2; ++j) {
                if (this.blocks[i][j] != i * dimension + j + 1) {
                    ++hamming;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattan = 0;
        int x, y;
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (this.blocks[i][j] != 0) {
                    x = (this.blocks[i][j] - 1) / dimension;
                    y = (this.blocks[i][j] - 1) % dimension;
                    manhattan += Math.abs(x - i);
                    manhattan += Math.abs(y - j);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension - 1; ++j) {
                if (this.blocks[i][j] != i * dimension + j + 1) {
                    return false;
                }
            }
        }
        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] newBlocks = new int[this.dimension][this.dimension];
        for (int i = 0; i < this.dimension; ++i) {
            for (int j = 0; j < this.dimension; ++j) {
                newBlocks[i][j] = this.blocks[i][j];
            }
        }
        boolean flag = true;
        for (int i = 0; flag && i < this.dimension; ++i) {
            for (int j = 1; j < this.dimension; ++j) {
                if (newBlocks[i][j] != 0 && newBlocks[i][j - 1] != 0) {
                    int tmp = newBlocks[i][j - 1];
                    newBlocks[i][j - 1] = newBlocks[i][j];
                    newBlocks[i][j] = tmp;
                    flag = false;
                    break;
                }
            }
        }
        return new Board(newBlocks);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (this.getClass() != y.getClass())
            return false;
        Board that = (Board) y;
        if (this.dimension != that.dimension)
            return false;
        for (int i = 0; i < this.dimension; ++i)
            for (int j = 0; j < this.dimension; ++j)
                if (this.blocks[i][j] != that.blocks[i][j])
                    return false;
        return true;
    }

    // all neighboring boards
    // don't need to use PQ. Just get, then input into PQ in Solver
    public Iterable<Board> neighbors() {
        int blank_i = this.dimension;
        int blank_j = this.dimension;
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                if (blocks[i][j] == 0) {
                    blank_i = i;
                    blank_j = j;
                }
            }
        }
        Queue<Board> q = new Queue<Board>();
        if (blank_j - 1 >= 0) {
            int[][] arr_temp = getCopy();
            arr_temp[blank_i][blank_j] = arr_temp[blank_i][blank_j - 1];
            arr_temp[blank_i][blank_j - 1] = 0;
            q.enqueue(new Board(arr_temp));
        }
        if (blank_j + 1 < this.dimension) {
            int[][] arr_temp = getCopy();
            arr_temp[blank_i][blank_j] = arr_temp[blank_i][blank_j + 1];
            arr_temp[blank_i][blank_j + 1] = 0;
            q.enqueue(new Board(arr_temp));
        }
        if (blank_i - 1 >= 0) {
            int[][] arr_temp = getCopy();
            arr_temp[blank_i][blank_j] = arr_temp[blank_i - 1][blank_j];
            arr_temp[blank_i - 1][blank_j] = 0;
            q.enqueue(new Board(arr_temp));
        }
        if (blank_i + 1 < this.dimension) {
            int[][] arr_temp = getCopy();
            arr_temp[blank_i][blank_j] = arr_temp[blank_i + 1][blank_j];
            arr_temp[blank_i + 1][blank_j] = 0;
            q.enqueue(new Board(arr_temp));
        }
        return q;
    }

    // string representation of this board (in the output format specified
    // below)
    public String toString() {
        StringBuilder sb = new StringBuilder(dimension + "\n");
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                sb.append(" " + this.blocks[i][j] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private int[][] getCopy() {
        int[][] result = new int[this.dimension][];
        for (int i = 0; i < this.dimension; i++) {
            result[i] = Arrays.copyOf(blocks[i], this.dimension);
        }
        return result;
    }

    // unit tests (not graded)
    public static void main(String[] args) {

    }

}
