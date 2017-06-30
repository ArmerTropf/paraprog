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

	private boolean isWritingEchoReceived = false;


/*
 * Monitore
 */
	public synchronized void writeEcho(Node caller, Object text) {

		while (isWritingEchoReceived) {
			System.out.println("Jemand schreibt gerade echo");

			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Echo von: " + caller.toString() + " an: " + toString());
		isWritingEchoReceived = true;
		echoReceived++;
		isWritingEchoReceived = false;
		notify();

	}

	public synchronized boolean readSleeping() {
		return isSleeping;
	}

	public synchronized void writeSingleEchoReceived() {
		echoReceived++;
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

		if (readSleeping()) {
			wakeUpFrom = neighbour;
			isSleeping = false;
			System.out.println("Knoten: " + wakeUpFrom + " weckt Knoten: " + toString());
		} else {
			writeSingleEchoReceived();
		}

	}

	@Override
	public void echo(Node neighbour, Object data) {
		writeEcho(neighbour, data);
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
			while (!isDone) {
				checkInitiatorToWakeUpNodes();

				if (!readSleeping()) {
					checkWakeUp();
					checkEcho();
				}

			}

		}

		System.out.println("Thread Done: " + toString());

	}

	public void showNeigbours() {
		for (Node neigbour : neighbours) {
			System.out.println("Knoten: " + toString() + " Nachbar: " + neigbour.toString());
		}
	}

	private void checkInitiatorToWakeUpNodes() {
		if (initiator == true) {
			isSleeping = false;

			if (echoReceived == neighbours.size()) {
				System.out.println("bin durch... " + toString());
				isDone = true;
			}
		}
	}

	private  void checkEcho() {
		if ((initiator == false) && (echoReceived == neighbours.size() - 1)) {
			wakeUpFrom.echo(this, "fertig");
			isDone = true;
			isSleeping = true;

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

				if (!(neighbour == wakeUpFrom)) {
					// start single thread to start wakeup()
					new SyncronStarter(neighbour, this, cbForWakeup).start();
				}

			}
			wokenUpNeighbours = true;
		}
	}

	private void getBidirectionalNeighbours() {

		for (Node neighbour : neighbours) {
			neighbour.hello(this);
		}
	}


}
