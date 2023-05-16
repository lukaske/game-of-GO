package logika;
import splosno.Poteza;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Igra {

    // Black is 1, White is 2, Empty is 0
    Point[][] board = new Point[9][9];

    public Igra(){
        // ustvari novo igro velikosti 9x9
        // črni začne igro
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                board[i][j] = new Point(0, 0, i, j, PointType.EMPTY);
            }
        }

        randomlyFill();


        Set<Point> group1 = getGroup(board[0][0], new HashSet<Point>());
        Set<Point> group2 = getGroup(board[5][4], new HashSet<Point>());
        Set<Point> group3 = getGroup(board[8][1], new HashSet<Point>());

        AsciiGridDisplay.printBoard(board, group1);
        AsciiGridDisplay.printBoard(board, group2);
        AsciiGridDisplay.printBoard(board, group3);

    }

    private Set<Point> getGroup(Point p, Set<Point> existing_group){
        // Returns a set of points that are in the same group as the source point
        PointType pt = p.type();
        existing_group.add(p);
        int x = p.x();
        int y = p.y();
        int[][] to_visit = new int[4][2];
        to_visit[0] = new int[]{x - 1, y};
        to_visit[1] = new int[]{x + 1, y};
        to_visit[2] = new int[]{x, y - 1};
        to_visit[3] = new int[]{x, y + 1};

        for (int i = 0; i < 4; i++){
            int x1 = to_visit[i][0];
            int y1 = to_visit[i][1];
            if (x1 >= 0 && x1 < 9 && y1 >= 0 && y1 < 9){
                Point p1 = board[x1][y1];
                if (p1.type() == pt && !existing_group.contains(p1)){
                    existing_group.addAll(getGroup(p1, existing_group));
                }
            }
        }
        return existing_group;
    }

    boolean odigraj(Poteza poteza){
        boolean isPossible = true;
        boolean isBlack = true;
        PointType color;
        if (isBlack) color = PointType.BLACK;
        else color = PointType.WHITE;

        if (isPossible){
            Point p = new Point(0, 0, poteza.x(), poteza.y(), color);
            board[p.x()][p.y()] = p;
        }
        return isPossible;
    }

    private void randomlyFill(){
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                PointType pt;
                if (Math.random() < 0.5) pt = PointType.BLACK;
                else pt = PointType.WHITE;
                board[i][j] = new Point(0, 0, i, j, pt);
            }
        }
    }

    public static void main(String[] args) {
        Igra igra = new Igra();
    }
}
