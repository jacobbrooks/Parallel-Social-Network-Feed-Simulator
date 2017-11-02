public class Main {
	public static void main(String[] args) throws InterruptedException {
		Control c = new Control("sentences.txt", 20, false);
		c.execute();
	}
}
