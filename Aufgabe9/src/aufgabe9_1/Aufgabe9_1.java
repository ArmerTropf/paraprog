package aufgabe9_1;

/**
 * Created by mguenster on 05.06.17.
 */
public class Aufgabe9_1 {


    public static void main(String[] args) {

        RWMonitor monitor = new RWMonitor();

//        RWWriter writer1 = new RWWriter(monitor, "writer1");
//        RWWriter writer2 = new RWWriter(monitor, "writer2");
//        RWWriter writer3 = new RWWriter(monitor, "writer3");
//        RWWriter writer4 = new RWWriter(monitor, "writer4");
//        RWWriter writer5 = new RWWriter(monitor, "writer5");
//        RWWriter writer6 = new RWWriter(monitor, "writer6");
//        RWWriter writer7 = new RWWriter(monitor, "writer7");
//        RWWriter writer8 = new RWWriter(monitor, "writer8");
//        RWWriter writer9 = new RWWriter(monitor, "writer9");
//        RWWriter writer10 = new RWWriter(monitor, "writer10");
//
//        RWReader reader1 = new RWReader(monitor, "reader1");
//        RWReader reader2 = new RWReader(monitor, "reader2");
//        RWReader reader3 = new RWReader(monitor, "reader3");
//        RWReader reader4 = new RWReader(monitor, "reader4");
//        RWReader reader5 = new RWReader(monitor, "reader5");
//        RWReader reader6 = new RWReader(monitor, "reader6");
//        RWReader reader7 = new RWReader(monitor, "reader7");
//        RWReader reader8 = new RWReader(monitor, "reader8");
//        RWReader reader9 = new RWReader(monitor, "reader9");
//        RWReader reader10 = new RWReader(monitor, "reader10");
//
//        //RWReader reader1 = new RWReader(monitor, "reader1");
//
//        writer1.start();writer2.start();writer3.start();writer4.start();writer5.start();
//        writer6.start();writer7.start();writer8.start();writer9.start();writer10.start();
//        reader1.start();reader2.start();reader3.start();reader4.start();reader5.start();
//        reader1.start();reader2.start();reader3.start();reader4.start();reader5.start();

        for (int i = 0; i < 10 ; i++) {
            new RWWriter(monitor, "writer"+i).start();
            new RWReader(monitor, "reader"+i).start();
        }


    }
}
