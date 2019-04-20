import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ServerProtocol {
	
	ArrayList<Flight> board ;
	String flightNum;
	

	public ServerProtocol(ArrayList<Flight> aBoard) {
		board = aBoard;
	}

	public String processRequestReaders(String theInput) {
		System.out.println("RECEIVED MESSAGE FROM CLIENT: " + theInput);
		flightNum = theInput;
		String theOutput = "";
		
		MultiThreadedServer.lock.readLock().lock();
		try {
			for(int i=0; i<board.size(); i++) {
				if(flightNum.equals(board.get(i).code)){
					theOutput = "ROK " + board.get(i).code + " " + board.get(i).status + " " + board.get(i).time;
				}
			}
		}finally {
			MultiThreadedServer.lock.readLock().unlock();
		}
		
		if(theOutput.equals("")) {
			theOutput = "RERR";
		}
		
		return theOutput; 
		
	}
	
	public String processRequestWriters(String theInput) {
		System.out.println("RECEIVED MESSAGE FROM CLIENT: " + theInput);
		String theOutput = "";
		String code="", status="", time="";
		
		int i=0;
		while(theInput.charAt(i) != ' ') {
			code += theInput.charAt(i);
			i++;
		}
		i++;
		while(theInput.charAt(i) != ' ') {
			status += theInput.charAt(i);
			i++;
		}
		i++;
		while(i < theInput.length()) {					
			time += theInput.charAt(i);
			i++;
		}
		
		Flight newFlight = new Flight(code, status, time);
		MultiThreadedServer.lock.writeLock().lock();
		try {
			if(board.add(newFlight))
				theOutput = "WOK";
			else
				theOutput = "WERR";
		}finally {
			MultiThreadedServer.lock.writeLock().unlock();
		}
		
		return theOutput;
		
		
	}
	
}