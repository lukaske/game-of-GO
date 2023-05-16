package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class SplashEkran extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ActionListener {

    private BufferedImage image;
    private BufferedImage backgroundImage;

    private int sirina_slike = 500; // default
    private int visina_slike = 500; // default
    private final SplashEkran self_ref = this;

    public SplashEkran(int sirina, int visina) {
        super();
        setSize(new Dimension(sirina, visina));
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();

        visina_slike = visina - 300;
        sirina_slike = sirina - 300;

        try {
            // Load the PNG image from the file
            File imageFile = new File("./assets/splash2.png");
            image = ImageIO.read(imageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // Load the PNG image from the file
            File imageFile = new File("./assets/grid2.png");
            backgroundImage = ImageIO.read(imageFile);
        } catch (Exception e) {
            e.printStackTrace();
        }


        setLayout(null);

        CustomButton playCaptureGo = new CustomButton("Play Capture GO", 50, 700);
        CustomButton playTraditionalGo = new CustomButton("Play Traditional GO", 400, 700);
        add(playCaptureGo);
        add(playTraditionalGo);
        setVisible(true);

        playCaptureGo.addActionListener(e -> {
            JPanel frame = (JPanel) self_ref.getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "capture-go");
            frame.repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int center_x_img = (this.getWidth() - sirina_slike) / 2;
        int center_y_img = (this.getHeight() - visina_slike) / 2;

        g.drawImage(backgroundImage, 0, 0, 800, 800, null);
        g.drawImage(image, center_x_img, center_y_img, sirina_slike, visina_slike, null);

        Graphics2D g3d = (Graphics2D) g;

        g3d.setColor(Color.BLACK);
        g3d.setFont(new Font("SansSerif", Font.BOLD, 70));
        g3d.drawString("GAME OF GO", 175, 100);

        setBackground(Color.WHITE);
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
