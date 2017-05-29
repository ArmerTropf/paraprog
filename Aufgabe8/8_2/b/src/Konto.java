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

    public synchronized void abheben (int betrag) throws Exception {

        if ( ! ( this.aktuellerKontostand >= betrag ) ) {

                wait(1000);
                throw new InterruptedException();

        }

        this.aktuellerKontostand -= betrag;
        notifyAll();
    }

    public synchronized void einzahlen(int betrag) {

//        if (! ( this.aktuellerKontostand < this.alterKontostand) ) {
//
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }

        this.aktuellerKontostand += betrag;
        notifyAll();
    }



    public synchronized int getBetrag() {
        return aktuellerKontostand;
    }

}


