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
        Point[] copyPoints = new Point[this.points.length], slopePoints = new Point[this.points.length - 1];
        int slopeLength;
        for (int i = 0; i < pointsNum; ++i) {
            copyPoints[i] = this.points[i];
        }
        // loop to find segments
        for (int i = 0; i < pointsNum; ++i) {
            slopeLength = 0;
            Point original = this.points[i];
            copyPoints[i] = null; // visited original
            for (int j = 0; j < copyPoints.length; ++j) {
                if (copyPoints[j] == null) {
                    continue;
                }
                slopePoints[slopeLength++] = copyPoints[j];
            }
            Arrays.sort(slopePoints, 0, slopeLength, original.slopeOrder());
            // find all segment through original
            int start = 0, count = 0;
            for (int k = 0; k < slopeLength; ++k) {
                double sSlope = original.slopeTo(slopePoints[start]);
                if (Double.compare(original.slopeTo(slopePoints[k]), sSlope) != 0
                        || k == slopeLength - 1) {
                    if (Double.compare(original.slopeTo(slopePoints[k]), sSlope) == 0) {
                        count++;
                    }
                    if (count >= 3) { // store one segment
                        int end = start + count - 1;
                        Point min, max;
                        if (original.compareTo(slopePoints[start]) < 0) {
                            min = original;
                        } else {
                            min = slopePoints[start];
                        }
                        if (original.compareTo(slopePoints[end]) > 0) {
                            max = original;
                        } else {
                            max = slopePoints[end];
                        }
                        /*
                         * Point[] segment = new Point[count + 1]; segment[0] =
                         * original; for (int p = start; p < start + count; p++)
                         * { segment[p - start + 1] = slopePoints[p]; }
                         */
                        if (checkExist(min, max, i)) {
                            /* Arrays.sort(segment); */
                            /*
                             * Point min = segment[0], max = segment[0]; for(int
                             * s = 0; s < segment.length; ++s){ min =
                             * min.compareTo(segment[s]) < 0 ? min : segment[s];
                             * max = max.compareTo(segment[s]) > 0 ? max :
                             * segment[s]; } totalSegments[segNum++] = new
                             * LineSegment( segment[0], segment[count]);
                             */
                            totalSegments[segNum++] = new LineSegment(min, max);
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

    private boolean checkExist(/* Point[] segment, */Point min, Point max,
            int currentOriginal) {
        // TODO Auto-generated method stub
        Point original = this.points[currentOriginal];
        double segSlope = max.slopeTo(min);
        for (int i = 0; i < currentOriginal; ++i) {
            if (Double.compare(original.slopeTo(this.points[i]), segSlope) == 0) {
                return false;
            }
        }
        return true;
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
