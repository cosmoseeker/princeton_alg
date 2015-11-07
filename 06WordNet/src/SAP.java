import java.util.Map;
import java.util.TreeMap;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private Digraph            graph;
    private Map<Pair, Integer> pairLen;
    private boolean[]          mark;

    private class Pair implements Comparable<Pair> {
        private final int v;
        private final int w;

        public Pair(int v, int w) {
            this.v = v;
            this.w = w;
        }

        @Override
        public int compareTo(Pair that) {
            if (this.v == that.v) {
                return this.w - that.w;
            }
            return this.v - that.v;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + v;
            result = prime * result + w;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (v != other.v)
                return false;
            if (w != other.w)
                return false;
            return true;
        }

        private SAP getOuterType() {
            return SAP.this;
        }
    }

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new java.lang.NullPointerException();
        }
        this.pairLen = new TreeMap<Pair, Integer>();
        this.graph = new Digraph(G);
        this.mark = new boolean[G.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (this.validateVertex(v) || this.validateVertex(w)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int lenV = 0, lenW = 0;
        int ancester = this.ancestor(v, w);
        if (ancester != -1) {
            lenV = this.pairLen.get(new Pair(v, ancester));
            lenW = this.pairLen.get(new Pair(w, ancester));
            return lenV + lenW;
        }
        return -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        if (this.validateVertex(v) || this.validateVertex(w)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        getPairDist(v);
        getPairDist(w);
        int shortest = Integer.MAX_VALUE, dist, ancester = -1;
        for (int i = 0; i < this.graph.V(); i++) {
            Pair pairv = new Pair(v, i), pairw = new Pair(w, i);
            if (this.pairLen.containsKey(pairv)
                    && this.pairLen.containsKey(pairw)) {
                int distv = this.pairLen.get(pairv), distw = this.pairLen
                        .get(pairw);
                dist = distv + distw;
                if (dist < shortest) {
                    shortest = dist;
                    ancester = i;
                }
            }
        }
        return ancester;
    }

    private void getPairDist(int vertex) {
        if (!mark[vertex]) {
            mark[vertex] = true;
            BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(
                    this.graph, vertex);
            for (int i = 0; i < this.graph.V(); i++) {
                if (bfs.hasPathTo(i)) {
                    int dist = bfs.distTo(i);
                    this.pairLen.put(new Pair(vertex, i), dist);
                }
            }
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new java.lang.NullPointerException();
        }
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return -1;
        }
        for (int vec : v) {
            if (this.validateVertex(vec)) {
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
        for (int vec : w) {
            if (this.validateVertex(vec)) {
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(
                this.graph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(
                this.graph, w);
        int shortest = -1;
        for (int i = 0; i < this.graph.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int dist = bfsv.distTo(i) + bfsw.distTo(i);
                if (dist < shortest || shortest == -1) {
                    shortest = dist;
                }
            }
        }
        return shortest;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new java.lang.NullPointerException();
        }
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return -1;
        }
        for (int vec : v) {
            if (this.validateVertex(vec)) {
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
        for (int vec : w) {
            if (this.validateVertex(vec)) {
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
        int shortest = -1, ancester = -1;
        BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(
                this.graph, v);
        BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(
                this.graph, w);
        for (int i = 0; i < this.graph.V(); i++) {
            if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
                int dist = bfsv.distTo(i) + bfsw.distTo(i);
                if (dist < shortest || shortest == -1) {
                    shortest = dist;
                    ancester = i;
                }
            }
        }
        return ancester;
    }

    private boolean validateVertex(int v) {
        return (v < 0 || v >= this.graph.V());
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
