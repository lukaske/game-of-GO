package inteligenca;
import logika.IgraTraditional;
import logika.Point;
import logika.PointType;
import splosno.Poteza;

import java.util.*;

public class MCTS extends splosno.KdoIgra {

    private static final int SIMULATION_COUNT = 100; // Adjust the number of simulations as needed
    private static final int SIMULATION_DEPTH = 4; // Adjust the number of simulations as needed
    private final int board_size;

    public MCTS(int board_size) {
        super("MCTS-Go");
        this.board_size = board_size;
    }

    public Poteza izberiPotezo(IgraTraditional igra) {
        // Create the root node of the Monte Carlo Tree
        IgraTraditional igra_copy = new IgraTraditional(board_size);
        igra_copy.copyTraditional(igra);
        MCTSNode rootNode = new MCTSNode(null, null, igra_copy, null);

        // Perform MCTS simulations
        for (int i = 0; i < SIMULATION_COUNT; i++) {
            // Selection and expansion phase
            MCTSNode selectedNode = selection(rootNode);
            MCTSNode expandedNode = expansion(selectedNode);

            // Simulation phase
            int score = simulation(expandedNode);

            // Backpropagation phase
            backpropagation(expandedNode, score);
        }
        // Choose the best move based on the statistics
        Point bestMove = getBestMove(rootNode);

        // We force MCTS to check if the player can pass the move and end the game.
        IgraTraditional gameState_check_pass = new IgraTraditional(board_size);
        gameState_check_pass.copyTraditional(igra);
        PointType optimize_for = gameState_check_pass.whoseMove();
        Poteza pass_move = new Poteza(-1, -1);
        gameState_check_pass.odigraj(pass_move);
        if (gameState_check_pass.getWinner() == optimize_for) {
            return pass_move;
        }

        // Otherwise go for the best move
        return bestMove.toPoteza();
    }

    private MCTSNode selection(MCTSNode rootNode) {
        MCTSNode currentNode = rootNode;
        while (!currentNode.children.isEmpty()) {
            currentNode = currentNode.selectBestChild();
        }
        return currentNode;
    }

    private MCTSNode expansion(MCTSNode node) {
        Set<Point> legalMoves = node.gameState.disallowedMoves(node.gameState.whoseMove(), true);
        for (Point move : legalMoves) {
            IgraTraditional nextState = new IgraTraditional(board_size);
            nextState.copyTraditional(node.gameState);
            nextState.odigraj(move.toPoteza());
            MCTSNode newNode = new MCTSNode(node, move, nextState, null);
            node.children.add(newNode);
        }
        int randomIndex = new Random().nextInt(node.children.size());
        return node.children.get(randomIndex);
    }

    private int simulation(MCTSNode node) {
        IgraTraditional gameState = new IgraTraditional(board_size);
        gameState.copyTraditional(node.gameState);
        // Because we are already at depth 1
        PointType optimize_for = gameState.whoseNotMove();
        int current_depth = 0;

        while (gameState.getWinner() == PointType.EMPTY && current_depth < SIMULATION_DEPTH) {
            Set<Point> legalMoves = gameState.disallowedMoves(gameState.whoseMove(), true);
            int randomIndex = new Random().nextInt(legalMoves.size());
            Point randomMove = (Point) legalMoves.toArray()[randomIndex];

            gameState.odigraj(randomMove.toPoteza());
            current_depth += 1;
        }

        // Evaluate the final state based on captured area, maximize the score
        int evaluation;
        int winning_coef = 1;
        if (gameState.getWinner() == optimize_for) winning_coef = 100;
        else if (gameState.getWinner() == gameState.whoseNotMove()) winning_coef = 0;
        if (optimize_for == PointType.BLACK) evaluation = winning_coef * (gameState.getBlackArea() - gameState.getWhiteArea());
        else evaluation = winning_coef * (gameState.getWhiteArea() - gameState.getBlackArea());
        return evaluation;
    }

    private void backpropagation(MCTSNode node, int score) {
        while (node != null) {
            node.visitCount++;
            // Try to normalize the score to be between 0 and 1 using Sigmoid function
            node.scoreSum += (1 / (1+ Math.exp(-score)));
            node = node.parent;
        }
    }

    private Point getBestMove(MCTSNode rootNode) {
        double bestScore = Double.NEGATIVE_INFINITY;
        Point bestMove = null;
        for (MCTSNode child : rootNode.children) {
            double nodeScore = (double) child.scoreSum / (double) child.visitCount;
            if (nodeScore > bestScore) {
                bestScore = nodeScore;
                bestMove = child.move;
            }
        }
        return bestMove;
    }

    private class MCTSNode {
        private MCTSNode parent;
        private Point move;
        private IgraTraditional gameState;
        private List<MCTSNode> children;
        private int visitCount;
        private double scoreSum;

        public MCTSNode(MCTSNode parent, Point move, IgraTraditional gameState, Poteza poteza) {
            this.parent = parent;
            this.move = move;
            this.gameState = gameState;
            this.children = new ArrayList<>();
            this.visitCount = 0;
            this.scoreSum = 0;
        }

        public MCTSNode selectBestChild() {
            double explorationFactor = Math.sqrt(Math.log(visitCount + 1));
            MCTSNode bestChild = null;
            double bestScore = Double.NEGATIVE_INFINITY;
            for (MCTSNode child : children) {
                double nodeScore = ( child.scoreSum / (double) child.visitCount)
                        + (explorationFactor * Math.sqrt(Math.log(visitCount) / child.visitCount));
                if (nodeScore > bestScore) {
                    bestScore = nodeScore;
                    bestChild = child;
                }
            }
            return bestChild;
        }
    }
}
