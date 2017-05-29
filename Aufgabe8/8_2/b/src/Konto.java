/**
 * Created by mguenster on 29.05.17.
 */
public class Konto {

    private int alterKontostand;
    private int aktuellerKontostand;

    public Konto (int kontostand) {
        this.aktuellerKontostand = kontostand;
        this.alterKontostand = kontostand;
    }

    public synchronized void abheben (int betrag) throws RuntimeException, InterruptedException {

        if ( ! ( this.aktuellerKontostand > 0 ) ) {

                wait(1000);

                if ( this.aktuellerKontostand <= 0 )
                    throw new RuntimeException();

        }

        System.out.println(Thread.currentThread().getName() +   " hebt " + betrag  + " ab");

        this.aktuellerKontostand -= betrag;

        System.out.println("Aktueller Kontostand: " + this.aktuellerKontostand );

        notifyAll();
    }

    public synchronized void einzahlen(int betrag) {

        System.out.println(Thread.currentThread().getName() +   " zahlt " + betrag  + " ein");

        this.aktuellerKontostand += betrag;

        System.out.println("Aktueller Kontostand: " + this.aktuellerKontostand );
        notifyAll();
    }



    public synchronized int getBetrag() {
        return aktuellerKontostand;
    }

}


