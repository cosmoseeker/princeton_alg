import java.util.Arrays;

public class BruteCollinearPoints {
    private final Point[]       points;
    // private final int[][] segPoints;
    private final LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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
        int pointsNum = this.points.length;
        // int[][] segPoint = new int[pointsNum * pointsNum][2];
        LineSegment[] totalSegments = new LineSegment[pointsNum * pointsNum / 2
                + 1];
        int segNum = 0;
        for (int i = 0; i < pointsNum - 3; ++i)
            for (int j = i + 1; j < pointsNum - 2; ++j)
                for (int k = j + 1; k < pointsNum - 1; ++k)
                    for (int l = k + 1; l < pointsNum; ++l) {
                        double slope1 = this.points[i].slopeTo(this.points[j]);
                        double slope2 = this.points[i].slopeTo(this.points[k]);
                        double slope3 = this.points[i].slopeTo(this.points[l]);
                        if (slope1 == slope2 && slope1 == slope3) {
                            totalSegments[segNum++] = new LineSegment(
                                    this.points[i], this.points[l]);
                            /*
                             * segPoint[segNum][0] = i; segPoint[segNum][1] = l;
                             * ++segNum;
                             */
                        }
                    }
        // store segment points
        // this.segPoints = new int[segNum][2];
        this.segments = new LineSegment[segNum];
        for (int c = 0; c < segNum; ++c) {
            // this.segPoints[c][0] = segPoint[c][0];
            // this.segPoints[c][1] = segPoint[c][1];
            this.segments[c] = totalSegments[c];
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        /*
         * LineSegment[] segments = new LineSegment[this.segPoints.length]; for
         * (int c = 0; c < this.segPoints.length; ++c) { segments[c] = new
         * LineSegment(this.points[this.segPoints[c][0]],
         * this.points[this.segPoints[c][1]]); } return segments;
         */
        LineSegment[] retSegments = new LineSegment[this.numberOfSegments()];
        for (int c = 0; c < this.segments.length; ++c) {
            retSegments[c] = this.segments[c];
        }
        return retSegments;
    }
}
