import java.net.*;
import java.io.*;

public class Client {
	//the host, the port and the exit sign for clients
    private static final String HOST = "localhost";
	private static final int PORT = 1234;
	private static final String EXIT = "CLOSE";
	
	
	public static void main(String args[]) throws IOException {

		//InetAddress address = InetAddress.getByName(HOST);
        Socket dataSocket = new Socket(HOST, PORT);
        
		InputStream is = dataSocket.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		OutputStream os = dataSocket.getOutputStream();
		PrintWriter out = new PrintWriter(os,true);

		String inmsg, outmsg;
		ClientProtocol app = new ClientProtocol();
		
		//define if its a writer or a reader
		
		/*
		 * Writers: a writer can read, write, modify and delete a flight 
		 * Readers: a reader can only read a flight
		 */
		app.getProperty();
		while(true) {
			//the communication continues until the user types "CLOSE"
			outmsg = app.prepareRequest();
			out.println(outmsg);
			inmsg = in.readLine();
			app.processReply(inmsg);  
			//the exit sign
			if(outmsg.equals(EXIT)) {
				break;
			}
		}

		//the connection is closed
		dataSocket.close();
		System.out.println("DATA SOCKET CLOSED");
	}
}			