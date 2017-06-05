package aufgabe9_1;

/**
 * Created by mguenster on 05.06.17.
 */
public class RWReader extends Thread {

    private String threadName;
    private RWMonitor monitorReader;

    public RWReader(RWMonitor monitorReader, String threadName) {
        super(threadName);
        this.monitorReader = monitorReader;
        this.threadName = threadName;
    }

    public void run() {

        //for (int i = 0; i < 10; i++) {
        while (true) {

            try {
                monitorReader.lesen();
                Thread.sleep((int)(Math.random()*200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
