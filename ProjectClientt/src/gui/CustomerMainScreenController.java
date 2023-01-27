package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientController;
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
 * The Controller class for the customer main screen. Handles the behavior of the customer main screen,
 * including handling button clicks, updating labels and handling draggable window feature.
 */
public class CustomerMainScreenController implements Initializable {
	
	@FXML
    private Label WelcomeLabel;
	
    @FXML
    private Button NewOrderBTN;

    @FXML
    private Button DeliveryBTN1;
    
    @FXML
    private Button SignOutbtn;

    @FXML
    private Label UserStatus;
    @FXML
    private Button pickUpOrderBTN;

    @FXML
    private Button exitBTN;
    private double xoffset;
	private double yoffset;
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> ekOrOLFromConnect;
	private static ArrayList<MyFile> arrFromServerRetProductsMyFile;///!!!!!!///

    
	/**
     * Handles the press of the New Order button.
     * If the user is in OL Customer Main Screen, opens the Online Store New Order page.
     * If the user is in EK Customer Main Screen, opens the Create New Order page.
     *
     * @param event the press of the New Order button
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
			EKrutFinalMarketController.setMacineName(ekOrOLFromConnect.get(1));
			EKrutFinalMarketController.setArea(ekOrOLFromConnect.get(2));
			EKrutFinalMarketController.getUserData(arrFromServerRet);
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
   	Handles the press of the Delivery button.
   	Hides the current window and opens the Delivery page.
   	@param event the press of the Delivery button
   	@throws Exception
   	*/
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
	   * Receives the product data from the DB in case he press create Button.
	   *
	   * @param arrFromServer an ArrayList containing the product data as MyFile objects (In case That The 
	   * manager is also a customer , and hw want to make new order
	   */
	public static void getDataProductsFromDBMyFile(ArrayList<MyFile> arrFromServer) {
		arrFromServerRetProductsMyFile = arrFromServer;
	}
	
	/**
     * Receives the EK or OL status from the previous controller.
     *
     * @param ekOrOLFromConnect1 an ArrayList containing the EK or OL status
     */
	public static void ekOrOl(ArrayList<String> ekOrOLFromConnect1){
    	ekOrOLFromConnect = ekOrOLFromConnect1;
    }
	
	/**
   	Receives the user data from the previous controller.
   	@param arrFromServer an ArrayList containing the user data
   	*/
	public static void getUserData(ArrayList<String> arrFromServer){
    	arrFromServerRet = arrFromServer;
    }
	
	/**
     * Event handler for the Exit button press. Closes the program.
     *
     * @param event the action event that triggered this handler
     */
    @FXML
    void pressExitBTN(ActionEvent event) throws IOException {
    	ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		ClientUI.chat.accept(msg);
		System.exit(1);
    }
    
    /**
     * Handles the press of the Pick Up Order button.
     * Opens the Pick Up Order page.
     *
     * @param event the press of the Pick Up Order button
     * @throws Exception
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
     * Handles the press of the Sign Out button.
     * Hides the current window and opens the Sign In page.
     *
     * @param event the press of the Sign Out button
     * @throws Exception
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
		LoginScreensController.ekOrOl(ekOrOLFromConnect);
		
    	LSC.start(primaryStage);
    	
    }
    
    
    /**
     * Opens the Customer Main Screen GUI.
     *
     * @param primaryStage the Stage to be shown
     * @throws Exception
     */
    public void start(Stage primaryStage) throws Exception {
    	
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/CustomerMainScreen.fxml"));
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
	 * Initializes the controller class. Initializes the Name Of User label with the
	 * user's name.
	 *
	 * @param url the URL of the FXML file
	 * @param rb  the ResourceBundle of the FXML file
	 */
	
    
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<String> msg1 = new ArrayList<>();
		msg1.add("getUserDataCustomer");
    	try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	String isCustomer = arrFromServerRet.get(4);
    	switch(isCustomer) {
    	case "Not A Customer":
    		UserStatus.setText("Not Customer :(");
    		NewOrderBTN.setDisable(true);
    		pickUpOrderBTN.setDisable(true);
    		break;
    	default:
    		
    		UserStatus.setText(arrFromServerRet.get(4));
    		if(UserStatus.getText().equals("NotApproved")) {
        		NewOrderBTN.setDisable(true);
        		pickUpOrderBTN.setDisable(true);
        	}
        	if(ekOrOLFromConnect.get(0).equals("OL")) {
        		pickUpOrderBTN.setDisable(true);
        	}
        	if(ekOrOLFromConnect.get(0).equals("EK")) {
        		DeliveryBTN1.setDisable(true);
        	}
        	
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
        	
        	
    		break;
    	}
    	
    	
    	WelcomeLabel.setText("Welcome back " + arrFromServerRet.get(0));
    	
    	
	}
}