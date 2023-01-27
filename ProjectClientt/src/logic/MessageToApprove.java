package logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * Class for messages for approve for area manager
 * @author Or
 *
 */
public class MessageToApprove {
	
	private SimpleStringProperty number;
	private SimpleStringProperty message;
	private ComboBox status;
	 
	/**
	 * Constructor
	 * @param number
	 * @param message
	 * @param data
	 */
	 public MessageToApprove(String number, String message, ObservableList data) {
	        this.number = new SimpleStringProperty(number);
	        this.message = new SimpleStringProperty(message);
	        this.status = new ComboBox(data);//(status);
	        
	        status.setValue("in process");
	    }
	 
	 
	 /**
	  * Getter for number filed
	  * @return number filed
	  */
	 public String getNumber() {
	        return number.get();
	    }

	 /**
	  * Setter for number filed
	  * @param number
	  */
	    public void setNumber(String number) {
	        this.number = new SimpleStringProperty(number);
	    }

	    /**
	     * Getter for message filed
	     * @return message filed
	     */
	    public String getMessage() {
	        return message.get();
	    }

	    /**
	     * Setter for message filed
	     * @param message
	     */
	    public void setMessage(String message) {
	        this.message = new SimpleStringProperty(message);
	    }

	    /**
	     * Getter for status comboBox filed
	     * @return status comboBox filed
	     */
	    public ComboBox getStatus() {
	        //return status.get();
	    	return status;
	    }

	    /**
	     * Setter for status comboBox filed
	     * @param combo
	     */
	    public void setStatus(ComboBox combo) {
	        //this.status = new ComboBox(status);
	    	this.status = combo;
	    }

}
