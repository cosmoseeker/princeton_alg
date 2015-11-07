import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class DirectedBFS {
    private final Digraph graph;
    private static final int INFINITY = Integer.MAX_VALUE;
    private final boolean[] marked;
    private final int[] distTo;
    private final int[] source;
    private Stack<Integer> visited = new Stack<Integer>();
    private int ancester = -1, shortest = -1;

    public DirectedBFS(Digraph G) {
        this.graph = G;
        distTo = new int[G.V()];
        marked = new boolean[G.V()];
        source = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = INFINITY;
    }

    // BFS from multiple sources
    public void bfs(Iterable<Integer> sourcesV, Iterable<Integer> sourcesW) {
        init();
        Queue<Integer> q = new Queue<Integer>();
        initSource(sourcesV, q, 1);
        initSource(sourcesW, q, 2);
        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : graph.adj(v)) {
                if (!marked[w]) {
                    visit(q, w, distTo[v] + 1, source[v]);
                } else {
                    if (source[w] != source[v]) {
                        int dist = distTo[w] + distTo[v] + 1;
                        // consider cycle, can not return in advance
                        if (dist < this.shortest || this.shortest == -1) {
                            this.shortest = dist;
                            this.ancester = w;
                        }
                        // refresh w, determine path of w either from v or w
                        // with greedy strategy
                        if (distTo[v] + 1 < distTo[w]) {
                            source[w] = source[v];
                            distTo[w] = distTo[v] + 1;
                        }
                        // merge path
                        if (distTo[v] + 1 == distTo[w]) {
                            source[w] |= source[v];
                        }
                    }
                }
            }
        }
    }

    private void visit(Queue<Integer> q, int visitedV, int dist, int id) {
        visited.push(visitedV);
        marked[visitedV] = true;
        distTo[visitedV] = dist;
        source[visitedV] = id;
        q.enqueue(visitedV);
    }

    private void initSource(Iterable<Integer> sources, Queue<Integer> q,
            int sourceId) {
        for (int s : sources) {
            if (!marked[s]) { // source maintain duplicate
                visit(q, s, 0, sourceId);
            } else {
                this.ancester = s;
                this.shortest = 0;
                while (!q.isEmpty())
                    q.dequeue();
            }
        }
    }

    private void init() {
        ancester = -1;
        shortest = -1;
        while (!visited.isEmpty()) {
            int v = visited.pop();
            marked[v] = false;
            distTo[v] = INFINITY;
            source[v] = 0;
        }
    }

    public int getAncester() {
        return ancester;
    }

    public int getShortest() {
        return shortest;
    }

}