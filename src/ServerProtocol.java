import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class ServerProtocol {
	
	ArrayList<Flight> board ;
	String flightNum;
	ReentrantReadWriteLock lock;
	

	//the constructor 
	public ServerProtocol(ArrayList<Flight> aBoard, ReentrantReadWriteLock aLock) {
		board = aBoard;
		lock = aLock;
	}

	//the method for readers
	public String processRequestReaders(String theInput) {
		System.out.println("RECEIVED MESSAGE FROM CLIENT: " + theInput);
		flightNum = theInput;
		String theOutput = "";
		
		
		int i = flightExists(theInput);
		if(i != -1) 
			//return the wanted flight
			theOutput = "ROK " + board.get(i).code + " " + board.get(i).status + " " + board.get(i).time;
		else
			//if the flight does not exist it is an error
			//READ ERROR
			theOutput = "RERR";
		
		return theOutput; 
	}
	
	//the method for writers to write
	public String processRequestWritersWrite(String theInput) {
		System.out.println("RECEIVED MESSAGE FROM CLIENT: " + theInput);
		String theOutput = "";
		String[] stuff;
		
		//splitting in the spaces
		stuff = theInput.split(" ");
		//if there are not 3 parts it is wrong
		if(stuff.length != 3)
			return "WERR";
		//check if the flight already exists
		int i = flightExists(stuff[0]);
		if(i != -1) {
			return "THAT FLIGHT ALREADY EXISTS";
		}
		//else create the new flight
		Flight newFlight = new Flight(stuff[0].toString(), stuff[1].toString(), stuff[2].toString());
		//to modify the flight board it uses a write lock
		lock.writeLock().lock();
		try {
			//adding the new flight in the board
			if(board.add(newFlight))
				//WRITE OK
				theOutput = "WOK";
			else
				//WRITE ERROR
				theOutput = "WERR";
		}finally {
			lock.writeLock().unlock();
		}
		
		return theOutput;
		
		
	}

	//the method for writers to modify a flight
	public String processRequestWritersModify(String theInput) {
		System.out.println("RECEIVED MESSAGE FROM CLIENT: " + theInput);
		String[] stuff;
		Flight newFlight = null;
		
		stuff = theInput.split(" ");
		//if there are not 3 parts it is wrong
		if(stuff.length != 3)
			return "MERR";
		int i = flightExists(stuff[0]);
		//if the flight does not exist it is an error else it creates a new Flight and modify the chosen
		if(i != -1) {
			newFlight = new Flight(stuff[0].toString(), stuff[1].toString(), stuff[2].toString());
			//write lock to access the flight board
			lock.writeLock().lock();
			try {
				board.set(i, newFlight);
			}finally {
				lock.writeLock().unlock();
			}
			//MODIFY OK
			return "MOK";
		}
		else
			//MODIFY ERROR
			return "MERR";
	}
	
	//the method for writers to delete a flight
	public String processRequestWritersDelete(String theInput) {
		System.out.println("RECEIVED MESSAGE FROM CLIENT: " + theInput);
		
		int i = flightExists(theInput);
		if(i != -1) {
			//if the flight exists it uses a write lock and remove the wanted flight from the board
			lock.writeLock().lock();
			try {
				board.remove(i);
			}finally {
				lock.writeLock().unlock();
			}
			//DELETE OK
			return "DOK";
		}
		else
			//DELETE ERROR
			return "DERR";
		
	}
	
	//the method to check if a flight exists in the flight board
	public int flightExists (String flightNum) {
		boolean found = false;
		int i=0;
		//a read lock is used to search in the board
		lock.readLock().lock();
		try {
			while(i<board.size()) {
				if(flightNum.equals(board.get(i).code)){
					found = true;
					break;
				}
				i++;
			}
		}
		finally {
			lock.readLock().unlock();
		}
		//return the position of the wanted object
		if(found)
			return i;
		else
			return -1;
	}
	
}