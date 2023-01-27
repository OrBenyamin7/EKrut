package client;

import gui.ConnectController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClientUI extends Application{
	public static ClientController chat; //only one instance

	public static void main( String args[] ) throws Exception
	   { 
		    launch(args);  
	   } // end main
	 
	
	//Main - launching the client
	@Override
	public void start(Stage primaryStage) throws Exception {
		 ConnectController p = new ConnectController(); // create StudentFrame
		 primaryStage.initStyle(StageStyle.UNDECORATED);
		 p.start(primaryStage);
		  
	}// end main
	
	/**
	 * function that return the chat instance of class ClientController 
	 * 
	 */
	public static ClientController getMyClientchat() {
		return chat;
	}
	
	/**
	 * function that set the chat instance of class ClientController 
	 * 
	 */
	public static void setMyClientchat(ClientController chat1) {
		chat = chat1;
	}

}
