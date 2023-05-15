package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GoBoard extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

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

        int width = getWidth();
        int height = getHeight();

        int padding = 40;

        int innerWidth = width - 2 * padding;
        int innerHeight = height - 2 * padding;

        int cellWidth = innerWidth / 8;
        int cellHeight = innerHeight / 8;

        g.setColor(new Color(189, 163, 94)); // Wood-like color

        // Fill the background with wood-like texture
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
