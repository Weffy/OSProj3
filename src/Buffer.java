import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer extends Thread {
	static int[] buffer = new int[6];
	static int dataStart = 0;
	static int dataEnd = 0;
	static int numOfItems = 0;
	static ReentrantLock l = new ReentrantLock();
	
	String name;
	
	public Buffer(String name) {
		this.name = name;
		
	}
	public Buffer(int p1, int p2, int p3, int p4, int p5, int p6) {
		int[] input = new int[6]; 
		input[0] = p1;
		input[1] = p2;
		input[2] = p3;
		input[3] = p4;
		input[4] = p5;
		input[5] = p6;
		
		for (int i = 0; i < input.length; i++) {
			produceItem(input[i]);
		}

	}
	
	public synchronized static void produceItem(int item) {
		buffer[dataEnd] = item; //writing 1's for data
		dataEnd++;
		numOfItems++;
		
	} 
	
	public synchronized static void consumeItem() {
		buffer[dataStart] = 0; //writing 0's for no data
		dataStart++;
		numOfItems--;
	}
	
	public void runSimulation() {
		System.out.println("run sim...");
		Buffer p1 = new Buffer("p1");
		p1.start();
		Buffer c1 = new Buffer("c1");
		c1.start();

	}
	public void run() {

		for (int i = 0; i < 5; i++) {
			sleep();
			System.out.println(this.name + ": run method");
			if (this.name.substring(0, 1).equals("p")) {
				System.out.println("I am a producer thread");
			} else if (this.name.substring(0, 1).equals("c")) {
				System.out.println("I am a consumer thread");
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
	
	public static void sleep() {
		long zzz = rng();
		try {
//			System.out.println("sleep time: " + zzz);
			TimeUnit.MILLISECONDS.sleep(zzz);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static long rng() {
		Random rng = new Random();
		long randNum = (long) rng.nextInt(500); 
		return randNum;
	}
	
	public static void main(String[] args) {
		//Part 0, Test 1 (optional)
		Buffer b = new Buffer(10, 1, 0, 20, 20, 10);
		b.runSimulation();
		//return true;
		
		
		
		
		
	}




}