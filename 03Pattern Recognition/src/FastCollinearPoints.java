import java.util.Arrays;

public class FastCollinearPoints {
    private final Point[]       points;
    private final LineSegment[] segments;

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] points) {
        // check corner case
        if (points == null) {
            throw new java.lang.NullPointerException();
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new java.lang.NullPointerException();
            }
        }
        this.points = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = points[i];
        }
        Arrays.sort(this.points);
        for (int i = 1; i < this.points.length; i++) {
            if (this.points[i].compareTo(this.points[i - 1]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        // find segments
        // initialize
        int pointsNum = this.points.length;
        LineSegment[] totalSegments = new LineSegment[pointsNum * pointsNum / 2];
        int segNum = 0;
        Point[] slopePoints = new Point[this.points.length];
        for (int i = 0; i < pointsNum; ++i) {
            Point original = this.points[i];
            for (int j = 0; j < pointsNum; ++j) {
                slopePoints[j] = this.points[j];
            }
            Arrays.sort(slopePoints, original.slopeOrder());
            // find all segment through original
            int start = 1, count = 0;
            for (int k = 1; k < pointsNum; ++k) {
                double sSlope = original.slopeTo(slopePoints[start]);
                if (Double.compare(original.slopeTo(slopePoints[k]), sSlope) != 0
                        || k == pointsNum - 1) {
                    if (Double
                            .compare(original.slopeTo(slopePoints[k]), sSlope) == 0) {
                        count++;
                    }
                    if (count >= 3) { // store one segment
                        int end = start + count - 1;
                        if (original.compareTo(slopePoints[start]) < 0) {
                            /*
                             * StdOut.print(original.toString()); for(int t =
                             * start; t <= end; ++t){ StdOut.print("->");
                             * StdOut.print(slopePoints[t].toString()); }
                             * StdOut.println("");
                             */
                            totalSegments[segNum++] = new LineSegment(original,
                                    slopePoints[end]);
                        }
                    }
                    start = k;
                    count = 1;
                } else {
                    count++;
                }
            }
        }
        // store segment points
        this.segments = new LineSegment[segNum];
        for (int c = 0; c < segNum; ++c) {
            this.segments[c] = totalSegments[c];
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] retSegments = new LineSegment[this.numberOfSegments()];
        for (int c = 0; c < this.segments.length; ++c) {
            retSegments[c] = this.segments[c];
        }
        return retSegments;
    }
}
