package assignment3;

import java.util.concurrent.CyclicBarrier;

public class Start {

	public static void main(String [] args) throws InterruptedException {
				
		
		CyclicBarrier cb = new CyclicBarrier(6);
		
		TheNode myNode1 = new TheNode("1", true, cb );
		TheNode myNode2 = new TheNode("2", false, cb );
		TheNode myNode3 = new TheNode("3", false, cb );
		TheNode myNode4 = new TheNode("4", false, cb );
		TheNode myNode5 = new TheNode("5", false, cb );
		TheNode myNode6 = new TheNode("6", false, cb );

		
		/*
		 * Vollstaendiger Graph 
		 * 
		 * 1---2  
		 * | X |  
		 * 4---3
		 * 
		 */	
//		myNode1.setupNeighbours(myNode2,myNode3,myNode4);
//		myNode2.setupNeighbours(myNode3,myNode4); 
//		myNode3.setupNeighbours(myNode4);
//		myNode4.setupNeighbours();
//		myNode5.setupNeighbours();
//		myNode6.setupNeighbours();

		/*
		 * Kreis
		 * 
		 * 1---2  
		 * |   |  
		 * 4---3
		 * 
		 */
//		myNode1.setupNeighbours(myNode2);
//		myNode2.setupNeighbours(myNode3); 
//		myNode3.setupNeighbours(myNode4);
//		myNode4.setupNeighbours(myNode1);
//		myNode5.setupNeighbours();
//		myNode6.setupNeighbours();
		
		/*
		 * Schlinge
		 * 
		 * --1--2
		 * |_|
		 * 
		 */
		myNode1.setupNeighbours(myNode1,myNode2);
		myNode2.setupNeighbours(); 
		myNode3.setupNeighbours();
		myNode4.setupNeighbours();
		myNode5.setupNeighbours();
		myNode6.setupNeighbours();

		/*
		 * Baum
		 * 
		 * 1--3--4--5
		 * |	 |
		 * 2     6	
		 *
		 */	 
//		myNode1.setupNeighbours(myNode2,myNode3);
//		myNode2.setupNeighbours(); 
//		myNode3.setupNeighbours(myNode4);
//		myNode4.setupNeighbours(myNode5,myNode6);
//		myNode5.setupNeighbours(myNode4);
//		myNode6.setupNeighbours(myNode4);
		
		myNode1.start();
		myNode2.start();
		myNode3.start();
		myNode4.start();
		myNode5.start();
		myNode6.start();

		myNode1.join();
		myNode2.join();
		myNode3.join();
		myNode4.join();
		myNode5.join();
		myNode6.join();
		 
		myNode1.showNeigbours();
		myNode2.showNeigbours();
		myNode3.showNeigbours();
		myNode4.showNeigbours();
		myNode5.showNeigbours();
		myNode6.showNeigbours();
		
	}
}
