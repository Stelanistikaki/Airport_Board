import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.*;

public class MultiThreadedServer {
	//the port that the server listens to
	private static final int PORT = 1234;
	
	public static void main(String args[]) throws IOException {

		ServerSocket connectionSocket = new ServerSocket(PORT);
		//the arraylist with the flights
		ArrayList<Flight> flightsBoard = new ArrayList<Flight>();
		//the readwrite lock is the same for the threads
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		
		//some dummy flights
		Flight flight1 = new Flight("XY123", "ARRIVE", "12:00");
		Flight flight2 = new Flight("ST666", "DEPART", "07:55");
		Flight flight3 = new Flight("ZZ456", "ARRIVE", "11:23");
		
		flightsBoard.add(flight1);
		flightsBoard.add(flight2);
		flightsBoard.add(flight3); 
		
		while (true) {	
			System.out.println("Server is listening to port: " + PORT);
			//connection established
			Socket dataSocket = connectionSocket.accept();
		    System.out.println("Received request from " + dataSocket.getInetAddress());
		    //the flightsBoard and the lock has to be sent
			Server sthread = new Server(dataSocket, flightsBoard, lock);
			sthread.start(); 
		}
	}
}