package logika;
import splosno.Poteza;
import java.util.HashSet;
import java.util.Set;

public class Igra {

    private Point[][] board = new Point[9][9];
    private boolean isBlack = true;
    private PointType winner = PointType.EMPTY;
    private int totalBlackLiberties = 0;
    private int totalWhiteLiberties = 0;

    public Igra(){
        resetGame();
        //randomlyFill();
        //printGameState();
        //printCapture();
    }

    private Set<PointGroup> findAllGroups(){
        // Finds all groups on the board
        // Returns a set of PointGroups
        Set<PointGroup> groups = new HashSet<>();
        Set<Point> visitedPoints = new HashSet<>();
        for (Point[] row : board){
            for (Point p : row){
                if (p.type() != PointType.EMPTY && !visitedPoints.contains(p)){
                    PointGroup pg = new PointGroup(board, p);
                    groups.add(pg);
                    visitedPoints.addAll(pg.getGroup());
                }
            }
        }
        return groups;
    }

    public boolean odigraj(Poteza poteza){
        //System.out.println("Playing move: " + poteza.x() + ", " + poteza.y() + " by " + (isBlack ? "black" : "white") + " player");
        Point p = board[poteza.x()][poteza.y()];
        boolean isPossible = p.type() == PointType.EMPTY;
        if (isPossible){
            PointType color = isBlack ? PointType.BLACK : PointType.WHITE;
            PointType anticolor = !isBlack ? PointType.BLACK : PointType.WHITE;
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
                if (liberties == 0 && groupType == anticolor) winner = color;
                else if (liberties == 0 && groupType == color) winner = anticolor;
            }

            isBlack = !isBlack;
            //printGameState();
        }
        return isPossible;
    }

    private void randomlyFill(){
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                PointType pt;
                double rand = Math.random();
                if (rand <= 0.3) pt = PointType.BLACK;
                else if (rand <= 0.66) pt = PointType.WHITE;
                else pt = PointType.EMPTY;
                board[i][j] = new Point(0, 0, i, j, pt);
            }
        }
    }

    public void resetGame(){
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                board[i][j] = new Point(0, 0, i, j, PointType.EMPTY);
            }
        }
        isBlack = true;
    }

    public void printCapture(){
        Set<PointGroup> groups = findAllGroups();
        System.out.println("All groups:" + groups.size());
        for (PointGroup pg : groups){
            if (pg.getLiberties() == 0){
                System.out.println("Group with no liberties:");
                pg.printGroupOnBoard();
            }
        }
    }

    public void printGameState(){
        System.out.println("Game state:");
        AsciiGridDisplay.printBoard(board, new HashSet<>());

    }

    public void calcBoardCoordinates(int x_start, int y_start, int width, int height){
        // For point on board, determine coordinates on canvas
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                Point p = board[i][j];
                p.set_x_coord(x_start + i * width / 8);
                p.set_y_coord(y_start + j * height / 8);
            }
        }
    }

    public Point[][] getBoard(){
        return board;
    }

    public boolean isBlack(){
        return isBlack;
    }

    public PointType getWinner(){
        return winner;
    }

    public Set<Point> getPointsOfColor(PointType color){
        Set<Point> points = new HashSet<>();
        for (Point[] row : board){
            for (Point p : row){
                if (p.type() == color) points.add(p);
            }
        }
        return points;
    }

    public void copyOf(Igra igra){
        Point[][] new_board = new Point[9][9];
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++) {
                new_board[i][j] = new Point(0, 0, i, j, igra.getBoard()[i][j].type());
            }
        }
        this.board = new_board;
        this.isBlack = igra.isBlack();
        this.winner = igra.getWinner();
    }

    public int getTotalBlackLiberties(){
        return totalBlackLiberties;
    }

    public int getTotalWhiteLiberties(){
        return totalWhiteLiberties;
    }

    public static void main(String[] args) {
        Igra igra = new Igra();
    }

}
