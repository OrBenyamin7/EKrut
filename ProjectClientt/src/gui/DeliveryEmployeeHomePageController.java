
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientUI;
import common.MyFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * The DeliveryEmployeeHomePageController class is a controller class for the GUI of a home page for a delivery employee. 
 * It contains several methods and instance variables that handle the functionality of the GUI.
 * The class implements the Initializable interface, which is used to handle the initialization of the GUI elements.
 */
public class DeliveryEmployeeHomePageController implements Initializable {
	
	  /**
     * FXML button to Exit the application.
     */
    @FXML
    private Button ExitBtn;
    
    /**
     * FXML Label to show the name of the user.
     */
    @FXML
    private Label NameOfUserLabel;
    
    /**
     * FXML button to create new order.
     */
    @FXML
    private Button NewOrderBTN;
    
    /**
     * FXML button to view deliveries.
     */
    @FXML
    private Button deliveriesbtn;
    
    /**
     * FXML button to sign out the current user and return to the login screen.
     */
    @FXML
    private Button SignOutbtn;
    
    /**
     * FXML AnchorPane to hold the new order and pick up buttons.
     */
    @FXML
    private AnchorPane newOrderAndPickUpBTNAnchorPane;
    
    /**
     * FXML button to pick up an order.
     */
    @FXML
    private Button pickUpOrderBTN;

	 /*
     * xoffset,yoffset to handle drag of the window.
     */
    private double xoffset;
   	private double yoffset;
   	
   	/**
     * Holds the data about user from the server
     */
   	private static ArrayList<String> arrFromServerRet;
   	
    /**
     * Holds the data about user is online customer or External customer 
     */
   	private static ArrayList<String> ekOrOLFromConnect;
   	
    /**
     * Holds the data about products from the server
     */
   	private static ArrayList<MyFile> arrFromServerRetProductsMyFile;
    
    /**
     * This method sets the data about the products from the server
     *
     * @param arrFromServer  ArrayList that contains the data about the products from the server
     */
    public static void getDataProductsFromDBMyFile(ArrayList<MyFile> arrFromServer) {
		arrFromServerRetProductsMyFile = arrFromServer;
	}
    @FXML
    private Button DeliveryBTN1;

    @FXML
    void PressDeliveryBTN(ActionEvent event) throws Exception {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		DeliveryGetController dgc = new DeliveryGetController();
		DeliveryGetController.ekOrOl(ekOrOLFromConnect);
		DeliveryGetController.getUserData(arrFromServerRet);
		dgc.start(primaryStage);

    }
    
    /**
     * Handles the functionality when the "New Order" button is pressed. 
     * It checks if the user is an Online or External customer, 
     * and if the user is an Online customer it opens the OrderTypeController window, 
     * and if the user is an External customer it opens the EKrutFinalMarketController window.
     *
     * @param event The event that triggers the method, in this case the "New Order" button press.
     * @throws Exception
     */
    @FXML
    void PressNewOrder(ActionEvent event) throws Exception {
    	
    	if(ekOrOLFromConnect.get(0).equals("OL")) {
			Stage primaryStage = new Stage();
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			OrderTypeController otc = new OrderTypeController();
			OrderTypeController.getDataAboutUser(arrFromServerRet);
			
			OrderTypeController.ekOrOl(ekOrOLFromConnect);
			if(arrFromServerRet.get(5).equals("1")) {
				OrderTypeController.setIsAMember(true);
			}else {
				OrderTypeController.setIsAMember(false);
			}
			
			otc.start(primaryStage);	
		}else { //Case Of EK
			ArrayList<String> msg = new ArrayList<>();
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			EKrutFinalMarketController ekfmc = new EKrutFinalMarketController();
			msg.add("getDataForCatalogEK");
			msg.add(ekOrOLFromConnect.get(1));
			ClientUI.chat.accept(msg);
			EKrutFinalMarketController.getProductsFromOrderType(arrFromServerRetProductsMyFile);
			EKrutFinalMarketController.getUserData(arrFromServerRet);
			EKrutFinalMarketController.setMacineName(ekOrOLFromConnect.get(1));
			EKrutFinalMarketController.setArea(ekOrOLFromConnect.get(2));
			EKrutFinalMarketController.ekOrOl(ekOrOLFromConnect);
			if(arrFromServerRet.get(5).equals("1")) {
				EKrutFinalMarketController.setIsAMember(true);
			}else {
				EKrutFinalMarketController.setIsAMember(false);
			}
			ekfmc.start(primaryStage);	
		}
	

    }

    /**
     * Handles the functionality when the "Pick Up Order" button is pressed. 
     * It opens a window that allows the user to pick up an order.
     *
     * @param event The event that triggers the method, in this case the "Pick Up Order" button press.
     */
    @FXML
    void PresspickUpOrderBTN(ActionEvent event) throws Exception {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		PickUpOrderLocalController puolc = new PickUpOrderLocalController();
		PickUpOrderLocalController.getMachineFromMainController(ekOrOLFromConnect.get(1));
		PickUpOrderLocalController.ekOrOl(ekOrOLFromConnect);
		PickUpOrderLocalController.getUserData(arrFromServerRet);
		puolc.start(primaryStage);

    }

    /**
     * Handles the functionality when the "Exit" button is pressed. 
     * It closes the application.
     *
     * @param event The event that triggers the method, in this case the "Exit" button press.
     */
    @FXML
    void pressExitBtn(ActionEvent event) throws IOException {
    	ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		ClientUI.chat.accept(msg);
		System.exit(1);

    }

    /**
     * Handles the functionality when the "Deliveries" button is pressed.
     * It opens a window that shows the deliveries for the current user.
     *
     * @param event The event that triggers the method, in this case the "Deliveries" button press.
     */
    @FXML
    void pressdeliveriesbtn(ActionEvent event) throws Exception {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		DeliveryEmployeeController dec = new DeliveryEmployeeController();
		DeliveryEmployeeController.ekOrOl(ekOrOLFromConnect);
		DeliveryEmployeeController.getUserData(arrFromServerRet);
		dec.start(primaryStage);
    }

   
    /**
     * Handles the functionality when the "Sign Out" button is pressed. 
     * It signs out the current user and return to the login screen.
     *
     * @param event The event that triggers the method, in this case the "Sign Out" button press.
     */
    @FXML
    void pressSignOut(ActionEvent event) throws Exception {
    	ArrayList<String> msg = new ArrayList<>();
    	msg.add("SignOut");
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		ClientUI.chat.accept(msg);
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		LoginScreensController LSC = new LoginScreensController();
    	LSC.start(primaryStage);

    }
    
    /**
         * This method starts the GUI of the Delivery Employee Home Page.
         *
         * @param primaryStage the stage where the GUI will be displayed
         */
    public void start(Stage primaryStage) throws Exception {
    	
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/DeliveryEmployeeHomePage.fxml"));
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
    
    /*
     * Static method to retrieve data from the server for the user.
     * @param arrFromServer an ArrayList of strings containing data from the server
     */
    public static void getUserData(ArrayList<String> arrFromServer){
 		arrFromServerRet = arrFromServer;
 	}
    
	/**
     * Static method to retrieve data from the server for ekOrOl.
     * @param ekOrOLFromConnect1
     */
 	 public static void ekOrOl(ArrayList<String> ekOrOLFromConnect1){
     	ekOrOLFromConnect = ekOrOLFromConnect1;
    }

 	
 	 
 	
 	 /**
      * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
      * It sets the name of the user in the label.
      *
      * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
      * @param resources The resources used to localize the root object, or null if the root object was not localized.
      */	 
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	if(arrFromServerRet.get(4).equals("NotApproved")) {
    		NewOrderBTN.setDisable(true);
    		pickUpOrderBTN.setDisable(true);
    	}
    	if(arrFromServerRet.get(4).equals("Not A Customer")) {
 			newOrderAndPickUpBTNAnchorPane.setVisible(false);
 		}
 		if(ekOrOLFromConnect.get(0).equals("EK")) {
 			deliveriesbtn.setDisable(true);
 			DeliveryBTN1.setDisable(true);
 			
 		}else {//OL
 			pickUpOrderBTN.setDisable(true);
 		}

 		NameOfUserLabel.setText("Welcome back " + arrFromServerRet.get(0));
 		
 		
		
	}

}

