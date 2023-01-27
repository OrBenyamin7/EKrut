package gui;
 
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
 
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 * The OrderAcceptedController class is a JavaFX controller class for the 'OrderAccepted.fxml' file.
 * It is responsible for displaying the order details and the pickup code if the order is pickup
 * It also provide the ability to return to the home page of the user and exit the application
 * This page is shown to the user once an order has been successfully placed and accepted.
*/
public class OrderAcceptedController implements Initializable {
 
	@FXML
	private Button backToHomePageBTN;
 
	@FXML
	private Button exitBTN;
 
	@FXML
	private Label pickUpCode;
 
	@FXML
	private AnchorPane anchorPickUp;
 
	@FXML
	private TextArea textAreaOrder;
	private double xoffset;
	private double yoffset;
	private static String Method=null;
	private static String orderCode;
	private static String reservation;
	private static ArrayList<String> arrFromServerRet3;
	private static ArrayList<String> ekOrOLFromConnect;
	/**
	* set the ekOrOLFromConnect from the client
	* @param ekOrOLFromConnect1 ArrayList<String> ekOrOLFromConnect1
	*/
	public static void ekOrOl(ArrayList<String> ekOrOLFromConnect1){
     	ekOrOLFromConnect = ekOrOLFromConnect1;
     }
 
 
	/**
	* get the user data from the client
	* @param arrFromServer ArrayList<String> arrFromServer
	*/
	 public static void getUserData(ArrayList<String> arrFromServer){
	    	arrFromServerRet3 = arrFromServer;
	    }
 
	 /**
	  * Handles the event triggered by the back to home page button.
      * It opens the appropriate home page based on the user role.
	  * @param event ActionEvent event
	  * @throws Exception
	 */
	@FXML
	void pressbackToHomePageBTN(ActionEvent event) throws Exception {
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
     * This method is used to set the Method variable
     * 
     * @param name the method for the order
     */
	public static void setMethodForAccepted(String name) {
		Method = name;
	}
	
    /**
     * This method is used to set the reservation variable
     * 
     * @param name the reservation details
     */
	public static void setResrvation(String name) {
		reservation = name;
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
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ClientUI.chat.accept(msg);
		System.exit(1);
 
	}
	
	/*
	* start method is to load the FXML file, to move the window with the mouse and to show the data in the table view.
	* @param primaryStage the primary stage for this application
	* @throws Exception
	*/
	public void start(Stage primaryStage) throws Exception {
 
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/OrderAccepted.fxml"));
		// event handler for when the mouse is pressed AND dragged to move the window
		root.setOnMousePressed(event1 -> {
			xoffset = event1.getSceneX();
			yoffset = event1.getSceneY();
		});
 
		// event handler for when the mouse is pressed AND dragged to move the window
		root.setOnMouseDragged(event1 -> {
			primaryStage.setX(event1.getScreenX() - xoffset);
			primaryStage.setY(event1.getScreenY() - yoffset);
		});
		//
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
 
	}
	
	/**
	* Sets the order code for the order that was placed.
	* @param massageFromServer The order code for the order that was placed.
	*/
	public static void setOrderCode(String massageFromServer) {		
		orderCode=massageFromServer;
	}
 
	
	@FXML
    private Label OL1;

    @FXML
    private Label OL2;

    @FXML
    private Label OL3;

    @FXML
    private Label ek1;

    @FXML
    private Label ek2;
	    /**
	     * Initializes the OrderAcceptedController class by updating the TextArea with the product and amount,
		 * and sets the visibility of the pick-up code based on the order method.
         * @param location The location used to resolve relative paths for the root object, or
	     * @param resources The resources used to localize the root object, or {@code null} if the root
	     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(Method.equals("EK")) {
			ek1.setVisible(true);
			ek2.setVisible(true);
			OL1.setVisible(false);
			OL2.setVisible(false);
			OL3.setVisible(false);
		}
		else {
			ek1.setVisible(false);
			ek2.setVisible(false);
			OL1.setVisible(true);
			OL2.setVisible(true);
			OL3.setVisible(true);
		}
		String[] res = reservation.split(",");
		String title ="Product\tAmount\n";
		reservation="";
		for(int i = 0;i<res.length;i+=2) {
			reservation = reservation + res[i] + "\t     " + res[i+1] + "\n";
		}
		reservation = " " + reservation.substring(1,reservation.length()-2);
		textAreaOrder.setText(title+reservation);
		if(Method=="PickUp") {
			anchorPickUp.setVisible(true);
			pickUpCode.setText(String.valueOf(orderCode));
		}
		else {
			anchorPickUp.setVisible(false);
		}
	}
}