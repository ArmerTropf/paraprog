
public class Turnstile extends Thread {

    private Controller controller;
    private int numOfPassengers;


    public Turnstile(Controller controller, int numOfPassengers) {
        this.controller = controller;
        this.numOfPassengers = numOfPassengers;
    }

    @Override
    public void run() {

        for (int i = 0; i < numOfPassengers; ++i) {
            try {
                controller.passagier(currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
