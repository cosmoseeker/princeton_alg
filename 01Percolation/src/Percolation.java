import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int N;
    private WeightedQuickUnionUF wquUF;
    private byte[] sites;
    private boolean percolated = false;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0)
            throw new java.lang.IllegalArgumentException();
        this.N = N;
        // construct a (N)*(N)grid
        wquUF = new WeightedQuickUnionUF(this.N * this.N);
        sites = new byte[this.N * this.N];
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        validate(i, j);
        if (isOpen(i, j))// if open already
            return;
        int current = this.toIndex(i, j);
        byte percolation = 0;
        sites[current] |= 1; //open site
        if (j > 1 && isOpen(i, j - 1)) { // left, iff has left
            percolation |= sites[wquUF.find(current - 1)];
            wquUF.union(current - 1, current);
        }
        if (j < this.N && isOpen(i, j + 1)) { // right, iff has right
            percolation |= sites[wquUF.find(current + 1)];
            wquUF.union(current + 1, current);
        }
        if (i > 1 && isOpen(i - 1, j)) { // up, iff has up
            percolation |= sites[wquUF.find(current - this.N)];
            wquUF.union(current - this.N, current);
        }
        if (i < this.N && isOpen(i + 1, j)) { // down, iff has down
            percolation |= sites[wquUF.find(current + this.N)];
            wquUF.union(current + this.N, current);
        }
        int root = wquUF.find(current);
        sites[root] |= percolation;
        if (i == 1)
            sites[root] |= 2;
        if (i == this.N)
            sites[root] |= 4;
        if ((sites[root] & 7) == 7)
            this.percolated = true;
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return (sites[this.toIndex(i, j)] & 1) == 1;
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        validate(i, j);
        if (!isOpen(i, j)) {
            return false;
        }
        return (sites[wquUF.find(this.toIndex(i, j))] & 3) == 3;
    }

    // does the system percolate?
    public boolean percolates() {
        /*
         * if (wquUF.connected(this.topVirtual, this.botVirtual)) return true;
         */
        return this.percolated;
    }

    // private utilities
    private int toIndex(final int i, final int j) {
        return this.N * (i - 1) + (j - 1);
    }

    private void validate(int i, int j) {
        if (i > this.N || j > this.N || i < 1 || j < 1)
            throw new IndexOutOfBoundsException();
    }
/*    
    private boolean validate(int p) {
        return (!(p < 0 || p >= this.N*this.N));
    }
*/
    // test client (optional)
    public static void main(String[] args) {
    }
}
