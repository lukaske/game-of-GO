package logika;

import java.util.Objects;

public class Point {

    // x_coord represents the x coordinate of the point on the board
    // y_coord represents the y coordinate of the point on the board

    // x represents the index of the point on the board
    // y represents the index of the point on the board

    // isBlack is true if the point is black, false if the point is white

    int x_coord, y_coord, x, y;
    PointType type;

    public Point(int x_coord, int y_coord, int x, int y, PointType type){
        this.x_coord = x_coord;
        this.y_coord = y_coord;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int x_coord(){
        return x_coord;
    }

    public int y_coord(){
        return y_coord;
    }

    public int x(){
        return x;
    }

    public int y(){
        return y;
    }

    public PointType type(){
        return type;
    }

    public int typeToInt(){
        if (type == PointType.BLACK) return 1;
        else if (type == PointType.WHITE) return 2;
        else return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object other){
        if (other == this) return true;
        if (!(other instanceof Point otherPoint)) return false;
        return otherPoint.x == this.x && otherPoint.y == this.y;
    }
}

