import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer extends Thread {

	static String[] buffer;
	static Buffer[] consumers;
	static Buffer[] producers;
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
	
	String name;
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
		
		System.out.println("prod arr: " + producers.length);
		System.out.println("cons arr: " + consumers.length);		
		

	}
	
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
	
	public String getTimestamp() {
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		String timestamp = df.format(dateobj);
		return timestamp;
	}
	
	
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
	
	public static int size() {
		return buffer.length; 
	}
	
	public void printData() {
		for (int i = 0; i < buffer.length; i++) {
			System.out.println(buffer[i]);
		}
	}
	
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
		Buffer b = new Buffer(10, 1, 1, 20, 20, 10);
		b.runSimulation();
		//return true;
		
		
		
		
		
	}




}