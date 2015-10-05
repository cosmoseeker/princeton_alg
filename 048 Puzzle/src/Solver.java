public class Solver {
    private class Node{
        private Board board;
        private Node parent;
    }
    private final Node root;
    // find a solution to the initial board(using the A* algorithm)
    public Solver(Board initial) {
        if(initial == null)
            throw new java.lang.NullPointerException();
        this.root = new Node();
        this.root.board = initial;
        this.root.parent = null;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return null;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

    }
}
