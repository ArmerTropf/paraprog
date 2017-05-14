/**
 * Assignment 2 - Parallele Programmierung
 * Hochschule Bremerhaven - FB2 - Informatik
 * @author Michael Günster, Hendrik Mahrt, Andre Schriever
 */
public class Assignment2 {

    public static void main(String[] args) {

        Steuerung steuerung = new Steuerung(10);
        Wagen wagen = new Wagen(steuerung);

        Drehkreuz drehkreuzWest = new Drehkreuz(steuerung, "Drehkreuz West", 100);
        Drehkreuz drehkreuzOst = new Drehkreuz(steuerung, "Drehkreuz Ost", 100);

        wagen.start();
        drehkreuzWest.start();
        drehkreuzOst.start();

        try {
            drehkreuzOst.join();
            drehkreuzWest.join();
            wagen.setWagenAusserBetrieb(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
