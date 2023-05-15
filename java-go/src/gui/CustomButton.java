package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CustomButton extends JButton {

    private static final Color BUTTON_COLOR = new Color(0, 0, 0);  // Dark blue
    private static final Color OUTLINE_COLOR = Color.WHITE;
    private static final int BUTTON_PADDING = 5;
    private static final int EDGE_WIDTH = 6;

    private int positionX;
    private int positionY;

    public CustomButton(String text, int x, int y) {
        super(text);
        setForeground(Color.WHITE);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFont(getFont().deriveFont(Font.BOLD, 36f));

        positionX = x;
        positionY = y;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        Graphics2D g3d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw button text
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(getText());
        int textHeight = fontMetrics.getHeight();
        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight) / 2 - fontMetrics.getDescent() - BUTTON_PADDING;
        g3d.setColor(getForeground());

        // Draw button outline
        Shape outline = new RoundRectangle2D.Double(x, y,
                getWidth() - EDGE_WIDTH, getHeight() - EDGE_WIDTH, 20, 20);
        g2d.setColor(OUTLINE_COLOR);
        g2d.setStroke(new BasicStroke(EDGE_WIDTH));
        g2d.draw(outline);


        // Draw button background
        g2d.setColor(BUTTON_COLOR);
        g2d.fill(outline);

        g3d.drawString(getText(), positionX, positionY);

        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension superPrefSize = super.getPreferredSize();
        int width = Math.max(superPrefSize.width, positionX + superPrefSize.width);
        int height = Math.max(superPrefSize.height, positionY + superPrefSize.height);
        return new Dimension(width, height);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Do nothing to remove the default border
    }

    @Override
    public boolean contains(int x, int y) {
        Shape outline = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20);
        return outline.contains(x, y);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Buttons");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        CustomButton button1 = new CustomButton("Button 1", 50, 50);
        CustomButton button2 = new CustomButton("Button 2", 200, 100);

        frame.add(button1);
        frame.add(button2);

        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}
