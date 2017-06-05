 package aufgabe9_1;

/**
 * Created by mguenster on 05.06.17.
 */
public class RWMonitor {
    private int datenBasis;
    private int leser;
    private int schreiber;
    private boolean amSchreiben = false;
    private boolean amLesen = false;

    private synchronized void willLesen() throws InterruptedException {
        while (amSchreiben) {
            System.out.println("WARTE: willLesen... Thread: " + Thread.currentThread().getName());
            wait();
        }
        leser = leser + 1;
    }

    private synchronized void fertigLesen() {
        leser = leser - 1;
        notifyAll();
    }

    private synchronized void willSchreiben() throws InterruptedException {
        while (amSchreiben) {
            System.out.println("WARTE: willSchreiben... Thread: " + Thread.currentThread().getName());
            wait();
        }

        amSchreiben = true;
        schreiber += 1;
    }

    private synchronized void fertigSchreiben() {
        schreiber -= 1;
        amSchreiben = false;
        notifyAll();
    }

    public synchronized void lesen() throws InterruptedException {
        willLesen();
        System.out.println("Thread: " + Thread.currentThread().getName() + " gelesen: " + this.datenBasis); //... Objekt aus der Datenbasis lesen
        fertigLesen();

    }

    public synchronized void schreiben(int datenBasis) throws InterruptedException {
        willSchreiben();
        this.datenBasis = datenBasis;                                         //... Objekt in die Datenbasis schreiben
        fertigSchreiben();
        System.out.println("Thread: " + Thread.currentThread().getName() + " geschrieben: " + this.datenBasis);
    }
}
