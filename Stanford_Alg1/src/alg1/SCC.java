package alg1;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;

public class SCC {
    private boolean[]             marked;
    private int[]                 id;
    private int                   count;
    private Map<Integer, Integer> sccs;

    public SCC(Digraph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        count = 0;
        sccs = new HashMap<Integer, Integer>();
        DepthFirstOrder dfs = new DepthFirstOrder(G.reverse());
        for (int v : dfs.reversePost()) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    public List<Entry<Integer, Integer>> getSccs() {
        List<Map.Entry<Integer, Integer>> sccList = new LinkedList<Map.Entry<Integer, Integer>>(
                this.sccs.entrySet());
        Collections.sort(sccList,
                new Comparator<Map.Entry<Integer, Integer>>() {
                    public int compare(Map.Entry<Integer, Integer> o1,
                            Map.Entry<Integer, Integer> o2) {
                        return (o2.getValue() - o1.getValue());
                    }
                });
        return sccList;
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        id[v] = count;
        if (!sccs.containsKey(count)) {
            sccs.put(count, 1);
        } else {
            sccs.put(count, sccs.get(count) + 1);
            // System.out.println(count + ": " + sccs.get(count));
        }
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
            }
        }
    }

}
