package aufgabe10a;

/**
 * Created by mguenster on 11.06.17.
 */
public class TheThread extends Thread {

    Monitor monitor = new Monitor();
    public int zaehler = 0;

    boolean running = true;

    public TheThread(Monitor monitor , String threadName) {
        super(threadName);
        this.monitor = monitor;

    }

    public void run() {

        while (running) {
            zaehler++;
            monitor.willDrankommen(zaehler);
        }


    }


    public void myStop () {
        running = false;
    }


}
