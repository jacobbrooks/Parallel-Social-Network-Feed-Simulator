
public class PrintingModule {
	
	public synchronized void printReadRecord(String update, int ID, int depression) {
		System.out.println("**********************************************");
		System.out.println("USER " + ID + " READS: " + update);
		System.out.println("USER " + ID + " DEPRESSION: " + depression);
		System.out.println("**********************************************");
	}
	
	public synchronized void printWriteRecord(String update, int ID, int depression) {
		System.out.println("\n+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
		System.out.println("USER " + ID + " WRITES: " + update);
		System.out.println("USER " + ID + " DEPRESSION: " + depression);
		System.out.println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+\n");
	}

}
