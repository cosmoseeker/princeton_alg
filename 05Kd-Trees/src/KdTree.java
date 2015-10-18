import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static class Node {
        private Point2D point;
        private int     level;
        private Node    left;
        private Node    right;

        Node() {
            this.point = null;
            this.left = null;
            this.right = null;
            this.level = 1;
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
        if (this.contains(p))
            return;
        while (parent != null) {
            double direct = this.direction(parent, p);
            leave.level = parent.level + 1;
            if (direct < 0) {
                if (parent.left != null) {
                    parent = parent.left;
                } else {
                    parent.left = leave;
                    break;
                }
            } else {
                if (parent.right != null) {
                    parent = parent.right;
                } else {
                    parent.right = leave;
                    break;
                }
            }
        }
        if (parent == null) {
            this.root = leave;
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
        if (search.level % KdTree.demensions == 1) { // vertical
            return p.x() - current.x();
        } else {
            return p.y() - current.y();
        }
    }

    // draw all points to standard draw
    public void draw() {
        Node node = this.root;
        double xmin = 0.0, ymin = 0.0, xmax = 1.0, ymax = 1.0;
        draw(node, xmin, xmax, ymin, ymax);
    }

    private void draw(Node node, double xmin, double xmax, double ymin,
            double ymax) {
        if (node == null)
            return;
        // draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        StdDraw.point(node.point.x(), node.point.y());
        // draw line
        StdDraw.setPenRadius();
        double x = node.point.x(), y = node.point.y();
        switch (node.level % 2) {
        case 0:// horizontal
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(xmin, y, xmax, y);
            this.draw(node.left, xmin, xmax, ymin, y);
            this.draw(node.right, xmin, xmax, y, ymax);
            return;
        case 1:// vertical
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x, ymin, x, ymax);
            this.draw(node.left, xmin, x, ymin, ymax);
            this.draw(node.right, x, xmax, ymin, ymax);
            return;
        default:
            return;
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.NullPointerException();
        List<Point2D> points = new ArrayList<Point2D>();
        Node node = this.root;
        this.range(rect, node, points);
        return points;
    }

    private void range(RectHV rect, Node node, List<Point2D> points) {
        if (node == null)
            return;
        if (rect.contains(node.point))
            points.add(node.point);
        switch (node.level % 2) {
        case 0:// horizontal
            if (rect.ymax() >= node.point.y()) {
                range(rect, node.right, points);
            }
            if (rect.ymin() <= node.point.y()) {
                range(rect, node.left, points);
            }
            return;
        case 1:// vertical
            if (rect.xmax() >= node.point.x()) {
                range(rect, node.right, points);
            }
            if (rect.xmin() <= node.point.x()) {
                range(rect, node.left, points);
            }
            return;
        default:
            return;
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException();
        if (this.isEmpty())
            return null;
        return nearest(this.root, p);
    }

    private Point2D nearest(Node node, Point2D p) {
        if (node == null)
            return null;
        Stack<Node> stack = new Stack<Node>();
        stack.push(node);

        double minDistSquare = Double.MAX_VALUE, dist;
        Node top;
        Point2D nearestP = node.point;
        while (stack.peek() != null) {
            top = stack.peek();
            // refresh min
            dist = p.distanceSquaredTo(top.point);
            if (dist < minDistSquare) {
                minDistSquare = dist;
                nearestP = top.point;
            }
            // add new node
            if (this.direction(top, p) < 0.0) {
                stack.push(top.left);
            } else {
                stack.push(top.right);
            }
        }
        // search path finish
        stack.pop(); // pop null
        while (!stack.isEmpty()) {
            top = stack.pop();
            double axisDistSquare = axisDistance(top, p);
            if (axisDistSquare > minDistSquare) {
                continue;
            }
            Point2D temp = null;
            double tempDistSquare;
            if (this.direction(top, p) < 0.0) {
                temp = nearest(top.right, p);
            } else {
                temp = nearest(top.left, p);
            }
            if (temp != null) {
                tempDistSquare = p.distanceSquaredTo(temp);
                if (tempDistSquare < minDistSquare) {
                    nearestP = temp;
                    minDistSquare = tempDistSquare;
                }
            }
        }
        return nearestP;
    }

    private double axisDistance(Node top, Point2D p) {
        if (top.level % KdTree.demensions == 0) {
            return (top.point.y() - p.y())*(top.point.y() - p.y());
        } else {
            return (top.point.x() - p.x())*(top.point.x() - p.x());
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }
}
