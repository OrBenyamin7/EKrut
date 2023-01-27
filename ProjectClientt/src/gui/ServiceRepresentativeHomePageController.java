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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.GetMessages;
/**
 * The ServiceRepresentativeHomePageController class is the controller class for the ServiceRepresentativeHomePage.fxml file.
 * It provides the functionality for the buttons and other components in the GUI.
 * 
 */
public class ServiceRepresentativeHomePageController implements Initializable  {
 
	/**
     * Declaring the components from the FXML file as class variables.
     */
    @FXML
    private Button AddNewCustomer;
 
    @FXML
    private Button ExitBtn;
 
    @FXML
    private Label NameOfUserLabel;
 
    @FXML
    private Button SignOutbtn;
    /**
     * Declaring the variables used for storing data received from the server
     */
    private double xoffset;
	private double yoffset;
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> ekOrOLFromConnect;
	private static ArrayList<MyFile> arrFromServerRetProductsMyFile;
	
	
	 @FXML
	    private Button NewOrderBTN;
	 @FXML
	    private AnchorPane newOrderAndPickUpBTNAnchorPane;
 
	    @FXML
	    private Button pickUpOrderBTN;
	    
		@FXML
	    private Button DeliveryBTN1;
		/**
	     * The PressDeliveryBTN method is used to handle the press action of the 'DeliveryBTN1' button.
	     * 
	     * @param event - the event that triggeredthe method call
* @throws Exception - if there is an error with the input/output operations or other exception is thrown
*/

		// code to handle the press action of the 'DeliveryBTN1' button
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
	   	 * The getDataProductsFromDBMyFile method is used to set the value of the 'arrFromServerRetProductsMyFile' variable.
	   	 * 
	   	 * @param arrFromServer - the ArrayList of MyFile objects received from the server
	   	 */
	    public static void getDataProductsFromDBMyFile(ArrayList<MyFile> arrFromServer) {
			arrFromServerRetProductsMyFile = arrFromServer;
		}
		/**
	   	 * The getUserData method is used to set the value of the 'arrFromServerRet' variable.
	   	 * 
	   	 * @param arrFromServer - the ArrayList of String objects received from the server
	   	 */
	    public static void getUserData(ArrayList<String> arrFromServer){
	 		arrFromServerRet = arrFromServer;
	 	}
		/**
	   	 * The ekOrOl method is used to set the value of the 'ekOrOLFromConnect' variable.
	   	 * 
	   	 * @param ekOrOLFromConnect1 - the ArrayList of String objects received from the server
	   	 */
	 	 public static void ekOrOl(ArrayList<String> ekOrOLFromConnect1){
	     	ekOrOLFromConnect = ekOrOLFromConnect1;
	     }
 
 
	    /**
	     * The PressNewOrder method is used to handle the press action of the 'NewOrderBTN' button.
	     * 
	     * @param event - the event that triggered the method call
	     * @throws Exception - if there is an error with the input/output operations or other exception is thrown
	     */
	    @FXML
	    void PressNewOrder(ActionEvent event) throws Exception {
	    	// code to handle the press action of the 'NewOrderBTN' butto
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
	     * The PressPickUpOrder method is used to handle the press action of the 'pickUpOrderBTN' button.
	     * 
	     * @param event - the event that triggered the method call
	     * @throws Exception - if there is an error with the input/output operations or other exception is thrown
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
	     * The pressAddNewCustomer method is used to handle the press action of the 'addNewCustomre' button.
	     * 
	     * @param event - the event that triggered the method call
	     * @throws Exception - if there is an error with the input/output operations or other exception is thrown
	     */
 
    @FXML
    void pressAddNewCustomer(ActionEvent event) throws Exception {
    	Stage primaryStage = new Stage();
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		primaryStage.initStyle(StageStyle.UNDECORATED);
		ServiceRepresentativeAddNewCustomerController sracc = new ServiceRepresentativeAddNewCustomerController();
		sracc.start(primaryStage);
    }
 
    /**
	* The 'pressExitBTN' method is used to handle the press action of the 'exitBTN' button.
	*
	* @param event - the event that triggered the method call
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
	* The 'pressSignOut' method is used to handle the press action of the 'signOut' button.*
* @param event - the event that triggered the method call
* @throws IOException - if there is an error with the input/output operations
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
 
 public void start(Stage primaryStage) throws Exception {
 
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/ServicrRepresentativeHomePage.fxml"));
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
 
 
 	
 
 	/**
 	 * The initialize method is called when the controller class is loaded.
 	 * It is used to initialize any resources that the class uses.
 	 * 
 	 * @param url - the location used to resolve relative paths for the root object
 	 * @param rb - the resources used to localize the root object
 	 */
 
 	@Override
	public void initialize(URL location, ResourceBundle resources) {
 		GetMessages checkMsg;
		try {
			checkMsg = new GetMessages(arrFromServerRet.get(7));
			ArrayList<String> msg = checkMsg.getMsg();
        	if(msg.size()>0) {
        		String readyMsg = "";
        		for(String msgs : msg) {
        			readyMsg += msgs + "\n";
        		}
        		Alert alert = new Alert(Alert.AlertType.INFORMATION);
			    alert.setTitle("Message !");
				alert.setHeaderText(readyMsg);
				alert.showAndWait();
				msg.clear();
        	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
 		
 		
 		if(arrFromServerRet.get(4).equals("Not A Customer")) {
 			newOrderAndPickUpBTNAnchorPane.setVisible(false);
 		}
 		if(ekOrOLFromConnect.get(0).equals("EK")) {
 			AddNewCustomer.setDisable(true);
 			DeliveryBTN1.setDisable(true);
 		}else {
 			pickUpOrderBTN.setDisable(true);
 			
 		}
		ArrayList<String> msg1 = new ArrayList<>();
		msg1.add("getUserDataServiceRep");
 	try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
 
 		NameOfUserLabel.setText("Welcome back " + arrFromServerRet.get(0));
 
 
 
	}
 
 
}