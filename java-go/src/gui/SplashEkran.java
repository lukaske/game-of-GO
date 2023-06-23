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
    private float visina, sirina;
    private float default_size = 800.0f;
    private final SplashEkran self_ref = this;

    public SplashEkran(int sirina, int visina) {
        super();
        setSize(new Dimension(sirina, visina));
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
        this.visina = (float) visina;
        this.sirina = (float) sirina;
        visina_slike = visina - (int)(300.0f/default_size * visina);
        sirina_slike = sirina - (int)(300.0f/default_size * sirina);

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

        CustomButton playCaptureGo = new CustomButton("Play Capture GO", (int) (50.0f / default_size * sirina), (int) (700.0f/default_size * visina), (int) (30/default_size * visina));
        CustomButton playTraditionalGo = new CustomButton("Play Traditional GO", (int) (400.0f / default_size * sirina), (int) (700.0f/default_size * visina), (int) (30/default_size * visina));
        add(playCaptureGo);
        add(playTraditionalGo);
        setVisible(true);
        System.out.println((int)( (float) 50 / default_size) * sirina);

        playCaptureGo.addActionListener(e -> {
            JPanel frame = (JPanel) self_ref.getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "capture-go");
            frame.repaint();
        });

        playTraditionalGo.addActionListener(e -> {
            JPanel frame = (JPanel) self_ref.getParent();
            CardLayout cardLayout = (CardLayout) frame.getLayout();
            cardLayout.show(frame, "traditional-go");
            frame.repaint();
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int center_x_img = (this.getWidth() - sirina_slike) / 2;
        int center_y_img = (this.getHeight() - visina_slike) / 2;

        g.drawImage(backgroundImage, 0, 0, (int) sirina, (int) visina, null);
        g.drawImage(image, center_x_img, center_y_img, sirina_slike, visina_slike, null);

        Graphics2D g3d = (Graphics2D) g;

        g3d.setColor(Color.BLACK);
        g3d.setFont(new Font("SansSerif", Font.BOLD, (int) (70.0f / default_size * sirina)));
        g3d.drawString("GAME OF GO", 175/default_size * sirina, 100/default_size * visina);

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
