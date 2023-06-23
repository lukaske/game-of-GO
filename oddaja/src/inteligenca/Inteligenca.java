package inteligenca;
import logika.Point;
import logika.PointGroup;
import logika.PointType;
import splosno.Poteza;
import logika.Igra;

import java.util.*;

public class Inteligenca extends splosno.KdoIgra {

    private int board_size = 9;
    int scannedNodes = 0;
    int maxDepth = 0;

    public Inteligenca(int board_size){
        super("play-goh");
        this.board_size = board_size;
    }

    private Poteza minimax(Igra igra, boolean computerIsBlack){
        // apply minimax algorithm, use alpha-beta pruning
        long start_time = System.currentTimeMillis();
        //System.out.println("Starting minimax search @ " + start_time);
        scannedNodes = 0;
        maxDepth = 0;
        TreeNode smart_guess = minimax_expand(
                igra,
                computerIsBlack,
                new TreeNode(igra.getPointsOfColor(PointType.EMPTY).iterator().next(), 0),
                start_time,
                Float.MAX_VALUE * -1,
                Float.MAX_VALUE,
                0
                );
        Point best_move = smart_guess.p();
        System.out.println("Found poteza" + best_move.toPoteza() + " in " + (System.currentTimeMillis() - start_time) + " ms");
        return best_move.toPoteza();
    }

    private TreeNode minimax_expand(Igra igra, boolean computerIsBlack, TreeNode current_node, long start_time, float alpha, float beta, int depth){
        Set<Point> possible_moves = igra.getPointsOfColor(PointType.EMPTY);
        List<Point> newList = new ArrayList<>( possible_moves ) ;
        Collections.shuffle(newList);

        Set<TreeNode> evaluated_nodes = new HashSet<>();

        long now = System.currentTimeMillis();
        boolean ranOutOfTime = now - start_time > 5000;
        boolean maximizingComputer = igra.isBlack() ^ computerIsBlack;

        scannedNodes += 1;
        if (depth > maxDepth) maxDepth = depth;

        if(possible_moves.isEmpty() || ranOutOfTime) return current_node;

        for (Point p : newList){
            Igra igra_branch = new Igra(board_size);
            igra_branch.copyOf(igra);
            igra_branch.odigraj(p.toPoteza());

            int liberties = 0;

            if (maximizingComputer){
                // Maximize for computer
                if(computerIsBlack) {
                    liberties = -1 * igra_branch.getTotalWhiteLiberties(); // This should be maximal for computer to win
                }
                else {
                    liberties = -1 * igra_branch.getTotalBlackLiberties(); // This should be maximal for computer to win
                }
            }
            else{
                // Maximize for computer
                if(computerIsBlack) {
                    liberties = igra_branch.getTotalBlackLiberties(); // This should be minimal for human to win
                }
                else {
                    liberties = igra_branch.getTotalWhiteLiberties(); // This should be minimal for human to win
                }
            }

            if (liberties == 0) evaluated_nodes.add(new TreeNode(p, (float) 0));
            else{
                TreeNode best_move = minimax_expand(
                        igra_branch,
                        computerIsBlack,
                        new TreeNode(p, (float) liberties),
                        start_time, alpha, beta, depth + 1);
                float evaluation = best_move.evaluation();
                if (maximizingComputer){
                    if (evaluation > alpha) alpha = evaluation;
                }
                else{
                    if (evaluation < beta) beta = evaluation;
                }

                evaluated_nodes.add(best_move);
                if (maximizingComputer && alpha > beta) break; // alpha-beta pruning (no need to evaluate other nodes)
                if (!maximizingComputer && alpha < beta) break; // alpha-beta pruning
            }
        }

        TreeNode best_move = evaluated_nodes.iterator().next();
        for (TreeNode n : evaluated_nodes){
            if (maximizingComputer && best_move.evaluation() < n.evaluation()) best_move = n;
            if (!maximizingComputer && best_move.evaluation() > n.evaluation()) best_move = n;
        }
        return best_move;
    }

    public Poteza izberiPotezo(Igra igra){
        // make an intelligent move based on Igra
        return minimax(igra, igra.isBlack());
    }

    private void playWithItself(){
        Igra igra = new Igra(board_size);
        int j = 0;
        while (igra.getWinner() == PointType.EMPTY){
            j += 1;
            System.out.println("Starting round: " + j);
            Poteza poteza1 = izberiPotezo(igra);
            igra.odigraj(poteza1);

            System.out.println("Scanned nodes = " + scannedNodes);

            Poteza poteza2 = izberiPotezo(igra);
            igra.odigraj(poteza2);

            System.out.println("Scanned nodes = " + scannedNodes);

            igra.printGameState();
            System.out.println(igra.getWinner());
        }
        igra.printCapture();
    }

}
