package aufgabe7_1b;


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
            Thread.sleep((int)(Math.random()*1));
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
                if ((this.nummer % 2) == 0 ) {
                    gabelLinks.aufnehmen(nummer, "links");
                } else {
                    gabelRechts.aufnehmen(nummer, "rechts");
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            essen();
            gabelRechts.ablegen(nummer, "rechts");
            gabelLinks.ablegen(nummer, "links");

        }
    }
}
