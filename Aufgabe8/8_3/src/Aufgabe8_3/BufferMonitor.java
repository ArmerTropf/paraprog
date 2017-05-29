package Aufgabe8_3;



/**
 * Created by mguenster on 29.05.17.
 */
public class BufferMonitor implements IAufgabe8_3  {

    int [] buffer;
    int in = 0 ;
    int out = 0;
    int kapazitaet = 0;
    int belegt = 0;

    public BufferMonitor(int kapazitaet) {
        buffer = new int [kapazitaet];

    }

    @Override
    public synchronized void put(int zahl) {

        if ( belegt ==  buffer.length) {
            try {
                System.out.println("Bin voll, warte...");

                wait(1000);
                if ( belegt == buffer.length )   {
                    throw new RuntimeException("Leider voll");
                }
            }
            catch (Exception e) {

            }

        }

        buffer[in] = zahl;

        if ( ++in % buffer.length == 0 ){
            in = 0;
        }

        belegt++;
        System.out.println("Belegt: " + belegt + " in: " + in + " out: " + out);
        notifyAll();

    }

    @Override
    public synchronized int take() {

        if ( belegt == 0 ) {
            try {
                System.out.println("Bin leer, warte...");
                wait();
            }
            catch (Exception e) {

            }
        }
        belegt--;
        notifyAll();
        System.out.println("Belegt: " + belegt + " in: " + in + " out: " + out);
        return buffer[out++];
    }

    @Override
    public synchronized void clear() {

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = Integer.parseInt(null);
            in = 0;
            out = 0;

        }
    }

}
