package assignment3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TheNode extends NodeAbstract {

	// private AtomicInteger echoReceived = new AtomicInteger(0);
	// private AtomicBoolean isSleeping = new AtomicBoolean(true);
	private boolean wokenUpNeighbours = false;
	private Node wakeUpFrom = null;
	private boolean isDone = false;

	private int echoReceived = 0;
	private boolean isSleeping = true;

//	private boolean isWritingEchoReceived = false;


/*
 * Monitore
 */
//	public synchronized void writeEcho(Node caller, Object text) {
//
//		while (isWritingEchoReceived) {
//			System.out.println("Jemand schreibt gerade echo");
//
//			try {
//				wait();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		System.out.println("Echo von: " + caller.toString() + " an: " + toString());
//		isWritingEchoReceived = true;
//		echoReceived++;
//		isWritingEchoReceived = false;
//		notify();
//
//	}
	public synchronized void writeEcho(Node caller, Object text) {
		echoReceived++;
		if (!initiator) {
			System.out.println("Thread: " + getName() + " Setze echoReceived++ ; hat nun " + echoReceived + "/" + (neighbours.size()-1));
		} else {
			System.out.println("Thread: " + getName() + " Setze echoReceived++ ; hat nun " + echoReceived + "/" + (neighbours.size()));
		}
		
	}
	

	public synchronized boolean readSleeping() {
		return isSleeping;
	}
	
	public synchronized void writeSleeping(boolean status) {
		isSleeping = status; 
	}
	
	public synchronized int readEchoReceived() {
		return echoReceived;
	}

	public synchronized void writeSingleEchoReceived() {
		echoReceived++;
		System.out.println("Thread: " + getName() + " war schon wach. Setze echoReceived++ ; hat nun " + echoReceived + "/" + (neighbours.size()-1));		
		if(echoReceived == neighbours.size() ) {
//			checkEcho();
		}
		checkEcho();
	}
	
	public synchronized void writeIsDone(boolean status) {
		isDone = status;
	}
	
	public synchronized boolean readIsDone() {
		return isDone;
	}
	
	public synchronized void writeWakeUpFrom(Node sourceNode) {
		wakeUpFrom = sourceNode;
	}
	
	public synchronized Node readWakeUpFrom() {
		return wakeUpFrom;
	}
	
	

	// Ende Monitore
	//--------------
	
	public TheNode(String name, boolean initiator, CyclicBarrier barrier) {
		super(name, initiator, barrier);
	}

	@Override
	public void hello(Node neighbour) {
		// System.out.println("fuege nachbar: " + neighbour.toString() + " zu thread: " + toString());
		neighbours.add(neighbour);
	}

	@Override
	public void wakeup(Node neighbour) {
		
		//Wenn er Schlaeft, auf
		if (readSleeping()) {
			writeWakeUpFrom(neighbour);//wakeUpFrom = neighbour;
			writeSleeping(false); //;isSleeping = false;
//			checkEcho();

		} 
		// Wenn Knoten wach, nur echoReceived++
		else {
			
			writeSingleEchoReceived();
//			checkEcho();
		}
		
	}

	@Override
	public void echo(Node neighbour, Object data) {	
		writeEcho(neighbour, data);
		checkEcho();
	}

	@Override
	public void setupNeighbours(Node... neighbours) {

		for (int i = 0; i < neighbours.length; i++) {
			Node node = neighbours[i];
			this.neighbours.add(node);
		}
		getBidirectionalNeighbours();

	}

	public void run() {
		
		
		try {
			barrier.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (neighbours.size() != 0) {
			while (!readIsDone()) {
				checkInitiatorToWakeUpNodes();

				if (!readSleeping()) {
					checkWakeUp();
				}

			}

		}

		System.out.println("Thread: " + toString() + " TERMINATED");

	}

	public void showNeigbours() {
		for (Node neigbour : neighbours) {
			System.out.println("Knoten: " + toString() + " Nachbar: " + neigbour.toString());
		}
	}

	private void checkInitiatorToWakeUpNodes() {
		if (initiator == true) {
			writeSleeping(false);

			if (readEchoReceived() == neighbours.size()) {
				System.out.println("bin durch... " + toString());
				writeIsDone(true); //isDone = true;
			}
		}
	}

	private synchronized void checkEcho() {
//		System.out.println("CHECK: Thread: " + getName() + " init: " + initiator + " echoReceived: " + readEchoReceived() + " neghbours - 1: " + (neighbours.size() -1));
		if ((initiator == false) && (readEchoReceived() == neighbours.size() - 1)) {
			if (neighbours.size() - 1 == 0 ) {
				System.out.println("Thread: " + getName() + " ohne Nachbarn. Benachrichtige " + readWakeUpFrom().toString());
			} else {
				System.out.println("Thread: " + getName() + " Alle Echos erhalten. Benachrichtige " + readWakeUpFrom().toString());				
			}
			
			readWakeUpFrom().echo(this, "fertig");//wakeUpFrom.echo(this, "fertig");
			writeIsDone(true); //isDone = true;
			writeSleeping(true);
			System.out.println("Thread: " + getName() + " STATE: " + this.getState());
			

		} else if (initiator && readEchoReceived() != neighbours.size()) {
			waitForEchoFromNeighbour();
			
//			writeIsDone(true); //isDone = true;
//			writeSleeping(true);
		}
			
	}

	private void checkWakeUp() {

		if (!wokenUpNeighbours && neighbours.size() > 1) {
			
			CyclicBarrier cbForWakeup = null;
			if (!initiator && neighbours.size() > 1) {
				cbForWakeup = new CyclicBarrier(neighbours.size() - 1);
			} else {
				cbForWakeup = new CyclicBarrier(neighbours.size());
			}

			for (Node neighbour : neighbours) {

				if (!(neighbour == readWakeUpFrom())) {
					// start single thread to start wakeup()
					new SyncronStarter(neighbour, this, cbForWakeup).start();
				}

			}
			wokenUpNeighbours = true;
			
			if ( neighbours.size() != 0 ) {
				System.out.println("Thread: " + getName() + " Alle Nachbarn aufgeweckt");
				waitForEchoFromNeighbour();
			}
		}
	}

	private void getBidirectionalNeighbours() {

		for (Node neighbour : neighbours) {
			neighbour.hello(this);
		}
	}
	
	
	private synchronized void waitForEchoFromNeighbour() {
		
			try {
				if (!initiator) {
					System.out.println("Thread: " + getName() + " hat " + echoReceived + "/" + (neighbours.size()-1) + " Echos bekommen ...wait()");	
				} else {
					System.out.println("Thread: " + getName() + " INITIATOR warte auf Echo " + echoReceived + "/" + (neighbours.size()) + " ...wait()");
				}
				
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			checkEcho();
		
	}
	
	
	public synchronized void notifyEchoReceived() {
		notifyAll();
	}


}
