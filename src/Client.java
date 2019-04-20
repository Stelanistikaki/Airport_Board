import java.net.*;
import java.io.*;

public class Client {
        private static final String HOST = "localhost";
	private static final int PORT = 1234;
	private static final String EXIT = "CLOSE";
	private static String property;

	
	public static void main(String args[]) throws IOException {

		//InetAddress address = InetAddress.getByName(HOST);
        Socket dataSocket = new Socket(HOST, PORT);
        	
		InputStream is = dataSocket.getInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		OutputStream os = dataSocket.getOutputStream();
		PrintWriter out = new PrintWriter(os,true);

		String inmsg, outmsg;
		ClientProtocol app = new ClientProtocol();
		
		property = app.getProperty();
		while(true) {
			outmsg = app.prepareRequest();
			out.println(outmsg);
			inmsg = in.readLine();
			app.processReply(inmsg);  
			
			if(outmsg.equals(EXIT)) {
				break;
			}
		}

		dataSocket.close();
		System.out.println("DATA SOCKET CLOSED");
	}
}			