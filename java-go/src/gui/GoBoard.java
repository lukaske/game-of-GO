package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;

public class GoBoard extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    int width, height, padding, innerWidth, innerHeight, cellWidth, cellHeight;
    Set<Point> blackStones = new HashSet<>();
    Set<Point> whiteStones = new HashSet<>();
    boolean playerTurn = true; // true = black, false = white

    public GoBoard(int sirina, int visina) {
        super();
        setPreferredSize(new Dimension(sirina, visina));
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        width = getWidth();
        height = getHeight();
        padding = 40;
        innerWidth = width - 2 * padding;
        innerHeight = height - 2 * padding;
        cellWidth = innerWidth / 8;
        cellHeight = innerHeight / 8;

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

        // Draw stones

        int stoneSize = cellWidth - 20;
        for (Point p : blackStones) {
            g.setColor(Color.BLACK);
            g.fillOval(p.x - (stoneSize / 2), p.y - (stoneSize / 2), stoneSize, stoneSize);
        }
        for (Point p : whiteStones) {
            g.setColor(Color.WHITE);
            g.fillOval(p.x - (stoneSize / 2), p.y - (stoneSize / 2), stoneSize, stoneSize);
        }
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
        int x_mouse = e.getX();
        int smallest_dist_x = width;
        int smallest_x_index = -1;
        int y_mouse = e.getY();
        int smallest_dist_y = height;
        int smallest_y_index = -1;

        System.out.println("x_mouse: " + x_mouse);
        System.out.println("y_mouse: " + y_mouse);

        // Check if the click is within the board
        if(x_mouse < padding || x_mouse > width - padding || y_mouse < padding || y_mouse > height - padding) {
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
            Point p = new Point(smallest_x_index, smallest_y_index);
            if (!whiteStones.contains(p) && !blackStones.contains(p)){
                if (playerTurn) {
                    blackStones.add(p);
                }
                else{
                    whiteStones.add(p);
                }
                playerTurn = !playerTurn;
                repaint();

            }
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
}
