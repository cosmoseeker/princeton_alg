import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static class Node {
        private Point2D point;
        private int     demension;
        private Node    left;
        private Node    right;
        private double  endpoint1;
        private double  endpoint2;

        Node() {
            this.point = null;
            this.left = null;
            this.right = null;
            this.demension = 1;
            this.endpoint1 = 0;
            this.endpoint2 = 1;
        }
    }

    private static int demensions = 2;
    private Node       root       = null;
    private int        size       = 0;

    // construct an empty set of points
    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.root == null;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException();
        Node leave = new Node();
        leave.point = p;
        Node parent = this.root;
        while (parent != null) {
            double direct = this.direction(parent, p);
            if (direct < 0) {
                if (parent.left != null) {
                    parent = parent.left;
                }else{
                    break;
                }
            } else {
                if (parent.left != null) {
                    parent = parent.right;
                }else{
                    break;
                }
            }
        }
        if (parent == null) {
            this.root = leave;
        } else {
            leave.demension = (parent.demension + 1) % KdTree.demensions;
            if (leave.demension == 0) {
                leave.endpoint1 = parent.point.x();
                if (parent.point.y() < p.y()) {
                    leave.endpoint2 = 0;
                    parent.left = leave;
                } else {
                    leave.endpoint2 = 1;
                    parent.right = leave;
                }
            } else {

            }
        }
        ++this.size;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        Node search = this.root;
        while (search != null) {
            if (search.point.equals(p))
                return true;
            double direct = this.direction(search, p);
            if (direct < 0.0) {
                search = search.left;
            } else {
                search = search.right;
            }
        }
        return false;
    }

    private double direction(Node search, Point2D p) {
        Point2D current = search.point;
        if (search.demension == 1) {
            return current.x() - p.x();
        }
        return current.y() - p.y();
    }

    // draw all points to standard draw
    public void draw() {
        Node node = this.root;
        draw(node);
    }

    private void draw(Node node) {
        if (node == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        StdDraw.point(node.point.x(), node.point.y());
        StdDraw.setPenRadius();
        if (node.demension == 0) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.endpoint1, node.point.y(), node.endpoint2,
                    node.point.y());
        } else if (node.demension == 1) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.endpoint1, node.point.x(),
                    node.endpoint2);
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.NullPointerException();
        List<Point2D> points = new ArrayList<Point2D>();
        double xmin = rect.xmin(), xmax = rect.xmax(), ymin = rect.ymin(), ymax = rect
                .ymax();

        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException();
        Point2D rpoint = null;
        double mindist = Double.MAX_VALUE, dist;

        return rpoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
