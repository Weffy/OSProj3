import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer extends Thread {

	/*
	 * arrays to manage buffer
	 * consumer threads
	 * producer threads
	 * 
	 * she didnt specify how to manage these things
	 */
	static String[] buffer;
	static Buffer[] consumers;
	static Buffer[] producers;
	
	
	/*
	 * using these as pointers to track where we are in teh array
	 * these ints will just point to the indicies in the arrays
	 */
	static int dataStart = 0;
	static int dataEnd = 0;
	static int numOfItems = 0;
	
	static ReentrantLock l = new ReentrantLock();
	
	
	
	
	//constructor items
	/*
	* 
	* There should be a constructor for the Buffer class that takes in 6 ints as its parameters (in this order): 
	* size of the buffer, 
	* number of producers, 
	* number of consumers, 
	* max number of milliseconds for the producer to sleep, 
	* max number of milliseconds for the consumer to sleep, 
	* and the number of messages that each producer should produce.
	*
	*/
	static int size;
	static int numProd;
	static int numCons;
	static long zzzProd;
	static long zzzCons;
	static int prodMsgs;
	
	/*
	 * how we will identify the Thread
	 * consumers will do 1 thing, producers do the other
	 * you can see how i split a lot of the methods using this
	 */
	String name;
	/*
	 * the producers are supposed to produce a set # of messages...
	 * I just have a small for loop running and i'm not using this as a limit yet...
	 */
	int msgsProduced;
	
	public Buffer(String name) {
		this.name = name;
		
	}
	
	public Buffer(int size, int numProd, int numCons, int zzzProd, int zzzCons, int prodMsgs) {
		Buffer.size = size;
		buffer = new String[size];
		
		Buffer.numProd = numProd;
		producers = new Buffer[numProd];
		createThreads(numProd, "p");
		
		Buffer.numCons = numCons;
		consumers = new Buffer[numCons];
		createThreads(numCons, "c");
		
		Buffer.zzzProd = zzzProd;
		Buffer.zzzCons = zzzCons;
		Buffer.prodMsgs = prodMsgs;
		
		//stubs!
		System.out.println("prod arr: " + producers.length);
		System.out.println("cons arr: " + consumers.length);		
		

	}
	/*
	 * figures out if it is a consumer or producer
	 * assigns to the corresponding array and populates the array
	 */
	public static void createThreads(int amount, String x) {
		
		if (x.equals("p")) {
			for (int i = 0; i < numProd; i++) {
				String name = "p" + i;
				Buffer buffer = new Buffer(name);
				producers[i] = buffer;
			}
		} else if (x.equals("c")) {
			for (int i = 0; i < numCons; i++) {
				String name = "c" + i;
				Buffer buffer = new Buffer(name);
				consumers[i] = buffer;
			}
		}

	}
	
	/*
	 * she offered a different method to get timestamps on comet...
	 * I couldn't get it to work.  I found this method to get a timestamp
	 * if you can figure out her method, go for it...
	 * otherwise, this should be sufficient
	 */
	public String getTimestamp() {
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		String timestamp = df.format(dateobj);
		return timestamp;
	}
	
	/*
	 * the consumers shouldn't technically be making a message and putting it into the array...
	 * I am using the messages to see that they are doing anything at all
	 */
	public String produceMessage() {
		String type = "";
		if (this.name.substring(0, 1).equals("p")) {
			type = " produced...";
		} else if (this.name.substring(0, 1).equals("c")) {
			type = " consumed...";		
		}
		StringBuilder message = new StringBuilder(this.name);
		message.append(type);
		String timestamp = getTimestamp();
		message.append(timestamp);
		return new String(message);

	}
	
	/*
	 * keeping these two synch'd should manage a lot of our concurrency issues with data
	 */
	public synchronized void produceItem() {
		String message = produceMessage();
		System.out.println(message);
		buffer[dataEnd] = message; //writing 1's for data
		dataEnd++;
		numOfItems++;
		
	} 
	
	public synchronized void consumeItem() {
		String message = produceMessage();
		System.out.println(message);
		buffer[dataStart] = message; //writing 0's for no data
		dataStart++;
		numOfItems--;
	}
	
	/*
	 * just goes through the arrays and starts all of the threads 
	 */
	public void runSimulation() {
		/*
		 * use teh arrays here!!!!
		 */
		System.out.println("run sim...");
//		Buffer p1 = new Buffer("p1");
//		p1.start();
//		Buffer c1 = new Buffer("c1");
//		c1.start();
		
		for (int i = 0; i < producers.length; i++) {
			producers[i].start();
		}
		for (int i = 0; i < consumers.length; i++) {
			consumers[i].start();
		}

	}
	
	/*
	 * we need logic to determine if we can call a producer or consumer...
	 * lock, CV, or semaphore shit will go here
	 * right now they just call their methods...
	 */
	public void run() {

		for (int i = 0; i < 5; i++) {
			sleep();
			System.out.println(this.name + ": run method");
			if (this.name.substring(0, 1).equals("p")) {
//				System.out.println("I am a producer thread");
				this.produceItem();
			} else if (this.name.substring(0, 1).equals("c")) {
//				System.out.println("I am a consumer thread");
				this.consumeItem();
			}
			sleep();
		}
	}
	
	/*
	 * i was using this for testing purposes...
	 * might not be needed anymore
	 */
	public static int size() {
		return buffer.length; 
	}

	/*
	 * i was using this for testing purposes...
	 * might not be needed anymore
	 */
	public void printData() {
		for (int i = 0; i < buffer.length; i++) {
			System.out.println(buffer[i]);
		}
	}
	
	/*
	 * borrowed this from HW5 & HW6...
	 * i wanted to sleep the threads to force the scheduler to switch between the active threads
	 * we can start looking at the strings in the buffer to see if there are data issues
	 */
	public void sleep() {
		long zzz = 0;
		if (this.name.substring(0, 1).equals("p")) {
			zzz = zzzProd;
		} else if (this.name.substring(0, 1).equals("c")) {
			zzz = zzzCons;
		}
		
		try {
//			System.out.println("sleep time: " + zzz);
			TimeUnit.MILLISECONDS.sleep(zzz);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		//Part 0, Test 1 (optional)
		//Buffer b = new Buffer(10, 1, 0, 20, 20, 10);
		//b.runSimulation();
		//return true;
		
		//put my own test so we can have 1 of each thread...
		//her optional test above only has 1 producer...no consumers...
		Buffer b = new Buffer(10, 1, 1, 20, 20, 10);
		b.runSimulation();
		
		
		
	}




}