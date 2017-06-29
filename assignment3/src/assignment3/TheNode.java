package assignment3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TheNode extends NodeAbstract {

	public AtomicInteger echoReceived = new AtomicInteger(0);
	public AtomicBoolean isSleeping = new AtomicBoolean(true);
	public boolean wokenUpNeighbours = false;
	public Node wakeUpFrom = null;
	public boolean isDone = false;

	public TheNode(String name, boolean initiator, CyclicBarrier barrier) {
		super(name, initiator, barrier);
		System.out.println("barrier: " + barrier.getParties() + " thread: " + getName());
	}

	@Override
	public void hello(Node neighbour) {
		// System.out.println("fuege nachbar: " + neighbour.toString() + " zu thread: " + toString());
		neighbours.add(neighbour);
	}

	@Override
	public void wakeup(Node neighbour) {
		wakeUpFrom = neighbour;
		isSleeping.set(!isSleeping.get());
		System.out.println("Knoten: " + wakeUpFrom + " weckt Knoten: " + toString());

	}

	@Override
	public void echo(Node neighbour, Object data) {
		System.out.println("SENDE echo von " + neighbour + " an: " + toString());
		echoReceived.incrementAndGet();
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

		while (!isDone) {
			checkInitiatorToWakeUpNodes();

			if (!isSleeping.get()) {
				checkWakeUp();
				checkEcho();
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
			isSleeping.set(false);

			if (echoReceived.get() == neighbours.size()) {
				System.out.println("bin durch... " + toString());
				isDone = true;
			}
		}
	}

	private void checkEcho() {
		if ((initiator == false) && (echoReceived.get() == neighbours.size() - 1)) {
			wakeUpFrom.echo(this, "fertig");
			isDone = true;
			isSleeping.set(true);

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
					/*
					 * schicke alle knoten die geweckt werden in einen thread
					 * mit cyclicbarrier Werden alle gleichzeitig geweckt
					 */
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
