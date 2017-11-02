import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MichaelScottQueue {
	
	private AtomicReference<Node> head;
	private AtomicReference<Node> tail;
	private Node dummy;
	
	private AtomicInteger size;
	
	public MichaelScottQueue() {
		head = new AtomicReference<Node>();
		tail = new AtomicReference<Node>();
		dummy = new Node(null, -1);
		head.set(dummy);
		tail.set(dummy);
		size = new AtomicInteger(0);
	}
	
	public void put(String s, int depressionMeasure) {
		Node toPut = new Node(s, depressionMeasure);
		while(true) {
			Node tempTail = tail.get();
			Node tailNext = tempTail.next.get();
			if(tempTail == tail.get()) {
				if(tailNext == null) {
					if(tempTail.next.compareAndSet(tailNext, toPut)) {
						tail.compareAndSet(tempTail, toPut);
						size.getAndIncrement();
						return;
					}
				}else {
					tail.compareAndSet(tempTail, tailNext);
				}
			}
		}
	}
	
	public Node poll() {
		while(true) {
			Node tempHead = head.get();
			Node tempTail = tail.get();
			Node headNext = tempHead.next.get();
			if(tempHead == head.get()) {
				if(tempHead == tempTail) {
					if(headNext == null) {
						return null;
					}
					tail.compareAndSet(tempTail, headNext);
				}else {
					Node retNode = headNext;
					if(head.compareAndSet(tempHead, headNext)) {
						size.getAndDecrement();
						return retNode;
					}
				}
			}	
		}
	}
	
	public Node[] subQueueToNodeArray(int length) {
		Object o = new Object();
		synchronized(o) {
			int startIndex = size.get() - (length + 1);
			if(size.get() < length) {
				return null;
			}
			Node n = head.get();
			if(n.getUpdate() == null) {
				if(n.next == null) {
					return null;
				}
				n = n.next.get();
			}
			for(int i = 0; i <= startIndex; i++) {
				n = n.next.get();
			}
			Node[] elements = new Node[length];
			for(int i = 0; i < length; i++) {
				elements[i] = n;
				n = n.next.get();
			}
			return elements;
		}
	}
	
	public int size() {
		return size.get();
	}
}
