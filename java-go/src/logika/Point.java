package logika;

import java.util.Objects;

public class Point {
    public int x,y;
    public int ix, iy; // Index on 9x9 board

    public Point(int x, int y, int ix, int iy) {
        this.x = x;
        this.y = y;
        this.ix = ix;
        this.iy = iy;
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
