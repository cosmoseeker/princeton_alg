import java.util.Arrays;

import edu.princeton.cs.algs4.IndexMinPQ;

public class SeamDFS {
    private int edgeTo[];
    private double distTo[];
    private IndexMinPQ<Double> pq;
    private final int w;
    private final int h;
    private Pixel[][] _pic;

    public SeamDFS(Pixel[][] pic, int w, int h) {
        int len = w * h + 2;
        edgeTo = new int[len];
        distTo = new double[len];
        Arrays.fill(distTo, Double.MAX_VALUE);
        pq = new IndexMinPQ<Double>(len);
        this.w = w;
        this.h = h;
        this._pic = pic;
    }

    public void findSeam() {
        boolean[] mark = new boolean[w * h + 2];
        distTo[0] = 0;
        pq.insert(0, 0.0);
        mark[0] = true;

        while (!pq.isEmpty()) {
            int index = pq.delMin();
            for (int adj : getAdj(index)) {
                if (!mark[adj]) {
                    relax(index, adj);
                    mark[adj] = true;
                }
            }
        }
    }

    private void relax(int from, int to) {
        int[] c = get2D(to);
        double weight;
        if (to <= w * h) {
            weight = Math.sqrt(this._pic[c[0]][c[1]].energy);
        } else { // end node
            weight = 0;
        }
        if (distTo[to] > distTo[from] + weight) {
            distTo[to] = distTo[from] + weight;
            edgeTo[to] = from;
            if (pq.contains(to))
                pq.decreaseKey(to, distTo[to]);
            else
                pq.insert(to, distTo[to]);
        }
    }

    private int[] getAdj(int index) { // simulate a DAG
        int[] adjs = new int[0];
        int[] coord = get2D(index);
        if (index == 0) { // start
            adjs = new int[this.w];
            for (int i = 0; i < adjs.length; i++) {
                adjs[i] = i + 1;
            }
        } else if (index == this.w * this.h + 1) { // end

        } else if (this.get2D(index)[0] == this.h - 1) { // last row
            adjs = new int[] { this.w * this.h + 1 };
        } else {
            if (coord[1] == 0) {
                adjs = new int[2];
                adjs[0] = get1D(coord[0] + 1, coord[1] + 1);
                adjs[1] = get1D(coord[0] + 1, coord[1]);
            } else if (coord[1] == this.w - 1) {
                adjs = new int[2];
                adjs[0] = get1D(coord[0] + 1, coord[1]);
                adjs[1] = get1D(coord[0] + 1, coord[1] - 1);
            } else {
                adjs = new int[3];
                adjs[0] = get1D(coord[0] + 1, coord[1] + 1);
                adjs[1] = get1D(coord[0] + 1, coord[1]);
                adjs[2] = get1D(coord[0] + 1, coord[1] - 1);
            }
        }
        return adjs;
    }

    private int[] get2D(int index) {
        int[] c = new int[2];
        c[0] = (index - 1) / this.w; // row
        c[1] = (index - 1) % this.w; // col
        return c;
    }

    private int get1D(int x, int y) {
        return x * this.w + y + 1;
    }

    public int[] path() {
        int[] path = new int[this.h];
        int to = this.w * this.h + 1;
        for (int i = this.h - 1; i >= 0; i--) {
            int from = edgeTo[to];
            int[] pixel = get2D(from);
            path[i] = pixel[1];
            to = from;
        }
        return path;
    }
}
