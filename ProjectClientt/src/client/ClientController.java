package client;

import java.io.IOException;
import java.util.ArrayList;


import ocsf.client.ObservableClient;

public class ClientController extends ObservableClient implements ChatIF{
	// Class variables *************************************************
	public static int DEFAULT_PORT ;
	ChatClient client;
	static boolean isConnected = false;
	//******************************************************************
	
	//Constructor ********
	 public ClientController(String host, int port) 
	  {
		 super(host, port);
	    try 
	    {
	        client = new ChatClient(host, port, this);
	    } 
	    catch(IOException exception) 
	    {
	      System.out.println("Error: Can't setup connection! Terminating client.");
	      System.exit(1);
	    }
	  }
	 
	 /**
	  * funnction that connect the gui controllers to the chatcliet
	  * 
	 */
	 public void accept(ArrayList<String> str) throws IOException 
	  {
		  client.handleMessageFromClientUI(str);
	  }
	 
	 public void display(String message) 
	  {
	    System.out.println("> " + message);
	  }
	 
	

}
