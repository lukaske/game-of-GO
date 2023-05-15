package inteligenca;
import splosno.Poteza;
import logika.Igra;

public class Inteligenca extends splosno.KdoIgra {

    public Inteligenca(){
        super("play-goh");
    }

    public Poteza izberiPotezo(Igra igra){
        // make an intelligent move based on Igra
        return new Poteza(0, 0);
    }

}
