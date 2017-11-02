import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Iterator;
import java.util.Scanner;

public class User implements Runnable{
	
	private final int ID;
	private final int WRITE_PROBABILITY = 10;
	private final int DEPRESSION_WRITE_BOUND = 5;
	private Control c;
	private int depression;
	private Scanner fileScanner;

	public User(int id, Control c) {
		ID = id;
		this.c = c;
		depression = 0;
	}
	
	public void run() {
		while(true) {
			if(depression >= 10) {
				writePotentially(true);
			}else {
				readFeed(5, true);
			}
			/*try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
	}
	
	private void writePotentially(boolean print) {
		int writeGate = ThreadLocalRandom.current().nextInt(0, WRITE_PROBABILITY);
		if(writeGate == 0) {
			String update = getUpdateFromFile();
			int depressionMeasure = ThreadLocalRandom.current().nextInt(1, DEPRESSION_WRITE_BOUND);
			pushToFeed(update, depressionMeasure);
			depression = 0;
			if(print) {
				c.print(update, ID, depression, false);
			}
		}
	}
	
	private void readFeed(int readAmount, boolean print) {
		Node[] myFeed = getFeed(readAmount);
		if(myFeed == null) {
			return;
		}
		for(int i = 0; i < myFeed.length; i++) {
			int depressionMeasure = myFeed[i].getDepressionMeasure();
			depression += depressionMeasure;
			if(print) {
				c.print(myFeed[i].getUpdate(), ID, depression, true);
			}
		}
	}
	
	private Node[] getFeed(int updateCount) {
		if(!c.dougLeaMode()) {
			Node[] updates = c.feed.subQueueToNodeArray(updateCount);
			return updates;
		}
		Iterator<Node> dequeueIterator = c.dlFeed.descendingIterator();
		Node[] updates = new Node[updateCount];
		for(int i = 0; i < updateCount; i++) {
			Node n = dequeueIterator.next();
			updates[i] = n;
		}
		return updates;
	}
	
	private void pushToFeed(String s, int depressionMeasure) {
		if(!c.dougLeaMode()) {
			c.feed.put(s, depressionMeasure);
		}else {
			Node n = new Node(s, depressionMeasure);
			c.dlFeed.add(n);
		}
	}
	
	
	public String getUpdateFromFile() {
		File f = c.getFile();
		try {
			try {
				fileScanner = new Scanner(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			int grabFromLine = ThreadLocalRandom.current().nextInt(0, c.fileSize());
			String line = "";
			for(int i = 0; i < grabFromLine; i++) {
				line = fileScanner.nextLine();
			}
			return line;
		}finally {
			fileScanner.close();
		}
	}
	
}
