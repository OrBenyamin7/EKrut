package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.control.ToggleGroup;
import client.ChatClient;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
* This class is the controller for the 'ServiceRepresentativeAddNewCustomer' gui.
* The class implements Initializable interface, and have the following methods:
* pressSubmitBTN, pressBackBTN, pressExitBTN, pressGetDetailsBTN, initialize.
*
* @author  Group 3
* @version 1.0
* 
*/
public class ServiceRepresentativeAddNewCustomerController implements Initializable{
	 /**
	    * The FXML annotation is used to mark a class field as the source of a value for a JavaFX node.
	    * The field is also annotated with the @FXML annotation, which is used to indicate that the field is a JavaFX-controlled field.
	    * The 'firstNameTextFild' is a TextField component in the fxml file that the class is a controller for.
	    */
	@FXML
    private TextField firstNameTextFild;

    @FXML
    private TextField userNameTextFild;

    @FXML
    private AnchorPane detailsAnchorPane;

    @FXML
    private Label errorLabel;

    @FXML
    private ToggleGroup group1;

    @FXML
    private TextField lastNameTextFild;

    @FXML
    private RadioButton radioSetAsNewCustomer;

    @FXML
    private RadioButton radioSetAsNewMember;

    @FXML
    private TextField roleTextFild;

    @FXML
    private Button BackBTN;
    
    @FXML
    private Button getDetails;

    @FXML
    private Button submitBTN;
    
    @FXML
    private Button exitBTN;
    /**
     * The xoffset and yoffset are used for the drag and drop functionality of the gui.
     */

    private double xoffset;
	private double yoffset;
	/**
	* The 'arrFromServerRet' is an ArrayList that holds data from the server
	* The 'userNameData' is an ArrayList that holds the username data from the server*/
	
	
	private static ArrayList<String> arrFromServerRet;
	
	private static ArrayList<String> userNameData = new ArrayList<>();
	
	public static void getInsertedNewCustomer(ArrayList<String> arrFromServer){
		arrFromServerRet = arrFromServer;
	}
	
	/**
	* The 'getUserNameData' method is used to set the 'userNameData' ArrayList from the data received from the server.
	*
	* @param massageFromServer - the data received from the server
	*/
	
	public static void getUserNameData(ArrayList<String> massageFromServer) {
		userNameData = massageFromServer;
		
	}



	/**
	* The 'addingDone' method is used to clear the fields and hide certain components after the user is added.
	*/
	   private void addingDone() {
		    detailsAnchorPane.setVisible(false);
			radioSetAsNewCustomer.setVisible(false);
			radioSetAsNewMember.setVisible(false);
			submitBTN.setDisable(true);
			userNameTextFild.setText("");
	   }
	    

	

	   /**
	   * The 'pressSubmitBTN' method is used to handle the press action of the 'submitBTN' button.
	   *
	   * @param event - the event that triggered the method call
	   * @throws IOException - if there is an error with the input/output operations
	   */
    @FXML
    void pressSubmitBTN(ActionEvent event) throws IOException {
    	String action = userNameData.get(3);
    	ArrayList<String> arrToDb = new ArrayList<>();
    	/**
    	* The switch case is used to determine the action to take based on the data received from the server.
    	* The '0' case is for when the user is added as a member.
    	* The 'isNotCustomer' case is for when the user is added as a customer.
    	*/
    	switch(action) {
    		case "0":
    			arrToDb.add("writeCustomerAsAMember");
    			arrToDb.add(userNameTextFild.getText());
    			ClientUI.chat.accept(arrToDb);
    			errorLabel.setText(userNameTextFild.getText() + " Is Now A Member!");
    			addingDone();
    			break;
    		
    			
    		case "isNotCustomer":
    			
    			if(radioSetAsNewCustomer.isSelected()) {
    				arrToDb.add("AddUserAsCustomerOrAMember");
        			arrToDb.add(userNameTextFild.getText());
        			arrToDb.add("AsCustomer");
        			ClientUI.chat.accept(arrToDb);
        			errorLabel.setText(userNameTextFild.getText() + " Is Waiting For Manager Approve!");
        			addingDone();
    			}else {
    				arrToDb.add("AddUserAsCustomerOrAMember");
        			arrToDb.add(userNameTextFild.getText());
        			arrToDb.add("AsMember");
        			ClientUI.chat.accept(arrToDb);
        			errorLabel.setText(userNameTextFild.getText() + " Is Waiting For Manager Approve!");
        			addingDone();
    			}
    			break;
    			
    	}
    	
    }

    
    
    
    
    
    
    
	
	/**
	* The initialize method is called when the controller class is loaded.
	* It is used to initialize any resources that the class uses.
	*
	* @param url - the location used to resolve relative paths for the root object
	* @param rb - the resources used to localize the root object
	*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		submitBTN.setDisable(true);
		detailsAnchorPane.setVisible(false);
		radioSetAsNewCustomer.setVisible(false);
		radioSetAsNewMember.setVisible(false);
	
}
    
	/**
	* The 'pressBackBTN' method is used to handle the press action of the 'BackBTN' button.*
* @param event - the event that triggered the method call
* @throws IOException - if there is an error with the input/output operations
*/
    
    
    
	@FXML
    void pressBackBTN(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		ServiceRepresentativeHomePageController srhpc = new ServiceRepresentativeHomePageController();
		srhpc.start(primaryStage);

    }
	/**
	* The 'pressExitBTN' method is used to handle the press action of the 'exitBTN' button.
	*
	* @param event - the event that triggered the method call
	*/
	@FXML
    void pressexitBTN(ActionEvent event) throws IOException {
    	ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		ClientUI.chat.accept(msg);
		System.exit(1);

    }
	public void start(Stage primaryStage) throws Exception {
    	
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/ServiceRepresentativeAddNewCustomer.fxml"));
		//event handler for when the mouse is pressed AND dragged to move the window
		root.setOnMousePressed(event1 -> {
            xoffset = event1.getSceneX();
            yoffset = event1.getSceneY();
        });

        // event handler for when the mouse is pressed AND dragged to move the window
        root.setOnMouseDragged(event1 -> {
            primaryStage.setX(event1.getScreenX()-xoffset);
            primaryStage.setY(event1.getScreenY()-yoffset);
        });
        //
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	  @FXML
	    void pressradioSetAsNewCustomer(ActionEvent event) {

	    }

	    @FXML
	    void pressradioSetAsNewMember(ActionEvent event) {

	    }
	    /**
	    * The 'pressGetDetailsBTN' method is used to handle the press action of the 'getDetails' button.
	    *
	    * @param event - the event that triggered the method call
	    */
	@FXML
    void preessGetDetailsBTN(ActionEvent event) throws IOException {
	 	if(userNameTextFild.getText().trim().isEmpty()) {
	 		errorLabel.setText("You Have To Insert UserName First");
	 		submitBTN.setDisable(true);
	 		detailsAnchorPane.setVisible(false);
	 	}else {
	 		errorLabel.setText("");
	 		String userNameFromServiceRep = userNameTextFild.getText();
	 		ArrayList<String> msg = new ArrayList<>();
	 		msg.add("getUserNameDetails");
	 		msg.add(userNameFromServiceRep);
	 		ClientUI.chat.accept(msg);
	 		if(userNameData.get(0).equals("notExsist")) {
	 			errorLabel.setText("User Not Exsist");
	 			detailsAnchorPane.setVisible(false);
	 			submitBTN.setDisable(true);
	 		}else if(userNameData.size()>4 ) {
	 			if(userNameData.get(4).equals("NotApproved")) {
	 				errorLabel.setText("User Not Approved");
		 			detailsAnchorPane.setVisible(false);
		 			radioSetAsNewCustomer.setVisible(false);
	 				radioSetAsNewMember.setVisible(false);
		 			submitBTN.setDisable(true);
	 			}else {
	 			
	    			errorLabel.setText(userNameTextFild.getText() + " Is Already A Member!");
	    			
	    			 
	 			}
	 			
	 		}
	 		else {
	 			String action = userNameData.get(3);
		 		switch(action) {
		 			case "isNotCustomer":
		 				detailsAnchorPane.setVisible(true);
		 				firstNameTextFild.setText(userNameData.get(0));
		 				lastNameTextFild.setText(userNameData.get(1));
		 				roleTextFild.setText(userNameData.get(2));
		 				radioSetAsNewCustomer.setVisible(true);
		 				radioSetAsNewMember.setVisible(true);
		 				errorLabel.setText("");
		 				submitBTN.setDisable(false);
		 				break;
		 				
		 			case "0":
		 				detailsAnchorPane.setVisible(true);
		 				firstNameTextFild.setText(userNameData.get(0));
		 				lastNameTextFild.setText(userNameData.get(1));
		 				roleTextFild.setText(userNameData.get(2));
		 				radioSetAsNewCustomer.setVisible(false);
		 				radioSetAsNewMember.setVisible(true);
		 				errorLabel.setText("");
		 				submitBTN.setDisable(false);
		 				break;
		 				
		 			case "1":
		 				detailsAnchorPane.setVisible(true);
		 				firstNameTextFild.setText(userNameData.get(0));
		 				lastNameTextFild.setText(userNameData.get(1));
		 				roleTextFild.setText(userNameData.get(2));
		 				errorLabel.setText("The UserName is Already A Member");
		 				radioSetAsNewCustomer.setVisible(false);
		 				radioSetAsNewMember.setVisible(false);
		 				submitBTN.setDisable(true);
		 				break;
		 		}
	 			
	 		}
	 		
	 		
	 	}
    }

	
	
	


}
