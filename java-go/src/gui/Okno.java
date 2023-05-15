package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Okno extends JFrame implements ActionListener{

    protected int sirina = 800;
    protected int visina = 800;
    protected CardLayout cardLayout;
    protected JPanel panel;
    protected GoBoard goBoard;
    protected SplashEkran splashEkran;
    boolean showSplashScreen = true;

    private JMenuItem menuOdpri, menuShrani, menuKoncaj;


    public Okno() {
        super(); // najprej pokličemo konstruktor JFrame, ki je nadrazred
        setTitle("Igraj GO!");
        setResizable(false);

        CardLayout cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);
        add(panel);

        goBoard = new GoBoard(sirina, visina);
        splashEkran = new SplashEkran(sirina, visina);
        panel.add("capture-go", goBoard);
        panel.add("splash-ekran", splashEkran);

        cardLayout.show(panel, "splash-ekran");

        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JMenu menuIgra = dodajMenu(menubar, "Igra");
        JMenu menuNastavitve = dodajMenu(menubar, "Nastavitve");
        JMenu menuOProgramu = dodajMenu(menubar, "O programu");

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
    }

    // Meant for testing
    public static void main(String[] args) {
        Okno okno = new Okno();
        okno.pack();
        okno.setVisible(true);
    }

}
