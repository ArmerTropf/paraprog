/**
 * Assignment 2 - Parallele Programmierung
 * Hochschule Bremerhaven - FB2 - Informatik
 * @author Michael Günster, Hendrik Mahrt, Andre Schriever
 */
public class Steuerung {

    private int wagenKapazitaet;
    private int freiePlaetze;

    public Steuerung(int wagenKapazitaet) {

        this.wagenKapazitaet = wagenKapazitaet;
        this.freiePlaetze = wagenKapazitaet;

    }

    public synchronized void passagier() throws InterruptedException {

        while (!(freiePlaetze > 0)) {
            wait();
        }

        System.out.println("Passagier durch " + Thread.currentThread().getName());
        --freiePlaetze;

        notifyAll();

    }

    public synchronized void abfahrt() throws InterruptedException {

        while (!(freiePlaetze == 0)) {
            wait();
        }

        System.out.println("Wagen voll. Abfahrt.");

        notifyAll();

    }

    public synchronized void aussteigen() {

        System.out.println("Passagiere steigen aus");
        freiePlaetze = wagenKapazitaet;

        notifyAll();

    }

}
