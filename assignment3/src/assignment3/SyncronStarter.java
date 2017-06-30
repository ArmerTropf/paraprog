package assignment3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SyncronStarter extends Thread {
	
	CyclicBarrier cb = null;
	Node neighbourToWake = null;
	Node wakeUpSource = null;
	
	public SyncronStarter(Node neighbourToWake,Node wakeUpSource, CyclicBarrier cb) {
		this.cb = cb;
		this.neighbourToWake = neighbourToWake;
		this.wakeUpSource = wakeUpSource;
	}
	
	
	public void run() {
		
		try {
			cb.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//System.out.println("Knoten: " + wakeUpSource + " weckt Knoten: " + neighbourToWake.toString());
		neighbourToWake.wakeup(wakeUpSource);
		
	}

}
