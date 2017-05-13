// Monitor
public class Controller {

    private Coaster coaster;
    private int coasterCapacity;
    private int freePlaces;

    public Controller(Coaster coaster, int coasterCapacity) {
        this.coaster = coaster;
        this.coasterCapacity = coasterCapacity;
        this.freePlaces = coasterCapacity;
    }

    public synchronized void passagier(String nameTurnstile) throws InterruptedException {

        while (!(freePlaces > 0)) wait();

        System.out.println("passagier " + nameTurnstile);
        --freePlaces;

        notifyAll();

    }

    public synchronized void abfahrt() throws InterruptedException {

        while (!(freePlaces == 0)) wait();

        coaster.abfahrt();

        notifyAll();

    }

    public synchronized void aussteigen() {

        freePlaces = coasterCapacity;

        notifyAll();

    }

}
