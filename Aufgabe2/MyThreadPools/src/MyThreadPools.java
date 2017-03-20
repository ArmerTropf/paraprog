import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyThreadPools {

	
	
	public static void main(String[] args) throws InterruptedException {

		
//		Thread test = new Thread(new MyThread("homo", 5));
//		test.start();
		
		ExecutorService myExCached = Executors.newCachedThreadPool();
		ExecutorService myExFixed = Executors.newFixedThreadPool(2);
		ExecutorService myExSingle = Executors.newSingleThreadExecutor();
		ExecutorService myExStealing = Executors.newWorkStealingPool();
		ExecutorService myExScheduled = Executors.newScheduledThreadPool(10);
	
		
		myLoop(myExCached,"cached");
		Thread.sleep(1000);
		myLoop(myExFixed,"fixed");
		Thread.sleep(1000);
		myLoop(myExSingle,"Single");
		Thread.sleep(1000);		
		myLoop(myExScheduled,"scheduled");
		Thread.sleep(1000);	
//		myLoop(myExFixed,"fixed");
		myLoop(myExStealing,"Stealing");




		myExCached.shutdown();
		myExFixed.shutdown();
		myExSingle.shutdown();

	
		
	}

	private static void myLoop(ExecutorService myExService, String type)
	{
		for (int i = 0; i < 4; i++) {
			int zufall = (int)(Math.random()*10 % 6);
			System.out.println("th"+i + " zufallszahl: " +zufall);
			myExService.submit(new MyThread("th"+i+"_"+type, zufall));
			
		}
		
	}
	
}

class MyThread implements Runnable {

	int zahl;
	String homo1;

	public MyThread(String homo1, int zahl) {
		this.homo1 = homo1;
		this.zahl = zahl;
	}

	@Override
	public void run() {

		for (int i = 1; i <= this.zahl; i++) {
			try {
				Thread.sleep(1);
			} catch (Exception e) {
				// TODO: handle exception
			}
			Thread t = Thread.currentThread();
			System.out.println("Durchgang: " + i + " Thread: " + this.homo1 + " \tPOOL: " + t.toString());
		}

				
	}

}