import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server extends Thread{
	private Socket dataSocket;
	private InputStream is;
   	private BufferedReader in;
	private OutputStream os;
   	private PrintWriter out;
   	private static final String EXIT = "CLOSE";
   	ArrayList<Flight> board;

   	public Server(Socket socket, ArrayList<Flight> aBoard)
   	{
   		    board = aBoard;
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
			while(true) {
				inmsg = in.readLine();
				if(inmsg.equals(EXIT)) {
					outmsg = "CLOSE";
					out.println(outmsg);
					break;
				}
				ServerProtocol app = new ServerProtocol(board);
				System.out.println(inmsg);
				if(inmsg.contains("READ")){
					outmsg = app.processRequestReaders(inmsg.substring(5)); 
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}else if(inmsg.contains("WRITE")) {
					outmsg = app.processRequestWriters(inmsg.substring(6));
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}else {
					outmsg = "NOT VALID. TRY AGAIN: ";
				}
				out.println(outmsg);
			}
			
			dataSocket.close();
			System.out.println("DATA SOCKET CLOSED");

		} catch (IOException e)	{		
	 		System.out.println("I/O Error " + e);
		}
	}	
}			
