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

MarketingManagerHomePageController is a controller for the Marketing Manager home page GUI.

It handles the interactions between the GUI elements and the business logic.

*/
public class MarketingManagerHomePageController implements Initializable {
	
    @FXML
    private Button ExitBtn;

    @FXML
    private Label NameOfUserLabel;

    @FXML
    private Button NewOrderBTN;

    @FXML
    private Button SignOutbtn;

    @FXML
    private AnchorPane newOrderAndPickUpBTNAnchorPane;

    @FXML
    private Button pickUpOrderBTN;
    @FXML
    private Button addNewSaleBTN;

    @FXML
    private Button selectNewSale;
    
    @FXML
    private Button DeliveryBTN1;

    private double xoffset;
   	private double yoffset;
   	private static ArrayList<String> arrFromServerRet;
   	private static ArrayList<String> ekOrOLFromConnect;
   	private static ArrayList<MyFile> arrFromServerRetProductsMyFile;
   	
   	
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
   	Receives the user data from the previous controller.
   	@param arrFromServer an ArrayList containing the user data
   	*/
   	public static void getUserData(ArrayList<String> arrFromServer){
 		arrFromServerRet = arrFromServer;
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
 	   * Receives the product data from the DB in case he press create Button.
 	   *
 	   * @param arrFromServer an ArrayList containing the product data as MyFile objects (In case That The 
 	   * manager is also a customer , and hw want to make new order
 	   */
 	public static void getDataProductsFromDBMyFile(ArrayList<MyFile> arrFromServer) {
		arrFromServerRetProductsMyFile = arrFromServer;
	}
 	
 	
 	
 	/**
 	   * Handles the press of the Select New Sale button.
 	   * Hides the current window and opens the Choose Sales Marketing Managment Page.
 	   *
 	   * @param event the press of the Select New Sale button
 	   * * @throws Exception
   */
    @FXML
    void pressSelectNewSale(ActionEvent event) throws Exception {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		ChooseSalesMarketingManagmentPageController csmmpc = new ChooseSalesMarketingManagmentPageController();
		ChooseSalesMarketingManagmentPageController.ekOrOl(ekOrOLFromConnect);
		ChooseSalesMarketingManagmentPageController.getUserData(arrFromServerRet);
		csmmpc.start(primaryStage);
    	
    }
   	
   	
    /**
     * Handles the press of the Add New Sale button.
     * Hides the current window and opens the Create New Sale Marketing Manager page.
     *
     * @param event the press of the Add New Sale button
     * @throws Exception
     */
    @FXML
    void pressAddNewSale(ActionEvent event) throws Exception {
    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		CreateNewSaleMarketingManagerController cnsmmc = new CreateNewSaleMarketingManagerController();
		CreateNewSaleMarketingManagerController.ekOrOl(ekOrOLFromConnect);
		CreateNewSaleMarketingManagerController.getUserData(arrFromServerRet);
		cnsmmc.start(primaryStage);
    }

    

    /**
     * Handles the press of the New Order button.
     * If the user is in OL Marketing Manager, opens the Online Store New Order page.
     * If the user is in EK Marketing Manager, opens the Create New Order page.
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
     * Event handler for the Exit button press. Closes the program.
     *
     * @param event the action event that triggered this handler
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
    	LSC.start(primaryStage);

    }
    
    /**
     * Opens the Marketing Manager home page GUI.
     *
     * @param primaryStage the Stage to be shown
     * @throws Exception
     */
    public void start(Stage primaryStage) throws Exception {
    	
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/MarketingManager.fxml"));
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
     * Initializes the controller class.
     * Initializes the Name Of User label with the user's name.
     *
     * @param url the URL of the FXML file
     * @param rb the ResourceBundle of the FXML file
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
    	
    	
    	
    	
    	if(arrFromServerRet.get(4).equals("NotApproved")) {
    		NewOrderBTN.setDisable(true);
    		pickUpOrderBTN.setDisable(true);
    	}
    	if(arrFromServerRet.get(4).equals("Not A Customer")) {
 			newOrderAndPickUpBTNAnchorPane.setVisible(false);
 		}
 		if(ekOrOLFromConnect.get(0).equals("EK")) {
 			selectNewSale.setDisable(true);
 			addNewSaleBTN.setDisable(true);
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
