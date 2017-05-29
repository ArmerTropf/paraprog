package Aufgabe8_3;

/**
 * Created by mguenster on 29.05.17.
 */
public class Start {


    public static void main (String[] args) {



        BufferMonitor bf1 = new BufferMonitor(2);

        User user1 = new User(bf1);
        User user2 = new User(bf1);

        user1.start();user2.start();


    }

}
