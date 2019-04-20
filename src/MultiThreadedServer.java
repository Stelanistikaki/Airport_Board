import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.*;

public class MultiThreadedServer {
	private static final int PORT = 1234;
	public static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	public static void main(String args[]) throws IOException {

		ServerSocket connectionSocket = new ServerSocket(PORT);
		ArrayList<Flight> flightsBoard = new ArrayList<Flight>();
		
		Flight flight1 = new Flight("XY123", "ARRIVE", "12:00");
		Flight flight2 = new Flight("ST666", "DEPART", "07:55");
		Flight flight3 = new Flight("ZZ456", "ARRIVE", "11:23");
		
		flightsBoard.add(flight1);
		flightsBoard.add(flight2);
		flightsBoard.add(flight3); 
		
		while (true) {	
			System.out.println("Server is listening to port: " + PORT);
			Socket dataSocket = connectionSocket.accept();
		    System.out.println("Received request from " + dataSocket.getInetAddress());
			Server sthread = new Server(dataSocket, flightsBoard);
			sthread.start(); 
		}
	}
}