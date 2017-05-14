/**
 * Assignment 2 - Parallele Programmierung
 * Hochschule Bremerhaven - FB2 - Informatik
 * @author Michael Günster, Hendrik Mahrt, Andre Schriever
 */
public class Wagen extends Thread {

    private Steuerung steuerung;
    private boolean wagenAusserBetrieb = false;

    public void setWagenAusserBetrieb(boolean value) {
        this.wagenAusserBetrieb = value;
    }

    public Wagen(Steuerung steuerung) {

        this.steuerung = steuerung;

    }

    public void fahren() {

        System.out.println("Wagen faehrt");
        System.out.println("Wagen haelt an");

    }

    @Override
    public void run() {

        while (!wagenAusserBetrieb) {
            try {
                steuerung.abfahrt();
                fahren();
                steuerung.aussteigen();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
