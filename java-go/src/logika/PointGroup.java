package logika;

import java.util.HashSet;
import java.util.Set;

public class PointGroup {
    private int liberties;
    // Not exactly liberties (double entries), however we just need to know if it's == 0, so it's fine
    private final Point startingPoint;
    private final Point[][] board;
    private final Set<Point> group;
    private final PointType groupType;

    public PointGroup(Point[][] board, Point p){
        this.board = board;
        this.startingPoint = p;
        this.groupType = p.type();
        this.liberties = 0;
        if (p.type() != PointType.EMPTY) this.group = getGroup(p, new HashSet<>());
        else this.group = new HashSet<>();
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
                } else if (p1.type() == PointType.EMPTY && !existing_group.contains(p1)){
                    this.liberties++;
                }
            }
        }
        return existing_group;
    }

    public void printGroupOnBoard(){
        AsciiGridDisplay.printBoard(board, group);
        System.out.println("Liberties: " + this.liberties);
    }

    public Point getStartingPoint(){
        return this.startingPoint;
    }

    public PointType getGroupType(){
        return this.groupType;
    }

    public int getLiberties(){
        return this.liberties;
    }

    public Set<Point> getGroup(){
        return this.group;
    }

    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof PointGroup)) return false;
        PointGroup pg = (PointGroup) o;
        return pg.getGroup().equals(this.group);
    }

    @Override
    public int hashCode(){
        return this.group.hashCode();
    }

}
