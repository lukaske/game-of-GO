package gui;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
    public CustomButton(String text, int x, int y, int size) {
        super(text);
        setBackground(new Color(59, 89, 182));
        setForeground(Color.BLACK);
        setFocusPainted(false);
        setFont(new Font("SansSerif", Font.BOLD, size));
        Dimension size1 = getPreferredSize();
        setBounds(x, y, size1.width, size1.height);
    }
}
