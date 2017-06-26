package assignment3;

import java.util.concurrent.CyclicBarrier;

public class TheNode extends NodeAbstract {

	public int echoReceived = 0;
	public boolean isSleeping = true;
	public boolean wokenUpNeighbours = false;
	public Node wakeUpFrom = null;
	public boolean isDone = false;
	

	public TheNode(String name, boolean initiator, CyclicBarrier barrier ) {
		super(name, initiator, barrier);
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
		
		System.out.println("Knoten: " + wakeUpFrom + " weckt Knoten: " + toString());
		isSleeping = false;
	}
	

	@Override
	public void echo(Node neighbour, Object data) {
//		System.out.println("SENDE echo von " + neighbour + " an: " + toString());
		echoReceived++;
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
				
				if (echoReceived == neighbours.size()) {
					System.out.println("bin durch... " + toString() );
					isDone = true;
				}			
			}

			
			if (!isSleeping) {
				
				
				if (!wokenUpNeighbours && neighbours.size() > 1) {
					for (Node neighbour : neighbours) {
						if (!(neighbour == wakeUpFrom)) {
							neighbour.wakeup(this);	
						}
						
					}	
					wokenUpNeighbours = true;
				}
				
				if (( initiator == false ) && (echoReceived == neighbours.size()-1) ){
					wakeUpFrom.echo(this, "fertig");
					isDone = true;
					isSleeping = !isSleeping;
										
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

}
