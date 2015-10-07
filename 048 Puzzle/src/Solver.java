import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private boolean isSolve = false;
    private int     move    = -1;

    private class Node implements Comparable<Node> {
        private final Board   board;
        private final Node    parent;
        private final int     priority;
        private final int     move;
        private final boolean isTwin;

        public Node(Board board, int move, Node parent, boolean isTwin) {
            this.board = board;
            this.move = move;
            this.parent = parent;
            this.isTwin = isTwin;
            this.priority = board.manhattan() + move;
        }

        @Override
        public int compareTo(Node that) {
            if (this.board.equals(that.board))
                return 0;
            if (this.priority < that.priority)
                return -1;
            else
                return 1;
        }
    }

    private MinPQ<Node>  minPQ    = new MinPQ<Node>();
    private Stack<Board> solution = new Stack<Board>();

    // find a solution to the initial board(using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new java.lang.NullPointerException();
        Board initialTwin = initial.twin();
        Node initNode = new Node(initial, 0, null, false);
        Node initNodeTwin = new Node(initialTwin, 0, null, true);
        minPQ.insert(initNode);
        minPQ.insert(initNodeTwin);
        this.solve();
    }

    private void solve() {
        while (true) {
            // solve for original
            Node node = minPQ.delMin();
            if (node.board.isGoal()) {
                if (node.isTwin) {
                    this.isSolve = false;
                    this.move = -1;
                } else {
                    this.isSolve = true;
                    this.move = node.move;
                    this.solution.push(node.board);
                    while (node.parent != null) {
                        node = node.parent;
                        this.solution.push(node.board);
                    }
                }
                break;
            } else {
                for (Board neibor : node.board.neighbors()) {
                    Node neiborNode = new Node(neibor, node.move + 1, node,
                            node.isTwin);
                    if (node.parent == null) {
                        minPQ.insert(neiborNode);
                    } else if (!node.parent.board.equals(neiborNode.board)) {
                        minPQ.insert(neiborNode);
                    }
                }
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return this.isSolve;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.move;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (this.isSolvable()) {
            return this.solution;
        } else {
            return null;
        }
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

    }
}
