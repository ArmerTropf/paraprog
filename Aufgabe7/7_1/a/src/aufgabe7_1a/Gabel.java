package aufgabe7_1a;

public class Gabel {

    private int gabelNummer = 0;
    private boolean gabelInBenutzung = false;


    public Gabel(int gabelNummer) {
        this.gabelNummer = gabelNummer;
    }


    public synchronized void aufnehmen(int nummer, String Seite) throws InterruptedException {

        PruefeGabelInBenutzung();
        System.out.println("Philosoph: " + nummer + " nimmt auf " + Seite);
        this.gabelInBenutzung = true;
        notifyAll();

    }

    public void ablegen(int nummer, String Seite) {
        System.out.println("Philosoph: " + nummer + " legt ab " + Seite);
        this.gabelInBenutzung = false;
    }

    public synchronized void PruefeGabelInBenutzung() throws InterruptedException {

        while (this.gabelInBenutzung){
            wait();
        }
    }

}
