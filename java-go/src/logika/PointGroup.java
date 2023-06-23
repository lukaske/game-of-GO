package logika;

import java.util.HashSet;
import java.util.Set;

public class PointGroup {
    private final Point startingPoint;
    private final Point[][] board;
    private final Set<Point> group;
    private final Set<Point> liberties;
    private final PointType groupType;
    private final Set<Point> blackNeighbors;
    private final Set<Point> whiteNeighbors;
    private final int board_size;

    public PointGroup(Point[][] board, Point p, int board_size, boolean allowEmpty) {
        this.board = board;
        this.startingPoint = p;
        this.groupType = p.type();
        this.liberties = new HashSet<>();
        this.blackNeighbors = new HashSet<>();
        this.whiteNeighbors = new HashSet<>();
        this.board_size = board_size;
        if (p.type() != PointType.EMPTY || allowEmpty) this.group = getGroup(p, new HashSet<>());
        else this.group = new HashSet<>();
    }

    private Set<Point> getGroup(Point p, Set<Point> existing_group) {
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

        for (int i = 0; i < 4; i++) {
            int x1 = to_visit[i][0];
            int y1 = to_visit[i][1];
            if (x1 >= 0 && x1 < board_size && y1 >= 0 && y1 < board_size) {
                Point p1 = board[x1][y1];

                if (pt == PointType.EMPTY && p1.type() == PointType.BLACK) blackNeighbors.addAll(existing_group);
                else if (pt == PointType.EMPTY && p1.type() == PointType.WHITE) whiteNeighbors.addAll(existing_group);

                if (p1.type() == pt && !existing_group.contains(p1)) {
                    existing_group.addAll(getGroup(p1, existing_group));
                } else if (p1.type() == PointType.EMPTY && !existing_group.contains(p1)) {
                    liberties.add(p1);
                }
            }
        }
        return existing_group;
    }

    public void printGroupOnBoard() {
        AsciiGridDisplay.printBoard(board, group);
    }

    public Point getStartingPoint() {
        return this.startingPoint;
    }

    public PointType getGroupType() {
        return this.groupType;
    }

    public int getLiberties() {
        return this.liberties.size();
    }

    public Set<Point> getGroup() {
        return this.group;
    }

    public AreaCount estimateArea() {
        return new AreaCount(blackNeighbors, whiteNeighbors);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PointGroup pg)) return false;
        return pg.getGroup().equals(this.group);
    }

    @Override
    public int hashCode() {
        return this.group.hashCode();
    }

}
