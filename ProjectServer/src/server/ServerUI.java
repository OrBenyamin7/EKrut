package server;

import java.net.InetAddress;
import java.sql.Connection;
import java.util.ArrayList;

import Boundry.ServerGuiController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import logic.ClientConnected;
import ocsf.server.ConnectionToClient;

public class ServerUI extends Application {
	
	// Class variables *************************************************
	final public static int DEFAULT_PORT = 5555;
	private ServerGuiController serverGuiController;
	private static String host;
	private static InetAddress ip;
	private static EchoServer sv;
	private static ServerUI instance;
	ObservableList<ClientConnected> tableViewList;
	//******************************************************************
	
	
	
	/**
	 * get Host info
	 * 
	 */
	public static String getHost() {
		return host;
	}
	/**
	 * set Host info
	 * @param host
	 */
	public static void setHost(String host) {
		ServerUI.host = host;
	}
	
	
	/**
	 * get ip info
	 * 
	 */
	public static InetAddress getIp() {
		return ip;
	}
	/**
	 * set Host ip
	 * @param ip
	 */
	public static void setIp(InetAddress ip) {
		ServerUI.ip = ip;
	}

		
	
	
	/**
	 * get Instance of serverUI
	 * 
	 */
	
	public static ServerUI getInstance() {
		return instance;
	}

	//Main - launching the program
	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main
	
	
	/**
	 * Start method - getting the primaryStage from Application Class for start 
	 * then - initiate the serverUI instance 
	 * then- Making new instance of ServerGuiController and starting it
	 * then - getting the list from serverGUI to insert data to the GUI table
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		instance = this;
		serverGuiController = new ServerGuiController(); // create StudentFrame
		serverGuiController.start(primaryStage);

		tableViewList = serverGuiController.getList();
		
	}
	
	/**
	 * function that getting parameters from user and open db 
	 * @param password
	 * @param dbName
	 * @param dbUserNameRoot
	 */
	public void dbConnection(String password,String dbName,String dbUserNameRoot) {
		sv.dbStarted(password,dbUserNameRoot,dbName);
		
	}

	
	
	/**
	 * function that getting port from user and starting the server, in addition:
	 * getting from the server the ip and the host for update the table view
	 * @param portFromUser
	 */
	public void runServer(String portFromUser) {
		int port = 0; // Port to listen on
		

		try {
			port = Integer.parseInt(portFromUser); // Set port to 5555

		} catch (Throwable t) {
			
			System.out.println("ERROR - Could not connect!");
		}
		
		sv = new EchoServer(port);
		ip = sv.getIp();
		host = sv.getHost();
		
		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
			
		}
		
	}

}
