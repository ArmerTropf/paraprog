package myRecursivAction_Task;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 
 * @author ArmerTropf
 * 
 *
 */
public class myRecursivAction_Task {

	public static void main(String[] args) {
		// 0 1 1 2 3 5 8 13

		
		int tester = ForkJoinPool.commonPool().invoke(new Fibonacci(6));
		System.out.println(tester);
		
		
		/*
		 * If you use a no-argument constructor, by default, it creates a pool of size that equals the number of available processors obtained using above technique.
		 */
		ForkJoinPool myJoinPool = new ForkJoinPool();
		try {
			/*
			 * 1) execute() method //Desired asynchronous execution; call its fork method to split the work between multiple threads.
			 * 2) invoke() method: //Await to obtain the result; call the invoke method on the pool.
			 * 3) submit() method: //Returns a Future object that you can use for checking status and obtaining the result on its completion.
			 */			
			System.out.println(myJoinPool.submit(new Fibonacci(6)).get());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}


/**
 * 
 * Value is the Index of the computed row
 *
 */
class Fibonacci extends RecursiveTask<Integer> {
	final int n;

	Fibonacci(int n) {
		this.n = n;
	}

	protected Integer compute() {
		if (n <= 1)  // 6
			return n;
		Fibonacci f1 = new Fibonacci(n - 1); // 5 , 4 , 3 , 2 , 1
		f1.fork();
		Fibonacci f2 = new Fibonacci(n - 2);
		return f2.compute() + f1.join();
	}

}