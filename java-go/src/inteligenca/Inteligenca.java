package inteligenca;
import logika.Point;
import logika.PointGroup;
import logika.PointType;
import splosno.Poteza;
import logika.Igra;

import java.util.*;

public class Inteligenca extends splosno.KdoIgra {

    int scannedNodes = 0;
    int maxDepth = 0;

    public Inteligenca(){
        super("play-goh");
        Igra igra = new Igra();
        for (int j = 0; j < 20; j++){
            System.out.println("Starting round: " + j);
            Poteza poteza1 = izberiPotezo(igra);
            igra.odigraj(poteza1);

            System.out.println("Scanned nodes = " + scannedNodes);

            Poteza poteza2 = izberiPotezo2(igra);
            igra.odigraj(poteza2);

            System.out.println("Scanned nodes = " + scannedNodes);

            igra.printGameState();
            System.out.println(igra.getWinner());
        }
    }

    private Poteza minimax(Igra igra, boolean computerIsBlack){
        // apply minimax algorithm, use alpha-beta pruning
        long start_time = System.currentTimeMillis();
        System.out.println("Starting minimax search @ " + start_time);
        scannedNodes = 0;
        maxDepth = 0;
        TreeNode smart_guess = minimax_expand(
                igra,
                computerIsBlack,
                new TreeNode(igra.getPointsOfColor(PointType.EMPTY).iterator().next(), 0),
                start_time,
                0,
                0,
                0
                );
        Point best_move = smart_guess.p();
        System.out.println("Found poteza" + best_move.toPoteza() + " in " + (System.currentTimeMillis() - start_time) + " ms");
        return best_move.toPoteza();
    }

    private TreeNode minimax_expand(Igra igra, boolean computerIsBlack, TreeNode current_node, long start_time, int alpha, int beta, int depth){
        Set<Point> possible_moves = igra.getPointsOfColor(PointType.EMPTY);
        List<Point> newList = new ArrayList<>( possible_moves ) ;
        Collections.shuffle( newList ) ;

        Set<TreeNode> evaluated_nodes = new HashSet<>();

        long now = System.currentTimeMillis();
        boolean ranOutOfTime = now - start_time > 5000;

        scannedNodes += 1;
        if (depth > maxDepth) maxDepth = depth;

        if(possible_moves.isEmpty() || ranOutOfTime) return current_node;

        for (Point p : newList){
            Igra igra_branch = new Igra();
            igra_branch.copyOf(igra);
            igra_branch.odigraj(p.toPoteza());

            int liberties = 0;
            int my_liberties = 1;

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
                        start_time, alpha, beta, depth + 1);
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
        return minimax(igra, true);
    }

    public Poteza izberiPotezo2(Igra igra){
        // make an intelligent move based on Igra
        return minimax(igra, false);
    }

    public static void main(String[] args){
        Inteligenca inteligenca = new Inteligenca();
    }

}
