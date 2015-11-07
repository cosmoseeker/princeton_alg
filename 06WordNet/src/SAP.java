import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private DirectedBFS dbfs;
    private Digraph graph;
    private HashMap<Query, List<Integer>> result = new HashMap<Query, List<Integer>>();

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        CheckUtil.checkNull(G);

        this.graph = new Digraph(G);
        this.dbfs = new DirectedBFS(this.graph);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (this.validateVertex(v) || this.validateVertex(w)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        List<Integer> iterv = Arrays.asList(v), iterw = Arrays.asList(w);
        Query query = new Query(iterv, iterw);
        int shortest = -1, ancester = -1;
        if (this.result.containsKey(query)) {
            shortest = this.result.get(query).get(1);
        } else {
            this.dbfs.bfs(iterv, iterw);
            ancester = this.dbfs.getAncester();
            shortest = this.dbfs.getShortest();
            this.result.put(query, Arrays.asList(ancester, shortest));
        }
        return shortest;
    }

    // a common ancestor of v and w that participates in a shortest ancestral
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        if (this.validateVertex(v) || this.validateVertex(w)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        
        List<Integer> iterv = Arrays.asList(v), iterw = Arrays.asList(w);
        Query query = new Query(iterv, iterw);
        int shortest = -1, ancester = -1;
        if (this.result.containsKey(query)) {
            ancester = this.result.get(query).get(0);
        } else {
            this.dbfs.bfs(iterv, iterw);
            ancester = this.dbfs.getAncester();
            shortest = this.dbfs.getShortest();
            this.result.put(query, Arrays.asList(ancester, shortest));
        }
        return ancester;
    }

    // length of shortest ancestral path between any vertex in v and any vertex
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        CheckUtil.checkNull(v);
        CheckUtil.checkNull(w);
        if (!CheckUtil.checkLen(v) || !CheckUtil.checkLen(w)) {
            return -1;
        }
        validateVertex(v);
        validateVertex(w);
        
        Query query = new Query(v, w);
        int shortest = -1, ancester = -1;
        if (this.result.containsKey(query)) {
            shortest = this.result.get(query).get(1);
        } else {
            this.dbfs.bfs(v, w);
            ancester = this.dbfs.getAncester();
            shortest = this.dbfs.getShortest();
            this.result.put(query, Arrays.asList(ancester, shortest));
        }
        return shortest;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        CheckUtil.checkNull(v);
        CheckUtil.checkNull(w);
        if (!CheckUtil.checkLen(v) || !CheckUtil.checkLen(w)) {
            return -1;
        }
        validateVertex(v);
        validateVertex(w);
        
        Query query = new Query(v, w);
        int shortest = -1, ancester = -1;
        if (this.result.containsKey(query)) {
            ancester = this.result.get(query).get(0);
        } else {
            this.dbfs.bfs(v, w);
            ancester = this.dbfs.getAncester();
            shortest = this.dbfs.getShortest();
            this.result.put(query, Arrays.asList(ancester, shortest));
        }
        return ancester;
    }

    private boolean validateVertex(int v) {
        return (v < 0 || v >= this.graph.V());
    }

    private void validateVertex(Iterable<Integer> vecs) {
        for (int vec : vecs) {
            if (this.validateVertex(vec)) {
                throw new java.lang.IndexOutOfBoundsException();
            }
        }
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
