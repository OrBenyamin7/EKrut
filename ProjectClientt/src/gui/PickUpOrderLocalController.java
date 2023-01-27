package gui;
 
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
 
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
 
/**
 * The PickUpOrderLocalController class is used to handle the functionality for 
 * the pick-up order feature of the application.
 * It allows the user to enter an order code and submit a request for pick-up.
 * 
 */
public class PickUpOrderLocalController{
 
	 /**
     * EnterOrderCode TextField is used to enter the OrderCode of the order
     */
    @FXML
    private TextField EnterOrderCode;
 
    /**
     * SignOutbtn111 button is used to SignOut the the user
     */
    @FXML
    private Button SignOutbtn111;
 
    /**
     * SubmitBTN button is used to submit the request for pickup 
     */
    @FXML
    private Button SubmitBTN;
 
    @FXML
    private Label errorLabel;
    @FXML
    private Button exitBTN;
    private double xoffset;
	private double yoffset;
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> arrFromServerRet3;
	private static ArrayList<String> ekOrOLFromConnect;
	private static String machine;
 
    /*
     * Static method to retrieve data of machine from the main controller of the order from the server for the user.
     * @param machine1 string containing data of machine in order
     */
	 public static void getMachineFromMainController(String machine1){
		 machine = machine1;
	    }
 
		/**
		* Static method to retrieve data about pickUP from the server for the user.
		* @param arrFromServer an ArrayList of strings containing data from the server
		*/
	public static void getDataFromDbPickUp(ArrayList<String> arrFromServer){
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
     * Static method to retrieve data from the server for the user.
     * @param arrFromServer an ArrayList of strings containing data from the server
     */
	 public static void getUserData(ArrayList<String> arrFromServer){
	    	arrFromServerRet3 = arrFromServer;
	    }
	 
	 /**
	  * Handles the pressSignOut button action. When the button is pressed, the current window is closed and the appropriate
	  *	home page for the user's role is opened.
	  * @param event the event that triggers the button press
	  * @throws Exception the event that triggers the button press
	  */
    @FXML
    void pressSignOut(ActionEvent event) throws Exception {
 
    	Stage primaryStage = new Stage();
		String role = arrFromServerRet3.get(2);
		switch(role) {
		
		case "Customer":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			CustomerMainScreenController cmc = new CustomerMainScreenController();
			CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
			CustomerMainScreenController.getUserData(arrFromServerRet3);
			cmc.start(primaryStage);	
			break;
		case "AreaManager":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			AreaManagerHomePageController amgpc = new AreaManagerHomePageController();
			AreaManagerHomePageController.detailsOnUser(arrFromServerRet3);
			AreaManagerHomePageController.ekOrOl(ekOrOLFromConnect);

			amgpc.start(primaryStage);
			break;
		case "ServiceRepresentative":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			ServiceRepresentativeHomePageController srhpc = new ServiceRepresentativeHomePageController();
			ServiceRepresentativeHomePageController.getUserData(arrFromServerRet3);
			ServiceRepresentativeHomePageController.ekOrOl(ekOrOLFromConnect);
			srhpc.start(primaryStage);
			break;
		case "User":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			CustomerMainScreenController cmcu = new CustomerMainScreenController();
			CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
			CustomerMainScreenController.getUserData(arrFromServerRet3);
			cmcu.start(primaryStage);	
			break;
		case "MarketingManager":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			MarketingManagerHomePageController mmhpc = new MarketingManagerHomePageController();
			MarketingManagerHomePageController.getUserData(arrFromServerRet3);
			MarketingManagerHomePageController.ekOrOl(ekOrOLFromConnect);
			mmhpc.start(primaryStage);
			break;

		case "OperationWorker":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			OperationWorkerHomePageController owhpc = new OperationWorkerHomePageController();
			OperationWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
			OperationWorkerHomePageController.detailsOnUser(arrFromServerRet3);
			owhpc.start(primaryStage);
			break;
			
			case "MarketingAreaWorker":
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				primaryStage.initStyle(StageStyle.UNDECORATED);
				MarketingAreaWorkerHomePageController mawhpc = new MarketingAreaWorkerHomePageController();
				MarketingAreaWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
				MarketingAreaWorkerHomePageController.getUserData(arrFromServerRet3);
				
				mawhpc.start(primaryStage);
				break;
			case "DeliveryEmployee":
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				primaryStage.initStyle(StageStyle.UNDECORATED);
				DeliveryEmployeeHomePageController dehpc = new DeliveryEmployeeHomePageController();//new conroller
				DeliveryEmployeeHomePageController.ekOrOl(ekOrOLFromConnect);//transfer Ek Or Ol TO delivery employe
				DeliveryEmployeeHomePageController.getUserData(arrFromServerRet3);//Details about user
				dehpc.start(primaryStage);
				break;
			case "Ceo":
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				primaryStage.initStyle(StageStyle.UNDECORATED);
				CeoHomePageController chpc = new CeoHomePageController();
				CeoHomePageController.detailsOnUser(arrFromServerRet3);
				CeoHomePageController.ekOrOl(ekOrOLFromConnect);
					
				chpc.start(primaryStage);
				break;
 
 
			default:
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				primaryStage = new Stage();
				primaryStage.initStyle(StageStyle.UNDECORATED);
				CustomerMainScreenController cmsc = new CustomerMainScreenController();
				CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
				cmsc.start(primaryStage);
				break;
		}
 
 
 
 
 
    }
    
    /**
     * Handles the functionality when the "Exit" button is pressed. 
     * It closes the application.
     *
     * @param event The event that triggers the method, in this case the "Exit" button press.
     */
    @FXML
    void pressexitBTN(ActionEvent event) throws IOException {
    	ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		ClientUI.chat.accept(msg);
		System.exit(1);
 
    }
 
    
    /**
     * Handles the press of the sign out button. 
     * Hides the current window and opens the appropriate home page based on the user's role.
     *
     * @param event the event that triggers this method.
     * @throws Exception if an error occurs while loading the home page.
     */
    @FXML
    void pressSubmitBTN(ActionEvent event) throws Exception {
    	String orderCode = EnterOrderCode.getText();
 
    	if((orderCode == null )) {
    		errorLabel.setText("No Order Code Pressed");
    	}else {
    		ArrayList<String> msg = new ArrayList<>();
    		msg.add("getPickUpOrder");
    		msg.add(orderCode);
    		msg.add(machine);
    		ClientUI.chat.accept(msg);
    		if(arrFromServerRet.get(0).equals("Error")) {
    			errorLabel.setText("Order Not Found , Try Again");
    		}else {
    			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
    			Stage primaryStage = new Stage();
    			primaryStage.initStyle(StageStyle.UNDECORATED);
    			OrderPickedController opc = new OrderPickedController();
    			OrderPickedController.getOrderFromPickUpConroller(arrFromServerRet);
    			OrderPickedController.ekOrOl(ekOrOLFromConnect);
    			OrderPickedController.getUserData(arrFromServerRet3);
    			opc.start(primaryStage);
 
    		}
    	}
   	}
 
 
	/*
	* start method is to load the FXML file, to move the window with the mouse and to show the data in the table view.
	* @param primaryStage the primary stage for this application
	* @throws Exception
	*/
public void start(Stage primaryStage) throws Exception {
 
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/PickUPOrderScreen.fxml"));
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
 
 
 
 
}