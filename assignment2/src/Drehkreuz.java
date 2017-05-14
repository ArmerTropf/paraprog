/**
 * Assignment 2 - Parallele Programmierung
 * Hochschule Bremerhaven - FB2 - Informatik
 * @author Michael Günster, Hendrik Mahrt, Andre Schriever
 */
public class Drehkreuz extends Thread {

    private Steuerung steuerung;
    private int numFahrgaeste;

    public Drehkreuz(Steuerung steuerung, String name, int numFahrgaeste) {

        super(name);

        this.steuerung = steuerung;
        this.numFahrgaeste = numFahrgaeste;

    }

    @Override
    public void run() {

        for (int i = 0; i < numFahrgaeste; ++i) {
            try {
                Thread.sleep((long) (Math.random() * 20));
                steuerung.passagier();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
