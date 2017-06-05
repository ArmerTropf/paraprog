package aufgabe9_1;

/**
 * Created by mguenster on 05.06.17.
 */
public class RWWriter extends Thread {

    private String threadName;
    private RWMonitor monitor;

    private int zahl;

    public RWWriter(RWMonitor monitor, String threadName ) {
       super(threadName);
        this.threadName = threadName;
        this.monitor = monitor;

    }


    public void run() {

        //for (int i = 0; i < 10; i++) {
        while (true) {

            this.zahl = (int)(Math.random()*20);

            try {
                monitor.schreiben(this.zahl);
                Thread.sleep((int)(Math.random()*200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
