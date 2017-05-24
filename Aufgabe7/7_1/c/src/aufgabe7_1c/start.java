package aufgabe7_1c;


public class start {


    public static void main(String[] argv) {
        System.out.println("Hallo");


        Gabel eins = new Gabel(1);
        Gabel zwei = new Gabel(2);
        Gabel drei = new Gabel(3);
        Gabel vier = new Gabel(4);
        Gabel fuenf = new Gabel(5);


        Philosoph A = new Philosoph(1, fuenf,eins);
        Philosoph B = new Philosoph(2, eins,zwei);
        Philosoph C = new Philosoph(3, zwei,drei);
        Philosoph D = new Philosoph(4, drei,vier);
        Philosoph E = new Philosoph(5, vier,fuenf);



    A.start();
    B.start();
    C.start();
    D.start();
    E.start();



    }



}
