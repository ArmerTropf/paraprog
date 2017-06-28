package assignment3;

import java.util.concurrent.CyclicBarrier;

public class TheNode extends NodeAbstract {

	public int echoReceived = 0;

	public boolean isSleeping = true;
	public boolean wokenUpNeighbours = false;
	public Node wakeUpFrom = null;
	public boolean isDone = false;

	private boolean isEchoWriting = false;
	private boolean isEchoReading = false;
	
	private boolean isWakingUp = false;
	
	private boolean isReadingSleep = false;

	public TheNode(String name, boolean initiator, CyclicBarrier barrier) {
		super(name, initiator, barrier);
	}

	public synchronized void writeReceive(Node neighbour) {

		while (isEchoWriting) {
			System.out.println("WAIT WRITE: Jemand schreibt gerade das EchoReceived");

			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		isEchoWriting = true;
		System.out.println("SENDE echo von " + neighbour + " an: " + toString());
		echoReceived++;
		isEchoWriting = false;
		notifyAll();
	}

	public synchronized void readReceive() {

		while (isEchoReading) {
			System.out.println("WAIT READ: Jemand liest gerade das EchoReceived");

			try {
				wait();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}

		}

		isEchoReading = true;

		checkEchoReceivedForInitiator();
		checkEchoReceivedForOther();

		isEchoReading = false;
		notifyAll();

	}
	
	public synchronized void wantToWakeUp() {
		
		while (isWakingUp) {
			
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		isWakingUp = true;
		
		CyclicBarrier cbForWakeup = null;
		if (!initiator && neighbours.size() > 1){
			 cbForWakeup = new CyclicBarrier(neighbours.size() - 1);	
		}
		else {
			 cbForWakeup = new CyclicBarrier(neighbours.size());
		}
		
		if (!wokenUpNeighbours && neighbours.size() > 1) {

			for (Node neighbour : neighbours) {
				
				// nicht sich selbst aufwecken
				if (!(neighbour == wakeUpFrom)) {
					/*
					 * schicke alle knoten die geweckt werden in einen thread mit cyclicbarrier
					 * Werden alle gleichzeitig geweckt
					 */							
					new SyncronStarter(neighbour, this, cbForWakeup).start();
				}

			}
			wokenUpNeighbours = true;
		}
		isWakingUp = false;
		
		notifyAll();
	}
	
	public synchronized void getSleep() {
		
		while (isReadingSleep) {
			System.out.println("Wait in Reading Sleep");
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		isReadingSleep = true;
		if (!isSleeping) {

			wantToWakeUp();
			
			readReceive();
		}
		isReadingSleep = false;
	}
	
	

	@Override
	public void hello(Node neighbour) {
		/*
		 * Dient der Bidirektionalität. Wenn ein Knoten eine
		 * Nachbarschaftsbezieheung eingetragen hat, aber andersherum keine
		 * eingetragen wurde, wird durch das "hello" die fehlende
		 * Nachbarschaftsbeziehung nachgetragen
		 * 
		 * Bsp: A -> B aber nicht B -> A A sagt Hello B ... B Trägt A in seine
		 * Nachbarschaftsbeziehung ein
		 * 
		 */

		// System.out.println("Trage nachbarn ein: " + neighbour.toString() + "
		// in " + toString());
		neighbours.add(neighbour);

	}

	@Override
	public void wakeup(Node neighbour) {
		wakeUpFrom = neighbour;
		isSleeping = false;
		wantToWakeUp();
		//System.out.println("Knoten: " + wakeUpFrom + " weckt Knoten: " + toString());
		
	}

	@Override
	public void echo(Node neighbour, Object data) {
		// System.out.println("SENDE echo von " + neighbour + " an: " +
		// toString());
		writeReceive(neighbour);
		// echoReceived++;
	}

	@Override
	public void setupNeighbours(Node... neighbours) {

		for (int i = 0; i < neighbours.length; i++) {
			Node node = neighbours[i];
			this.neighbours.add(node);
		}

	}

	public void run() {
		
		
		while (!isDone) {

			if (initiator == true) {
				isSleeping = false;
				readReceive();
			}

			getSleep();

		}

		

	}

	public void showNeigbours() {
		for (Node neigbour : neighbours) {
			System.out.println("Knoten: " + toString() + " Nachbar: " + neigbour.toString());
		}
	}

	private void checkEchoReceivedForInitiator() {
		if (initiator == true && echoReceived == neighbours.size()) {
			System.out.println("bin durch... " + toString());
			isDone = true;
			
		}
	}

	private void checkEchoReceivedForOther() {
		if ((initiator == false) && (echoReceived == neighbours.size() - 1)) {
			wakeUpFrom.echo(this, "fertig");
			isDone = true;
			isSleeping = true;
			System.out.println("Thread Done: " + toString());

		}
	}

}
