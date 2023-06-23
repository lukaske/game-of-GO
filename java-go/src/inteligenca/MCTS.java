package inteligenca;

import logika.IgraTraditional;
import logika.Point;
import logika.PointType;
import splosno.Poteza;

import java.util.Set;

public class MCTS extends splosno.KdoIgra {

    private int board_size;

    public MCTS(int board_size) {
        super("MCTS-Go");
        this.board_size = board_size;
    }

    public Poteza izberiPotezo(IgraTraditional igra) {
        Set<Point> moves = igra.disallowedMoves(igra.isBlack() ? PointType.BLACK: PointType.WHITE, true);
        System.out.println(moves);
        Point move = moves.iterator().next();
        return move.toPoteza();
    }
}
