package aufgabe10_1;

public class Aufgabe10 {



    public Aufgabe10() throws Exception {

        Object resource1 = new Object();
        Object resource2 = new Object();


        // additional medium priority threads to cause preemption
        DemoThread X1 = new DemoThread("X1", null);
        X1.setPriority(Thread.NORM_PRIORITY);
        X1.start();

        DemoThread X2 = new DemoThread("X2", null);
        X2.setPriority(Thread.NORM_PRIORITY);
        X2.start();

        DemoThread X3 = new DemoThread("X3", null);
        X3.setPriority(Thread.NORM_PRIORITY);
        X3.start();

        DemoThread X4 = new DemoThread("X4", null);
        X4.setPriority(Thread.NORM_PRIORITY);
        X4.start();

        // wait a bit
        Thread.sleep(10);

        // first start low priority thread
        DemoThread L = new DemoThread("LOW", resource1);
        L.setPriority(Thread.MIN_PRIORITY);
        L.start();

        // wait a bit for it to reach the lock
        Thread.sleep(50);

        // start high priority thread
        DemoThread H = new DemoThread("HIGH", resource1);
        L.setPriority(Thread.MAX_PRIORITY);
        H.start();

        // again wait a bit for it to reach the lock
        Thread.sleep(50);

        // start medium priority thread. This should lose against
        // the low priority thread if priority inheritance is used
        DemoThread M = new DemoThread("MED", null);
        M.setPriority(Thread.NORM_PRIORITY);
        M.start();
    }

    // priority inversion
    public static void main(String[] args) throws Exception {

        new Aufgabe10();

    }
}

class DemoThread extends Thread {

    private Object resource;
    private String name;

    public DemoThread(String name, Object resource) {
        this.resource = resource;
        this.name = name;
    }

    @Override
    public void run() {

        try {
            while (true) {

                System.out.println("Thread " + name + " started");

                if (resource == null) {
                    for (long i = 0; i < 2000000000; i++) {
                        Math.sqrt(i);
                    }
                    System.out.println("Thread " + name + " finished");
                    continue;
                }

                synchronized (resource) {
                    System.out.println("Thread " + name + " locks resource");
                    for (long i = 0; i < 1000000000; i++) {
                        Math.sqrt(i);
                    }
                    System.out.println("Thread " + name + " unlocks resource");
                }
                yield();
                System.out.println("Thread " + name + " finished");
            }
        } catch (Exception e) {}
    }

}

