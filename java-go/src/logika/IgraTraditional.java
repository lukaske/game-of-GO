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
    private Set<Point> blackCapturedArea = new HashSet<>();
    private Set<Point> whiteCapturedArea = new HashSet<>();


    public IgraTraditional(int board_size){
        super(board_size);
        lastMove = board[0][0];
    }

    public Set<Point> disallowedMoves(PointType playerColor, boolean allowedMovesOnly){
        Set<Point> allowedMoves = new HashSet<>();
        Set<Point> disallowedMoves = new HashSet<>();

        for (Point[] row : board){
            for (Point p : row){
                if (p.type() == PointType.EMPTY){

                    // Check if move is suicidal
                    int isSuicide = -1;
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
                        if (liberties == 0 && groupType == anticolor) isSuicide = 0;
                        else if (liberties == 0 && groupType == color && isSuicide != 0) isSuicide = 1;
                    }

                    // Check if move is Ko
                    boolean isKo = false;
                    if (lastCapturedSet.size() == 1){
                        Iterator<Point> iterator = lastCapturedSet.iterator();
                        Point capturedStone = iterator.next();
                        if (capturedStone.type() == p.type() && capturedStone.equals(p) && secondLastMove.equals(p) && secondLastMove.type() == p.type()) isKo = true;
                    }
                    p.setType(PointType.EMPTY);

                    if (!(isSuicide == 1) && !isKo) allowedMoves.add(p);
                    else disallowedMoves.add(p);
                }
            }
        }
        allowedMoves.add(new Point(-1, -1, -1, -1, PointType.EMPTY));
        if (allowedMovesOnly) return allowedMoves;
        else return disallowedMoves;
    }

    @Override
    public boolean odigraj(Poteza poteza) {
        PointType color = isBlack ? PointType.BLACK : PointType.WHITE;
        PointType anticolor = !isBlack ? PointType.BLACK : PointType.WHITE;
        if (poteza.x() == -1 && poteza.y() == -1) {
            secondLastMove = lastMove.clone();
            lastMove = new Point(-1, -1, -1, -1, color);

            getWinner();

            isBlack = !isBlack;
            return true;
        }
        Point p = board[poteza.x()][poteza.y()];
        boolean isPossible = isAllowedMove(p, color);

        if (isPossible){
            p.setType(color);

            secondLastMove = lastMove.clone();
            lastMove = p.clone();

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

            countArea();
            isBlack = !isBlack;

            //printGameState();
        }
        return isPossible;
    }

    protected boolean isAllowedMove(Point p, PointType color){
        if (p.type() != PointType.EMPTY) return false;
        Set<Point> disallowedMoves = disallowedMoves(color, false);
        return !disallowedMoves.contains(p);
    }

    protected void countArea(){
        Set<PointGroup> groups = new HashSet<>();
        Set<Point> visitedPoints = new HashSet<>();
        whiteCapturedArea = new HashSet<>();
        blackCapturedArea = new HashSet<>();
        for (Point[] row : board){
            for (Point p : row){
                if (p.type() == PointType.EMPTY && !visitedPoints.contains(p)){
                    PointGroup pg = new PointGroup(board, p, board_size, true);
                    groups.add(pg);
                    visitedPoints.addAll(pg.getGroup());
                    AreaCount ac = pg.estimateArea();
                    if (ac.black().size() == 0 && ac.white().size() != 0) whiteCapturedArea.addAll(ac.white());
                    else if (ac.white().size() == 0 && ac.black().size() != 0) blackCapturedArea.addAll(ac.black());
                }
            }
        }
    }

    public int getBlackArea(){
        return blackCapturedArea.size();
    }

    public int getWhiteArea(){
        return whiteCapturedArea.size();
    }

    @Override
    public void resetGame(){
        for (int i = 0; i < board_size; i++){
            for (int j = 0; j < board_size; j++) {
                board[i][j] = new Point(0, 0, i, j, PointType.EMPTY);
            }
        }
        isBlack = true;
        lastMove = board[0][0];
        secondLastMove = board[0][0];
        lastCapturedSet = new HashSet<Point>();
        blackCaptures = 0;
        whiteCaptures = 0;
        countArea();
    }

    public boolean isGameOver(){
        return (winner == PointType.NEUTRAL || winner == PointType.BLACK || winner == PointType.WHITE);
    }

    public void copyTraditional(IgraTraditional igra){
        Point[][] new_board = new Point[board_size][board_size];
        for (int i = 0; i < board_size; i++){
            for (int j = 0; j < board_size; j++) {
                new_board[i][j] = new Point(0, 0, i, j, igra.getBoard()[i][j].type());
            }
        }
        this.board = new_board;
        this.isBlack = igra.isBlack();
        this.winner = igra.getWinner();
        this.lastMove = igra.getLastMove();
        this.secondLastMove = igra.getSecondLastMove();
        this.lastCapturedSet = igra.getLastCapturedSet();
        this.blackCaptures = igra.getBlackCaptures();
        this.whiteCaptures = igra.getWhiteCaptures();
        this.blackCapturedArea = igra.getBlackCapturedArea();
        this.whiteCapturedArea = igra.getWhiteCapturedArea();
        this.totalBlackLiberties = igra.getTotalBlackLiberties();
        this.totalWhiteLiberties = igra.getTotalWhiteLiberties();

    }

    public Point getSecondLastMove(){
        return secondLastMove.clone();
    }

    public Point getLastMove(){
        return lastMove.clone();
    }

    public Set<Point> getLastCapturedSet(){
        return new HashSet<>(lastCapturedSet);
    }

    public int getBlackCaptures(){
        return blackCaptures;
    }

    public int getWhiteCaptures(){
        return whiteCaptures;
    }

    public Set<Point> getBlackCapturedArea(){
        return new HashSet<>(blackCapturedArea);
    }

    public Set<Point> getWhiteCapturedArea(){
        return new HashSet<>(whiteCapturedArea);
    }

    public PointType whoseMove(){
        return isBlack() ? PointType.BLACK : PointType.WHITE;
    }

    public PointType whoseNotMove(){
        return !isBlack() ? PointType.BLACK : PointType.WHITE;
    }

    @Override
    public PointType getWinner(){
        if (lastMove.x() == -1 && lastMove.y() == -1 && secondLastMove.x() == -1 && secondLastMove.y() == -1 && secondLastMove.type() == whoseMove()){
            countArea();
            if (blackCapturedArea.size() > whiteCapturedArea.size()) winner = PointType.BLACK;
            else if (blackCapturedArea.size() < whiteCapturedArea.size()) winner = PointType.WHITE;
            else winner = PointType.NEUTRAL;
        }
        return winner;
    }
}
