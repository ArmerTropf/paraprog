package aufgabe7_1a;


public class Philosoph extends Thread {

    private int nummer = 0;
    private Gabel gabelLinks = null;
    private Gabel gabelRechts = null;


    public Philosoph(int nummer, Gabel links, Gabel rechts )  {
        this.nummer = nummer;
        this.gabelLinks = links;
        this.gabelRechts = rechts;

    }

    public void essen() {
        System.out.println("Philosoph: " + this.nummer + " am essen...");

    }

    public void denken() {

        try {
            Thread.sleep((int)(Math.random()*100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Philosoph: " + nummer + " denkt nach");

    }

    @Override
    public void run() {

        while (true) {
            denken();
            try {
                gabelLinks.aufnehmen(nummer, "links");
                gabelRechts.aufnehmen(nummer, "rechts");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            essen();
            gabelRechts.ablegen(nummer, "rechts");
            gabelLinks.ablegen(nummer, "links");

        }
    }
}
