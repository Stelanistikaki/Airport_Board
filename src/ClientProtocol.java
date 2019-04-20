import java.io.*;

public class ClientProtocol {

	BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
	private String property;
	
	public String getProperty() throws IOException {
		System.out.print("R FOR READER - W FOR WRITER: ");
		
		while(true) {
			property = user.readLine();
			if(property.equals("R")) {
				break;
			}else if(property.equals("W")) {
				break;
			}else {
				System.out.println("TRY AGAIN. R FOR READER - W FOR WRITER: ");
			}
		}
		//login ok
		System.out.println("LOK");
		
		return property;
	}
	
	public String prepareRequest() throws IOException {
     	String theOutput = user.readLine();
     	while(property.equals("R")) {
     		if(theOutput.contains("READ") || theOutput.equals("CLOSE")) {
         		break;
         	}else if (theOutput.contains("WRITE")){
         		System.out.print("YOU DO NOT HAVE PERMISSION TO WRITE. TRY AGAIN: ");	
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