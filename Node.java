import java.util.concurrent.atomic.AtomicReference;

public class Node {
	
	private volatile String update;
	private volatile int depressionMeasure;
	public AtomicReference<Node> next;
	
	public Node(String update, int depressionMeasure) {
		this.update = update;
		this.depressionMeasure = depressionMeasure;
		next = new AtomicReference<Node>(null);
	}
	
	public void setUpdate(String update) {
		this.update = update;
	}
	
	public String getUpdate() {
		return update;
	}
	
	public int getDepressionMeasure() {
		return depressionMeasure;
	}

}
