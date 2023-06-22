package gui;

import logika.IgraTraditional;

public class GoBoardTraditional extends GoBoard {

    public GoBoardTraditional(int sirina, int visina, int board_size) {
        super(sirina, visina, board_size);
        super.igra = new IgraTraditional(board_size);
    }

}
