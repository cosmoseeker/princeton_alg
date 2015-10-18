import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> treeSet = new SET<Point2D>();

    // construct an empty set of points
    public PointSET() {
        this.treeSet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.treeSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.treeSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException();
        if (!this.contains(p)) {
            this.treeSet.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return this.treeSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D point : this.treeSet) {
            StdDraw.point(point.x(), point.y());
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.NullPointerException();
        List<Point2D> points = new ArrayList<Point2D>();
        double xmin = rect.xmin(), xmax = rect.xmax(), ymin = rect.ymin(), ymax = rect
                .ymax();
        for (Point2D point : this.treeSet) {
            double x = point.x(), y = point.y();
            if (x >= xmin && x <= xmax && y >= ymin && y <= ymax) {
                points.add(point);
            }
        }
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException();
        Point2D rpoint = null;
        double mindist = Double.MAX_VALUE, dist;
        if (!this.treeSet.isEmpty()) {
            for (Point2D point : this.treeSet) {
                dist = p.distanceSquaredTo(point);
                if (dist < mindist) {
                    mindist = dist;
                    rpoint = point;
                }
            }
        }
        return rpoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
