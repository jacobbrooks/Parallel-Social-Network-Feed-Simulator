import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Control {
	
	private final int USER_COUNT;

	public volatile MichaelScottQueue feed;
	public volatile ConcurrentLinkedDeque<Node> dlFeed;
	private File updates;
	private Random r;
	private Scanner fileScanner;
	private int fileSize;
	private Thread[] users;
	private Thread secretPopper;
	private PrintingModule printingModule;
	private boolean dougLeaMode;
	
	public Control(String fileName, int userCount, boolean dougLeaMode) {
		this.dougLeaMode = dougLeaMode;
		USER_COUNT = userCount;
		
		if(!dougLeaMode) {
			feed = new MichaelScottQueue();
		}else {
			dlFeed = new ConcurrentLinkedDeque<Node>();
		}
		
		updates = new File(fileName);
		r = new Random();
		users = new Thread[USER_COUNT];
		printingModule = new PrintingModule();
		initializeThreads();

		try {
			initializeQueue(10, 5);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void execute() {
		startThreads();
	}
	
	private void startThreads() {
		for(int i = 0; i < USER_COUNT; i++) {
			users[i].start();
		}
		secretPopper.start();
	}
	
	private void initializeThreads() {
		for(int i = 0; i < USER_COUNT; i++) {
			users[i] = new Thread(new User(i, this));
		}
		secretPopper = new Thread(new SecretPopper(this));
	}
	
	private void initializeQueue(int elements, int depressionMeasureBound) throws FileNotFoundException {
		r = new Random();
		fileScanner = new Scanner(updates);
		String stringFileSize = fileScanner.nextLine();
		fileSize = Integer.parseInt(stringFileSize);
		for(int i = 0; i < elements; i++) {
			if(fileScanner.hasNextLine()) {
				String update = fileScanner.nextLine();
				int depressionMeasure = r.nextInt(depressionMeasureBound);
				
				if(!dougLeaMode) {
					feed.put(update, depressionMeasure);
				}else {
					Node n = new Node(update, depressionMeasure);
					dlFeed.add(n);
				}
				
			}else {
				break;
			}
		}
		fileScanner.close();
	}
	
	public File getFile() {
		return updates;
	}
	
	public int fileSize() {
		return fileSize;
	}
	
	public void popFeed(int popCount) {
		for(int i = 0; i < popCount; i++) {
			if(!dougLeaMode) {
				feed.poll();
			}else {
				dlFeed.poll();
			}
		}
	}
	
	public int feedSize() {
		if(!dougLeaMode) {
			return feed.size();
		}
		return dlFeed.size();
	}
	
	public void print(String update, int ID, int depression, boolean read) {
		if(read) {
			printingModule.printReadRecord(update, ID, depression);
		}else {
			printingModule.printWriteRecord(update, ID, depression);
		}
	}
	
	public boolean dougLeaMode() {
		return dougLeaMode;
	}
}
