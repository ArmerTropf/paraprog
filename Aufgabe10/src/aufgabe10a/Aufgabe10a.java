package aufgabe10a;

/**
 * Created by mguenster on 11.06.17.
 *
 * Prüfen wie viele Durchgaenge ein Thread macht bei eingestellter Prio
 *
 * Sehe im Moment keine Möglichkeit das Prioritätenproblem herbeizurufen
 *
 */
public class Aufgabe10a {
    public static void main(String [] args) {

        /*Monitor monitor = new Monitor();

        TheThread thread1 = new TheThread(monitor , "thread1");
        TheThread thread2 = new TheThread(monitor , "thread2");
        TheThread thread3 = new TheThread(monitor , "thread3");

        thread1.setPriority(Thread.MAX_PRIORITY);
        thread2.setPriority(Thread.NORM_PRIORITY);
        thread3.setPriority(Thread.MIN_PRIORITY);

        thread1.start();
        thread2.start();
        thread3.start();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread1.myStop();
        thread2.myStop();
        thread3.myStop();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Thread1: Anzahl Aufrufe " + thread1.zaehler);
        System.out.println("Thread2: Anzahl Aufrufe " + thread2.zaehler);
        System.out.println("Thread3: Anzahl Aufrufe " + thread3.zaehler);*/



        // Locks einzeln setzten
        RessourceLock lock = new RessourceLock();
        lock.startThreads();


    }
}
