public class SecretPopper implements Runnable{
	
	private Control c;
	
	public SecretPopper(Control c) {
		this.c = c;
	}
	
	public void run() {
		while(true) {
			if(c.feedSize() >= 50) {
				c.popFeed(20);
			}
		}
	}
}
