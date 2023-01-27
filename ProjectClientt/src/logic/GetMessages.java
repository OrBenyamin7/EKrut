package logic;

import java.io.IOException;
import java.util.ArrayList;

import client.ClientUI;

/**
 * This class represents a simple message retrieval system.
 * It contains methods for getting and setting messages, as well as
 * a constructor for initializing a new instance of the class with a user name.
 */
public class GetMessages {
	private String userName;
	private static ArrayList<String> msg = new ArrayList<>();
	
	
    /**
     * Returns the current list of messages.
     * @return An ArrayList containing the current messages.
     */
	public ArrayList<String> getMsg() {
		return msg;
	}

    /**
     * Sets the current list of messages.
     * @param msg An ArrayList containing the new messages.
     */
	public static void setMsg(ArrayList<String> msg) {
		GetMessages.msg = msg;
	}

    /**
     * Constructor for creating a new instance of the class.
     * @param userName The user name to be associated with this instance.
     * @throws IOException if there is an error communicating with the database.
     */
	public GetMessages(String userName) throws IOException {
		this.userName = userName;
		getMessages();
	}
	
    /**
     * Retrieves a list of messages from a database and adds them to the current list of messages.
     * @param msg1 An ArrayList of messages to be retrieved from the database.
     */
	public static void getMessageDromDb(ArrayList<String> msg1) {
		msg.addAll(msg1);
	}
	
    /**
     * Retrieves a list of messages from a database and returns it.
     * @return An ArrayList containing the retrieved messages.
     * @throws IOException if there is an error communicating with the database.
     */
	public ArrayList<String> getMessages() throws IOException{
		ArrayList<String> msgToDb = new ArrayList<>();
		msgToDb.add("GetMessagesForAllCustomers");
		msgToDb.add(userName);
		ClientUI.chat.accept(msgToDb);
		return msg;
	}
}
