import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyCallableFuture {

	public static void main(String[] args) throws InterruptedException {
		
		ExecutorService myExCached = Executors.newCachedThreadPool();
		
		
		Thread RunnTh1 = new Thread(new MyRunnable(3, "RunnTh1"));
		Thread RunnTh2 = new Thread(new MyRunnable(5, "RunnTh2"));
		
		
		try 
		{
//			RunnTh1.start();
//			RunnTh2.start();
			
			myExCached.submit(new MyRunnable(3, "RunnTh1"));
			myExCached.submit(new MyRunnable(5, "RunnTh2"));
			
			System.out.println(myExCached.submit(new MyCallable(3, "CallTh1")).get());
			System.out.println("zweiter");
			System.out.println(myExCached.submit(new MyCallable(5, "CallTh2")).get());
						
			myExCached.shutdown();
		} catch (Exception e) {
		
		}

	}

}

class MyRunnable implements Runnable {
	int meinezahl;
	String name;

	public MyRunnable(int meinezahl, String name) {
		this.meinezahl = meinezahl;
		this.name = name;
	}

	@Override
	public void run(){

		for (int i = 0; i < this.meinezahl; i++) {
			System.out.println(this.name + " " + i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}		
	}

}

class MyCallable implements Callable<Integer> {
	
	int meinezahl;
	String name;

	public MyCallable(int meinezahl, String name) {
		this.meinezahl = meinezahl;
		this.name = name;
	}

	@Override
	public Integer call() throws Exception {

		for (int i = 0; i < this.meinezahl; i++) {
			System.out.println(this.name + " " + i);
			Thread.sleep(1000);
			
		}

		return this.meinezahl;
	}

}


