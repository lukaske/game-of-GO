package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Okno extends JFrame implements ActionListener{

    protected int sirina = 800;
    protected int visina = 800;
    protected CardLayout cardLayout;
    protected JPanel panel;
    protected GoBoard goBoard;
    protected SplashEkran splashEkran;
    boolean showSplashScreen = true;

    private JMenuItem navodilaGo, navodilaCaptureGo, gitHubLink;
    private JMenuItem endGame;


    public Okno() {
        super(); // najprej pokličemo konstruktor JFrame, ki je nadrazred
        setTitle("Igraj GO!");
        setResizable(false);

        cardLayout = new CardLayout();
        panel = new JPanel(cardLayout);
        add(panel);

        goBoard = new GoBoard(sirina, visina);
        splashEkran = new SplashEkran(sirina, visina);
        panel.add("capture-go", goBoard);
        panel.add("splash-ekran", splashEkran);

        cardLayout.show(panel, "splash-ekran");

        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JMenu menuIgra = dodajMenu(menubar, "Game");
        endGame = dodajMenuItem(menuIgra, "Exit to main menu");


        JMenu menuNastavitve = dodajMenu(menubar, "Settings");

        JMenu menuOProgramu = dodajMenu(menubar, "About");
        navodilaCaptureGo = dodajMenuItem(menuOProgramu, "How to play Capture Go");
        navodilaGo = dodajMenuItem(menuOProgramu, "How to play Traditional Go");
        gitHubLink = dodajMenuItem(menuOProgramu, "Open on GitHub");


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    private JMenu dodajMenu(JMenuBar menubar, String naslov) {
        JMenu menu = new JMenu(naslov);
        menubar.add(menu);
        return menu;
    }

    private JMenuItem dodajMenuItem(JMenu menu, String naslov) {
        JMenuItem menuitem = new JMenuItem(naslov);
        menu.add(menuitem);
        menuitem.addActionListener(this);
        return menuitem;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object objekt = e.getSource();
        if (objekt == navodilaCaptureGo) {
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File("./assets/capturego.pdf");
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                    // no application registered for PDFs
                }
            }

        }
        else if (objekt == navodilaGo) {
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File("./assets/go.pdf");
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                    // no application registered for PDFs
                }
            }
        }
        else if (objekt == gitHubLink) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URL("https://github.com/lukaske/programiranje2-projekt").toURI());
                } catch (Exception ex) {}
            }
        }
        else if (objekt == endGame) {
            System.out.println(panel.getComponentCount());
            cardLayout.show(panel, "splash-ekran");
            panel.revalidate();
            panel.repaint();
        }


    }

    // Meant for testing
    public static void main(String[] args) {
        Okno okno = new Okno();
        okno.pack();
        okno.setVisible(true);
    }

    public void changeView(String viewName) {
        try{
            cardLayout.show(panel, viewName);
            panel.revalidate();
            panel.repaint();
        } catch (Exception e) {
            System.out.println("View does not exist");
        }
    }

}
