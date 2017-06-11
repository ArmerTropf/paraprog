package aufgabe10a;

/**
 * Created by mguenster on 11.06.17.
 */
public class Monitor {


    boolean belegt = false;


    public synchronized void ausgabe(int zaehler) {

        while (belegt) {
            try {
                System.out.println("belegt");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        belegt = true;


        // System.out.println("Thread: "+ Thread.currentThread().getName() + " : aufgerufen: " + zaehler + "");


        belegt = false;
       // notifyAll();
    }

    public void willDrankommen(int zaehler) {
        ausgabe(zaehler);
    }
}
