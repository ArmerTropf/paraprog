/**
 * Created by mguenster on 29.05.17.
 */
public class Aufgabe8_2b {

    public static void main(String [] args) {

        Konto konto = new Konto(0);
        System.out.println("Aktueller_Kontostand: " + konto.getBetrag());

        Person pers1 = new Person("Person1", konto);
        Person pers2 = new Person("Person2", konto);
        Person pers3 = new Person("Person3", konto);
        Person pers4 = new Person("Person4", konto);
        Person pers5 = new Person("Person5", konto);
        Person pers6 = new Person("Person6", konto);

        pers1.start();
        pers2.start();
        pers3.start();
        pers4.start();
        pers5.start();
        pers6.start();



    }
}
