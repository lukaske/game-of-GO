package gui;

import inteligenca.MCTS;
import logika.IgraTraditional;
import logika.PointType;
import splosno.Poteza;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

import static java.lang.Math.abs;

public class TraditionalGoBoard extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    protected int width, height, padding, innerWidth, innerHeight, cellWidth, cellHeight;
    protected int board_size;
    protected IgraTraditional igra;
    protected Set<logika.Point> blackStones;
    protected Set<logika.Point> whiteStones;
    protected PointType winner;
    protected boolean isBlack;
    protected int blackScore;
    protected int whiteScore;
    protected boolean isGameRunning = false;
    protected boolean isBlackHuman = true;
    protected boolean isWhiteHuman = true;
    protected MCTS intelligentAgent = new MCTS(board_size);

    protected final JToolBar topToolbar = new JToolBar();
    protected final JToolBar bottomToolbar = new JToolBar();
    protected JLabel statusLabel = new JLabel("Select player roles");
    protected JLabel blackAreaLabel = new JLabel("Black score: 0");
    protected JLabel whiteAreaLabel = new JLabel("White score: 0");
    protected JButton start_game = new JButton("Start game");
    protected JButton blackPass = new JButton("Black pass");
    protected JButton whitePass = new JButton("White pass");
    protected JComboBox<String> blackPlayer = new JComboBox<>();
    protected JComboBox<String> whitePlayer = new JComboBox<>();


    public TraditionalGoBoard(int sirina, int visina, int board_size) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(sirina, visina));
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();

        topToolbar.setFloatable(false);
        add(topToolbar, BorderLayout.SOUTH);
        topToolbar.addSeparator();
        topToolbar.add(start_game);
        topToolbar.addSeparator();
        topToolbar.add(statusLabel);

        topToolbar.add(new JLabel("Black: "));
        blackPlayer.addItem("Human");
        blackPlayer.addItem("Computer (MCTS)");
        topToolbar.add(blackPlayer);

        topToolbar.add(new JLabel("White: "));
        whitePlayer.addItem("Human");
        whitePlayer.addItem("Computer (MCTS)");
        topToolbar.add(whitePlayer);

        bottomToolbar.setFloatable(false);
        bottomToolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        add(bottomToolbar, BorderLayout.NORTH);
        bottomToolbar.add(statusLabel, BorderLayout.CENTER);
        bottomToolbar.addSeparator();
        bottomToolbar.add(blackAreaLabel, BorderLayout.CENTER);
        bottomToolbar.addSeparator();
        bottomToolbar.add(whiteAreaLabel, BorderLayout.CENTER);

        start_game.addActionListener(e -> {
            if (!isGameRunning) {
                startGame();
            } else {
                stopGame();
            }
        });

        blackPass.addActionListener(e -> {
            if (isGameRunning) {
                if (isBlack) {
                    Poteza poteza = new Poteza(-1, -1);
                    playMove(poteza);
                }
            }
        });

        whitePass.addActionListener(e -> {
            if (isGameRunning) {
                if (!isBlack) {
                    Poteza poteza = new Poteza(-1, -1);
                    playMove(poteza);
                }
            }
        });

        this.board_size = board_size;
        this.igra = new IgraTraditional(board_size);
        getGameState();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        width = getWidth();
        padding = 60;
        height = getHeight() - (padding / 2);
        innerWidth = width - 2 * padding;
        innerHeight = height - 2 * padding;
        cellWidth = innerWidth / (board_size - 1);
        cellHeight = innerHeight / (board_size - 1);
        igra.calcBoardCoordinates(padding, padding, innerWidth, innerHeight);

        g.setColor(new Color(189, 163, 94));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2)); // Set the line width to 3 pixels
        // Draw vertical lines
        for (int x = padding; x <= width - padding; x += cellWidth) {
            g2d.drawLine(x, padding, x, height - padding);
        }
        // Draw horizontal lines
        for (int y = padding; y <= height - padding; y += cellHeight) {
            g2d.drawLine(padding, y, width - padding, y);
        }

        // Draw black dots on board intersection for visibility

        int dotSize = 10;
        int dotSpacing = cellWidth * 2;

        int centerX = padding + cellWidth * (board_size / 2);
        int centerY = padding + cellHeight * (board_size / 2);
        int dotCount = (board_size - 5) / 2;

        for (int i = 0; i < dotCount; i++) {
            int x = centerX + (i * dotSpacing);
            int y = centerY + (i * dotSpacing);
            g.setColor(Color.BLACK);
            g.fillOval(x - (dotSize / 2), y - (dotSize / 2), dotSize, dotSize);

            x = centerX - (i * dotSpacing);
            y = centerY + (i * dotSpacing);
            g.fillOval(x - (dotSize / 2), y - (dotSize / 2), dotSize, dotSize);

            x = centerX - (i * dotSpacing);
            y = centerY - (i * dotSpacing);
            g.fillOval(x - (dotSize / 2), y - (dotSize / 2), dotSize, dotSize);

            x = centerX + (i * dotSpacing);
            y = centerY - (i * dotSpacing);
            g.fillOval(x - (dotSize / 2), y - (dotSize / 2), dotSize, dotSize);


            // Draw stones
        int stoneSize = cellWidth - 20;
        for (logika.Point p : blackStones) {
            g.setColor(Color.BLACK);
            g.fillOval(p.x_coord() - (stoneSize / 2), p.y_coord() - (stoneSize / 2), stoneSize, stoneSize);
        }
        for (logika.Point p : whiteStones) {
            g.setColor(Color.WHITE);
            g.fillOval(p.x_coord() - (stoneSize / 2), p.y_coord() - (stoneSize / 2), stoneSize, stoneSize);
        }
        }

        String currentPlayer = isBlack ? "BLACK" : "WHITE";
        if (isGameRunning){
            statusLabel.setText("Current player: " + currentPlayer);
            blackAreaLabel.setText("Black area: " + blackScore);
            whiteAreaLabel.setText("White area: " + whiteScore);
        }
    }

    private void getGameState(){
        blackStones = igra.getPointsOfColor(PointType.BLACK);
        whiteStones = igra.getPointsOfColor(PointType.WHITE);
        winner = igra.getWinner();
        isBlack = igra.isBlack();
        blackScore = igra.getBlackArea();
        whiteScore = igra.getWhiteArea();
    }

    protected void startGame(){
        isGameRunning = true;
        start_game.setText("Stop game");
        String currentPlayer = isBlack ? "BLACK" : "WHITE";
        statusLabel.setText("Current player: " + currentPlayer);
        blackPlayer.setEnabled(false);
        whitePlayer.setEnabled(false);
        topToolbar.add(blackPass);
        topToolbar.add(whitePass);
        isBlackHuman = blackPlayer.getSelectedItem().toString().equals("Human");
        isWhiteHuman = whitePlayer.getSelectedItem().toString().equals("Human");
        igra = new IgraTraditional(board_size);
        getGameState();
        repaint();

        if (!isBlackHuman){
            Poteza p = intelligentAgent.izberiPotezo(igra);
            playMove(p);
        }
    }

    protected void stopGame(){
        isGameRunning = false;
        start_game.setText("Start game");
        statusLabel.setText("Select player roles");
        blackPlayer.setEnabled(true);
        whitePlayer.setEnabled(true);
        blackAreaLabel.setText("Black score: 0");
        whiteAreaLabel.setText("White score: 0");
        topToolbar.remove(blackPass);
        topToolbar.remove(whitePass);
        igra = new IgraTraditional(board_size);
        getGameState();
        repaint();
    }

    protected void resetBoard() {
        stopGame();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!isGameRunning) return;

        int x_mouse = e.getX();
        int smallest_dist_x = width;
        int smallest_x_index = -1;
        int y_mouse = e.getY();
        int smallest_dist_y = height;
        int smallest_y_index = -1;

        System.out.println("x_mouse: " + x_mouse);
        System.out.println("y_mouse: " + y_mouse);

        // Check if the click is within the board
        int expanded_pad = padding - 20;
        if(x_mouse < expanded_pad || x_mouse > width - expanded_pad || y_mouse < expanded_pad || y_mouse > height - expanded_pad) {
            return;
        }

        for (int x = padding; x <= width - padding; x += cellWidth) {
            if (abs(x - x_mouse) < smallest_dist_x) {
                smallest_dist_x = abs(x - x_mouse);
                smallest_x_index = x;
            }
        }

        for (int y = padding; y <= height - padding; y += cellHeight) {
            if (abs(y - y_mouse) < smallest_dist_y) {
                smallest_dist_y = abs(y - y_mouse);
                smallest_y_index = y;
            }
        }

        if (smallest_x_index != -1 && smallest_y_index != -1){
            // Translate coordinates to board indexes
            int ix = (smallest_x_index - padding) / (cellWidth);
            int iy = (smallest_y_index - padding) / (cellHeight);

            Poteza poteza = new Poteza(ix, iy);
            playMove(poteza);

        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void playMove(Poteza poteza) {
        if (winner == PointType.EMPTY){
            boolean wasPlayed = igra.odigraj(poteza);
            // If the move was played, get the get game state, update screen and check if the game is over
            if (wasPlayed) {
                getGameState();

                // If we have a winner, display dialogue
                if (winner != PointType.EMPTY) {
                    repaint();
                    String winnerString = "";
                    if (winner == PointType.BLACK) {
                        winnerString = "Congratulations! Black player has won.";
                    } else if (winner == PointType.WHITE) {
                        winnerString = "Congratulations! White player has won.";
                    } else if (winner == PointType.NEUTRAL) {
                        winnerString = "The match resulted in a tie.";
                    }
                    winnerString += "\nBlack captured area: " + blackScore + "\nWhite captured area: " + whiteScore;
                    Object[] options = { "New game", "Back to menu" };
                    int action = JOptionPane.showOptionDialog(this, winnerString,
                            "Game over!", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,
                            new ImageIcon("./assets/cup.png"), options, options[0]);

                    // Reset game and return to main menu if the user chooses to
                    stopGame();
                    getGameState();
                    setFocusable(true);
                    if (action == 1) {
                        JPanel frame = (JPanel) this.getParent();
                        CardLayout cardLayout = (CardLayout) frame.getLayout();
                        cardLayout.show(frame, "splash-ekran");
                        frame.repaint();
                    }
                    else if (action == 0) {
                    }
                }
                repaint();
            }

            if (isBlack && !isBlackHuman){
                Poteza p = intelligentAgent.izberiPotezo(igra);
                playMove(p);
            }
            else if (!isBlack && !isWhiteHuman){
                Poteza p = intelligentAgent.izberiPotezo(igra);
                playMove(p);
            }
        }

    }
}
