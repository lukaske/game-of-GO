package inteligenca;
import logika.Point;
import logika.PointGroup;
import logika.PointType;
import splosno.Poteza;
import logika.Igra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Inteligenca extends splosno.KdoIgra {

    public Inteligenca(){
        super("play-goh");
        Igra igra = new Igra();
        Poteza poteza = izberiPotezo(igra);

    }

    private Poteza minimax(Igra igra){
        // apply minimax algorithm, use alpha-beta pruning
        long start_time = System.currentTimeMillis();
        TreeNode smart_guess = minimax_expand(
                igra,
                true,
                new TreeNode(igra.getPointsOfColor(PointType.EMPTY).iterator().next(), 0),
                start_time,
                0,
                0
                );
        Point best_move = smart_guess.p();
        return best_move.toPoteza();
    }

    private TreeNode minimax_expand(Igra igra, boolean computerIsBlack, TreeNode current_node, long start_time, int alpha, int beta){
        Set<Point> possible_moves = igra.getPointsOfColor(PointType.EMPTY);
        Set<TreeNode> evaluated_nodes = new HashSet<>();

        long now = System.currentTimeMillis();
        boolean ranOutOfTime = now - start_time > 4500;

        if(possible_moves.isEmpty() || ranOutOfTime) return current_node;

        for (Point p : possible_moves){
            Igra igra_branch = new Igra();
            igra_branch.copyOf(igra);
            igra_branch.odigraj(p.toPoteza());

            int liberties = 0;
            int my_liberties = 0;

            if (igra.isBlack() ^ computerIsBlack){
                // Maximize for computer
                if(computerIsBlack) {
                    liberties = igra_branch.getTotalWhiteLiberties();
                    my_liberties = igra_branch.getTotalBlackLiberties();
                }
                else {
                    liberties = igra_branch.getTotalBlackLiberties();
                    my_liberties = igra_branch.getTotalWhiteLiberties();
                }
            }
            else{
                // Maximize for computer
                if(computerIsBlack) {
                    liberties = igra_branch.getTotalBlackLiberties();
                    my_liberties = igra_branch.getTotalWhiteLiberties();

                }
                else {
                    liberties = igra_branch.getTotalWhiteLiberties();
                    my_liberties = igra_branch.getTotalBlackLiberties();
                }
            }

            if (liberties == 0) evaluated_nodes.add(new TreeNode(p, (float) 0));
            else{
                TreeNode best_move = minimax_expand(
                        igra_branch,
                        computerIsBlack,
                        new TreeNode(p, (float) liberties / (float) my_liberties),
                        start_time, alpha, beta);
                evaluated_nodes.add(best_move);
            }
        }


        TreeNode best_move = evaluated_nodes.iterator().next();;
        for (TreeNode n : evaluated_nodes){
            if (best_move.evaluation() > n.evaluation()) best_move = n;
        }
        return best_move;
    }

    public Poteza izberiPotezo(Igra igra){
        // make an intelligent move based on Igra
        return minimax(igra);
    }

}
