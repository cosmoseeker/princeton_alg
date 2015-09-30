import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private final int[] counter;
    private double counterMean;
    private double counterVariance;
    private final int scale; // == N * N
    private int count;
    
    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N < 1 || T < 1)
            throw new IllegalArgumentException();
        this.scale = N * N;
        this.counter = new int[T];
        int i, j;
        Percolation per;
        for (this.count = 0; count < T; ++this.count) {
            per = new Percolation(N);
            while (!per.percolates()) {
                i = StdRandom.uniform(N) + 1;
                j = StdRandom.uniform(N) + 1;
                if (per.isOpen(i, j))
                    continue;
                per.open(i, j);
                counter[this.count]++;
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        this.counterMean = 0.0;
        for (int i = 0; i < this.count; i++) {
            this.counterMean += this.counter[i];
        }
        this.counterMean /= this.count;
        return this.counterMean / this.scale;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        this.mean();
        double avg = this.counterMean;
        double sum = 0.0;
        for (int i = 0; i < this.count; i++) {
            sum += (this.counter[i] - avg) * (this.counter[i] - avg);
        }
        this.counterVariance = sum / (this.count - 1);
        return Math.sqrt(this.counterVariance) / this.scale;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        this.stddev();
        return (this.counterMean - 1.96 * Math.sqrt(this.counterVariance / this.count)) / this.scale;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        this.stddev();
        return (this.counterMean + 1.96 * Math.sqrt(this.counterVariance / this.count)) / this.scale;
    }

    // test client (described below)
    public static void main(String[] args) {

    }
}