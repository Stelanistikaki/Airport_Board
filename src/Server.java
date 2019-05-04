import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.*;

public class Server extends Thread{
	private Socket dataSocket;
	private InputStream is;
   	private BufferedReader in;
	private OutputStream os;
   	private PrintWriter out;
   	private static final String EXIT = "CLOSE";
   	//the common board and lock
    private ArrayList<Flight> board;
    ReentrantReadWriteLock lock;

    //the constructor
   	public Server(Socket socket, ArrayList<Flight> aBoard, ReentrantReadWriteLock aLock)
   	{
   		    board = aBoard;
   		    lock = aLock;
      		dataSocket = socket;
      		try {
			is = dataSocket.getInputStream();
			in = new BufferedReader(new InputStreamReader(is));
			os = dataSocket.getOutputStream();
			out = new PrintWriter(os,true);
		} 
		catch (IOException e)	{		
	 		System.out.println("I/O Error " + e);
      		}
    	}

	public void run()
	{
   		String inmsg, outmsg;
		try {
			//while the message is not "CLOSE"
			while(true) {
				inmsg = in.readLine();
				if(inmsg.equals(EXIT)) {
					outmsg = "CLOSE";
					out.println(outmsg);
					break;
				}
				ServerProtocol app = new ServerProtocol(board, lock);
				System.out.println(inmsg);
				//the client wants to read a flight
				switch(getAction(inmsg)) {
					case 1: 
						outmsg = app.processRequestReaders(inmsg.substring(5)); 
						try {
							//the reading part is less slow than the others
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						break;
					case 2:
						outmsg = app.processRequestWritersWrite(inmsg.substring(6));
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						break;
					case 3:
						outmsg = app.processRequestWritersModify(inmsg.substring(7));
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						break;
					case 4:
						outmsg = app.processRequestWritersDelete(inmsg.substring(7));
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						break;
					default:
						//none of the above so it's wrong
						outmsg = "NOT VALID. TRY AGAIN: ";
						break;
						
				}
				out.println(outmsg);
			}
			//the connection is closed
			dataSocket.close();
			System.out.println("DATA SOCKET CLOSED");

		} catch (IOException e)	{		
	 		System.out.println("I/O Error " + e);
		}
	}
	
	
	//a method to define what the writer wants to do and call the proper method
	public int getAction(String inmsg) {
		if(inmsg.contains("READ"))
			return 1;
		else if(inmsg.contains("WRITE"))
			return 2;
		else if(inmsg.contains("MODIFY"))
			return 3;
		else if(inmsg.contains("DELETE"))
			return 4;
		else
			return 0;
	}
}			
