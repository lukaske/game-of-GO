package gui;

import java.util.Objects;

public class Point {
    public int x,y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals (Object o) {
        if (o == this) return true;
        if (!(o instanceof Point p)) return false;
        return p.x == x && p.y == y;
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }

}
