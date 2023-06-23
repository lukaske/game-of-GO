package gui;

import logika.IgraTraditional;
import logika.PointType;

import java.awt.*;

public class TraditionalGoBoardChild extends TraditionalGoBoard {

    public TraditionalGoBoardChild(int sirina, int visina, int board_size) {
        super(sirina, visina, board_size);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        PointType color = isBlack ? PointType.BLACK : PointType.WHITE;
        for (logika.Point p : igra.disallowedMoves(color, false)) {
            g.setColor(Color.RED);
            g.fillOval(p.x_coord() - (10 / 2), p.y_coord() - (10 / 2), 10, 10);
        }
    }
}
