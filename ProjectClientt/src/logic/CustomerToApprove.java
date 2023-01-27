package logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * Class that represent Customer to approve for area manager screen
 * @author Or
 *
 */
public class CustomerToApprove {
	 private SimpleStringProperty number;
	 private SimpleStringProperty userName;
	 private ComboBox status;
	 
	 /**
	  * Constructor for class
	  * @param number
	  * @param userName
	  * @param data
	  */
	 public CustomerToApprove(String number, String userName, ObservableList data) {
	        this.number = new SimpleStringProperty(number);
	        this.userName = new SimpleStringProperty(userName);
	        this.status = new ComboBox(data);//(status);
	        
	        status.setValue("Not Approve");
	    }

	 /**
	  * Getter for number filed
	  * @return the number filed
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
	     * Getter for user name
	     * @return userName
	     */
	    public String getUserName() {
	        return userName.get();
	    }

	    /**
	     * Setter for userName
	     * @param userName
	     */
	    public void setUserName(String userName) {
	        this.userName = new SimpleStringProperty(userName);
	    }

	    /**
	     * Getter for comboBox
	     * @return the status comboBox
	     */
	    public ComboBox getStatus() {
	        //return status.get();
	    	return status;
	    }

	    /**
	     * Setter for status comboBox
	     * @param combo
	     */
	    public void setStatus(ComboBox combo) {
	        //this.status = new ComboBox(status);
	    	this.status = combo;
	    }
	
}