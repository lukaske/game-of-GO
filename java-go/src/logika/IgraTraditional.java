package logika;

import splosno.Poteza;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class IgraTraditional extends Igra {

    private Point lastMove;
    private Point secondLastMove;
    private Set<Point> lastCapturedSet = new HashSet<Point>();
    private int blackCaptures = 0;
    private int whiteCaptures = 0;


    public IgraTraditional(int board_size){
        super(board_size);
        System.out.println("Traditional game initialized");
    }

    public Set<Point> disallowedMoves(PointType playerColor){
        Set<Point> allowedMoves = new HashSet<>();
        Set<Point> disallowedMoves = new HashSet<>();

        for (Point[] row : board){
            for (Point p : row){
                if (p.type() == PointType.EMPTY){

                    // Check if move is suicidal
                    boolean isSuicide = false;
                    PointType color = playerColor;
                    PointType anticolor = playerColor == PointType.WHITE? PointType.BLACK : PointType.WHITE;
                    p.setType(color);
                    Set<PointGroup> groups = findAllGroups();
                    totalBlackLiberties = 0;
                    totalWhiteLiberties = 0;
                    for (PointGroup pg : groups){
                        int liberties = pg.getLiberties();
                        PointType groupType = pg.getGroupType();

                        // Count liberties for both colors
                        if (groupType == PointType.BLACK) totalBlackLiberties += liberties;
                        else if (groupType == PointType.WHITE) totalWhiteLiberties += liberties;

                        // Color wins if it captures opponent's group
                        // If color committed suicide and did not capture opponent's group, color loses
                        if (liberties == 0 && groupType == anticolor) isSuicide = false;
                        else if (liberties == 0 && groupType == color) isSuicide = true;
                    }
                    p.setType(PointType.EMPTY);

                    // Check if move is Ko
                    boolean isKo = false;
                    if (lastCapturedSet.size() == 1){
                        Iterator<Point> iterator = lastCapturedSet.iterator();
                        Point capturedStone = iterator.next();
                        if (capturedStone.type() == p.type() && capturedStone == p) isKo = true;
                    }

                    if (!isSuicide && !isKo) allowedMoves.add(p);
                    else disallowedMoves.add(p);
                }
            }
        }

        return disallowedMoves;
    }

    @Override
    public boolean odigraj(Poteza poteza) {
        PointType color = isBlack ? PointType.BLACK : PointType.WHITE;
        PointType anticolor = !isBlack ? PointType.BLACK : PointType.WHITE;
        Point p = board[poteza.x()][poteza.y()];
        boolean isPossible = isAllowedMove(p, color);

        if (isPossible){
            p.setType(color);
            Set<PointGroup> groups = findAllGroups();
            totalBlackLiberties = 0;
            totalWhiteLiberties = 0;

            for (PointGroup pg : groups){
                int liberties = pg.getLiberties();
                PointType groupType = pg.getGroupType();

                // Count liberties for both colors
                if (groupType == PointType.BLACK) totalBlackLiberties += liberties;
                else if (groupType == PointType.WHITE) totalWhiteLiberties += liberties;

                // Check if group has been captured
                if (liberties == 0 && groupType == anticolor) {
                    for (Point point : pg.getGroup()){
                        board[point.x()][point.y()] = point.clone();
                        board[point.x()][point.y()].setType(PointType.EMPTY);
                        if (point.type() == PointType.BLACK) blackCaptures++;
                        else if (point.type() == PointType.WHITE) whiteCaptures++;
                    }
                    lastCapturedSet = pg.getGroup();
                }
            }

            isBlack = !isBlack;
            //printGameState();
        }
        return isPossible;
    }

    protected boolean isAllowedMove(Point p, PointType color){
        Set<Point> disallowedMoves = disallowedMoves(color);
        System.out.println(disallowedMoves);
        return !disallowedMoves.contains(p);
    }

}
