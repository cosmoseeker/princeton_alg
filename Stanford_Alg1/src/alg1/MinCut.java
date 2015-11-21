package alg1;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import edu.princeton.cs.algs4.In;

public class MinCut {
    HashSet<Integer> vertices = new HashSet<Integer>();
    File             file;

    private class Edge implements Comparable<Edge> {
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((v1 == null) ? 0 : v1.hashCode());
            result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
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
            Edge other = (Edge) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (v1 == null) {
                if (other.v1 != null)
                    return false;
            } else if (!v1.equals(other.v1))
                return false;
            if (v2 == null) {
                if (other.v2 != null)
                    return false;
            } else if (!v2.equals(other.v2))
                return false;
            return true;
        }

        Integer v1;
        Integer v2;

        public Edge(Integer v1, Integer v2) {
            if (v1 < v2) {
                this.v1 = v1;
                this.v2 = v2;
            } else {
                this.v1 = v2;
                this.v2 = v1;
            }
        }

        @Override
        public int compareTo(Edge that) {
            if (that == null) {
                return -1;
            }
            int thisS = this.v1 < this.v2 ? this.v1 : this.v2;
            int thisL = this.v1 > this.v2 ? this.v1 : this.v2;
            int thatS = that.v1 < that.v2 ? that.v1 : that.v2;
            int thatL = that.v1 > that.v2 ? that.v1 : that.v2;
            if (thisS == thatS) {
                return thisL - thatL;
            }
            return thisS - thatS;
        }

        private MinCut getOuterType() {
            return MinCut.this;
        }
    }

    LinkedList<Edge> net = new LinkedList<Edge>();

    public MinCut(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            File file = new File(args[0]);
            if (file.exists()) {
                MinCut minCut = new MinCut(file);
                int inver = minCut.findMinCut();
                System.out.println(inver);
            }
        }
    }

    private int findMinCut() {
        int iter = 200;
        int minCuts = Integer.MAX_VALUE, currentCuts;
        for (int i = 0; i < iter; i++) {
            currentCuts = this.getCuts();
            System.out.println(currentCuts + "; " + minCuts);
            if (currentCuts < minCuts) {
                minCuts = currentCuts;
            }
        }
        return minCuts;
    }

    private int getCuts() {
        TreeSet<Integer> v = new TreeSet<Integer>();
        LinkedList<Edge> e = new LinkedList<Edge>();
        HashSet<Edge> edgeSet = new HashSet<Edge>();

        In in = new In(file);
        while (in.hasNextLine()) {
            String[] line = in.readLine().split("\\s+");
            Integer v1 = Integer.parseInt(line[0]);
            v.add(v1);
            for (int i = 1; i < line.length; i++) {
                Edge edge = this.new Edge(v1, Integer.parseInt(line[i]));
                if (!edgeSet.contains(edge)) {
                    e.add(edge);
                    edgeSet.add(edge);
                }

            }
        }
        while (v.size() > 2) {
            // random get one edge
            Edge cutEdge = e.get((int) Math.floor(Math.random() * e.size()));

            // contraction
            Integer maintain = cutEdge.v1, delete = cutEdge.v2;
            v.remove(delete);
            e.remove(cutEdge);
            Iterator<Edge> iter = e.iterator();
            LinkedList<Edge> deteles = new LinkedList<Edge>();
            while (iter.hasNext()) {
                Edge edge = iter.next();
                if (edge.v1.equals(delete)) {
                    if (!edge.v2.equals(maintain)) {
                        edge.v1 = maintain;
                    } else {
                        iter.remove();
                        deteles.add(edge);
                    }
                } else if (edge.v2.equals(delete)) {
                    if (!edge.v1.equals(maintain)) {
                        edge.v2 = maintain;
                    } else {
                        iter.remove();
                        deteles.add(edge);
                    }
                }
            }
        }
        return e.size();
    }
}
