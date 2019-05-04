import java.io.*;

public class ClientProtocol {

	BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
	private String property;
	
	//the method to define the property
	public void getProperty() throws IOException {
		System.out.print("R FOR READER - W FOR WRITER: ");
		
		//it keeps asking for W or R until it is right
		while(true) {
			property = user.readLine();
			if(property.equals("R") || property.equals("W")) 
				//correct so it exits
				break;
			else 
				System.out.println("TRY AGAIN. R FOR READER - W FOR WRITER: ");
		}
		//LOGIN OK
		System.out.println("LOK");
		
	}
	
	public String prepareRequest() throws IOException {
     	String theOutput = user.readLine();
     	
     	//if the client is a reader he cannot do much more than read. If he is a writer he can do anything
     	while(property.equals("R")) {
     		if(theOutput.contains("READ") || theOutput.equals("CLOSE")) {
         		break;
         	}else if (theOutput.contains("WRITE") || theOutput.contains("MODIFY") || theOutput.contains("DELETE")){
         		System.out.print("YOU DO NOT HAVE PERMISSION TO WRITE, MODIFY OR DELETE. TRY AGAIN: ");	
         		theOutput = user.readLine();
    		}else {
    			System.out.println("NOT VALID. TRY AGAIN: ");
    			theOutput = user.readLine();
    		}
     	}
     	return theOutput; 
    }

	public void processReply(String theInput) throws IOException {
		if(!(theInput.equals("CLOSE")))
			System.out.println(theInput); 
	}
}