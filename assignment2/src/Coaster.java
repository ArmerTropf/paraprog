/**
 * Created by hmahrt on 13.05.17.
 */
public class Coaster extends Thread {

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Coaster() {
    }

    public void abfahrt() {
        System.out.println("Wagen faehrt ab");
    }

    public void fahrt() {
        System.out.println("Wagen faehrt");
    }

    @Override
    public void run() {

        while (true) {
            try {
                controller.abfahrt();
                fahrt();
                controller.aussteigen();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
