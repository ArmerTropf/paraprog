import jdk.nashorn.internal.runtime.ECMAException;

/**
 * Created by mguenster on 29.05.17.
 */
public class Person extends Thread {

    private Konto konto;


    public Person (String Name, Konto konto) {
        super(Name);
        this.konto = konto;
    }


    @Override
    public void run() {



        for (int i = 0 ; i < 10 ; i++) {

            int betrag = 1;

            int random =  (int)(Math.random() * 10);

            if ((random % 2 ) == 0  ) {


                try {
                    this.konto.abheben(betrag);

                }catch (Exception e) {
                    System.out.println(Thread.currentThread().getName() + ": Kein Geld da, gehe wieder nach Hause");
                }


            }
            else {

                this.konto.einzahlen(betrag);

            }



        }


    }

}
