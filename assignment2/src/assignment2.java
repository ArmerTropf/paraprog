public class assignment2 {

    public static void main(String[] args) {

        Coaster coaster = new Coaster();
        Controller controller = new Controller(coaster, 8);
        coaster.setController(controller);
        Turnstile turnstile = new Turnstile(controller, 100);
        Turnstile turnstile2 = new Turnstile(controller, 100);

        coaster.start();
        turnstile.start();
        turnstile2.start();


    }

}
