package threads1;

import java.util.concurrent.TimeUnit;

public class threads1 {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("gogog");

		Utils.printThreadData(null);
		Thread myFirst = new Thread(new MyRunnable(-1));
		Thread mySecond = new Thread(new MyRunnable(3), "gogog");
		myFirst.start();
		mySecond.start();

		// start some anonymous threads from MyRunnable
		for (int i = 0; i < 10; i += 1) {
			(new Thread(new MyRunnable(i))).start();
		}

		try {
			myFirst.join();
			mySecond.join();
		} catch (Exception e) {

		}

		System.out.println("* * *");

	}

}

class MyRunnable implements Runnable {

	private final int v;

	public MyRunnable(int v) {
		this.v = v;
	}

	@Override
	public void run() {
		Utils.doit(v);

	}

}

class Utils {

	public static void printThreadData(Integer i) {
		Thread t = Thread.currentThread();
		System.out.printf("instance of %s(%s): %s\n", t.getClass().getSimpleName(), i, t.toString());
	}

	public static void doit(int i) {
		printThreadData(i);
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
