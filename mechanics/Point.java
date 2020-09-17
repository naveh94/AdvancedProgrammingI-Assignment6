package mechanics;

import java.util.Arrays;
import java.util.List;

import static mechanics.Point.Direction.*;

public class Point {

    public enum Direction {N, NE, E, SE, S, SW, W, NW}
    public  static List<Direction> directions = Arrays.asList(N, NE, E, SE, S, SW, W, NW);
    private int x;
    private int y;

    /**
     * Create's a new Point with given parameters.
     * @param x int
     * @param y int
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor.
     * @param p Point
     */
    public Point(Point p) {
        this(p.getX(), p.getY());
    }

    /**
     * Returns Point's x parameter.
     * @return int
     */
    public int getX() {
        return x;
    }

    /**
     * Returns Point's y parameter
     * @return int
     */
    public int getY() {
        return y;
    }

    /**
     * Returns a new Point with parameters relative to this point, in given Direction.
     * @param dir Direction
     * @return Point
     */
    public Point moveDirection(Direction dir) {
        switch (dir) {
            case N:
                return new Point(x, y - 1);
            case NE:
                return new Point(x + 1, y - 1);
            case E:
                return new Point(x + 1, y);
            case SE:
                return new Point(x + 1, y + 1);
            case S:
                return new Point(x, y + 1);
            case SW:
                return new Point(x - 1, y + 1);
            case W:
                return new Point(x - 1, y);
            case NW:
                return new Point(x - 1, y - 1);
        }
        return this;
    }

    /**
     * Check if given Point is equal to another Point
     * @param obj Point (or another object)
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point point = (Point)obj;
            return (this.x == point.getX() && this.y == point.getY());
        }
        return super.equals(obj);
    }
}