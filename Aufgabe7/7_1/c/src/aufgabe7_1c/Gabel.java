package aufgabe7_1c;

public class Gabel {

    private final int gabelNummer;
    private boolean gabelInBenutzung = false;


    public Gabel(int gabelNummer) {
        this.gabelNummer = gabelNummer;
    }


    public synchronized void aufnehmen(int nummer, String Seite)  {

        PruefeGabelInBenutzung();
        System.out.println("Philosoph: " + nummer + " nimmt auf " + Seite);
        this.gabelInBenutzung = true;
        notifyAll();

    }

    public void ablegen(int nummer, String Seite) {
        System.out.println("Philosoph: " + nummer + " legt ab " + Seite);
        this.gabelInBenutzung = false;
    }

    public synchronized void PruefeGabelInBenutzung()  {

        while (this.gabelInBenutzung){
            try {
                wait(1000);
                if (this.gabelInBenutzung) {
                    throw new RuntimeException("Gib Gabel zurueck");
                }
            }
            catch (InterruptedException e) {
                System.exit(1);
            }


        }
    }

    public int getGabelNummer() {
        return this.gabelNummer;
    }


}
