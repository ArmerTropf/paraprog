package Aufgabe8_3;

/**
 * Created by mguenster on 29.05.17.
 */
public class User extends Thread {

    BufferMonitor bf;

    public User(BufferMonitor bf) {
        this.bf = bf;
    }


    public void run() {



        for (int i = 0; i < 10; i++) {

            int zahl = (int)(Math.random() * 10);


            if (zahl % 2 == 0 ) {

                try {
                    bf.put(zahl);
                    System.out.println("Eingefuegt : " + zahl);
                }catch (RuntimeException e) {
                    System.out.println("Versuche erneut einzufuegen");
                }

            }
            else {
                System.out.println("Entnehme: " + bf.take());
            }





        }

    }



}
