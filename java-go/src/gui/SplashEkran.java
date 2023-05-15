package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class SplashEkran extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ActionListener {

    private BufferedImage image;
    private int sirina_slike = 500; // default
    private int visina_slike = 500; // default

    private Color startColor;
    private Color endColor;
    private double hue;
    private Timer timer;



    public SplashEkran(int sirina, int visina) {
        super();
        setPreferredSize(new Dimension(sirina, visina));
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();

        visina_slike = visina - 300;
        sirina_slike = sirina - 300;

        startColor = new Color(11, 36, 71);  // Dark blue
        endColor = new Color(87, 108, 188);  // Pink
        hue = 0;
        timer = new Timer(20, this);  // Animation speed (milliseconds)
        timer.start();


        try {
            // Load the PNG image from the file
            File imageFile = new File("./assets/splash.png");
            System.out.println(imageFile.getAbsolutePath());
            image = ImageIO.read(imageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //CustomButton button1 = new CustomButton("Capture Go", 100, 500);
        //CustomButton button2 = new CustomButton("Traditional Go", 100, 100);

        //add(button1);
        //add(button2);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the image onto the panel
        int width = getWidth();
        int height = getHeight();

        int center_x_img = (this.getWidth() - sirina_slike) / 2;
        int center_y_img = (this.getHeight() - visina_slike) / 2;

        // Calculate the current hue value
        double hue = (Math.sin(System.currentTimeMillis() / 5000.0) + 1) / 2;


        // Create a gradient from startColor to endColor based on hue
        Color gradientColor = getGradientColor(startColor, endColor, (float) hue);

        // Fill the panel with the gradient color
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(new GradientPaint(0, 0, gradientColor, width, height, gradientColor.darker()));
        g2d.fillRect(0, 0, width, height);

        g.drawImage(image, center_x_img, center_y_img, sirina_slike, visina_slike, null);

        Graphics2D g3d = (Graphics2D) g;
        g3d.setColor(Color.WHITE);
        g3d.setFont(new Font("SansSerif", Font.BOLD, 70));
        g3d.drawString("GAME OF GO", 200, 300);

    }

    private Color getGradientColor(Color startColor, Color endColor, float fraction) {
        float[] startHsb = Color.RGBtoHSB(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), null);
        float[] endHsb = Color.RGBtoHSB(endColor.getRed(), endColor.getGreen(), endColor.getBlue(), null);

        float hue = interpolate(startHsb[0], endHsb[0], fraction);
        float saturation = interpolate(startHsb[1], endHsb[1], fraction);
        float brightness = interpolate(startHsb[2], endHsb[2], fraction);

        return Color.getHSBColor(hue, saturation, brightness);
    }

    private float interpolate(float startValue, float endValue, float fraction) {
        return startValue + fraction * (endValue - startValue);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
