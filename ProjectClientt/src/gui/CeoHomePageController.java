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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.GetMessages;

/**
 * Controller for CEO home page - option for showing report 
 * @author michal
 *
 */
public class CeoHomePageController implements Initializable{

    @FXML
    private ImageView ekrutLogoImage;

    @FXML
    private Button exitBtn;

    @FXML
    private Button logOutbtn;

    @FXML
    private AnchorPane newOrderAndPickUpBTNAnchorPane;

    @FXML
    private Button newOrderBtn;

    @FXML
    private ImageView newOrderImg;

    @FXML
    private Button pickUpOrderBtn;

    @FXML
    private ImageView pickUpOrderImg;

    @FXML
    private ImageView watchReportImage;

    @FXML
    private Button watchReportsBtn;

    @FXML
    private Label welcomeBackLable;
    
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> ekOrOLFromConnect;
	private static ArrayList<String> detailsOnUserArray;
	private static ArrayList<MyFile> arrFromServerRetProductsMyFile;

	
	private double xoffset;
	private double yoffset;
	
	
	 @FXML
	    private Button DeliveryBTN1;

	    @FXML
	    void PressDeliveryBTN(ActionEvent event) throws Exception {
	    	((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			DeliveryGetController dgc = new DeliveryGetController();
			DeliveryGetController.ekOrOl(ekOrOLFromConnect);
			DeliveryGetController.getUserData(detailsOnUserArray);
			dgc.start(primaryStage);

	    }
	
	/**
	 * Start window to provide movement window
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/CeoHomePage.fxml"));

		root.setOnMousePressed(event1 -> {
			xoffset = event1.getSceneX();
			yoffset = event1.getSceneY();
		});

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
	 * Press exit button from ceo home page
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void pressExitBtn(ActionEvent event) throws IOException {
		ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ClientUI.chat.accept(msg);
		System.exit(1);
	}

	/**
	 * Press logOut button from CEO home page
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void pressLogOut(ActionEvent event) throws Exception {
		ArrayList<String> msg = new ArrayList<>();
		msg.add("SignOut");
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ClientUI.chat.accept(msg);
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		LoginScreensController LSC = new LoginScreensController();
		LSC.start(primaryStage);
	}

	/**
	 * Press NewOrder button to buy in case of OL or EK (if he is a customer)
	 * @param event
	 * @throws Exception
	 */
    @FXML
	void pressNewOrder(ActionEvent event) throws Exception {
		if(ekOrOLFromConnect.get(0).equals("OL")) {
			Stage primaryStage = new Stage();
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			OrderTypeController otc = new OrderTypeController();
			OrderTypeController.getDataAboutUser(detailsOnUserArray);

			OrderTypeController.ekOrOl(ekOrOLFromConnect);
			if(detailsOnUserArray.get(5).equals("1")) {
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
			EKrutFinalMarketController.getUserData(detailsOnUserArray);
			EKrutFinalMarketController.setMacineName(ekOrOLFromConnect.get(1));

			EKrutFinalMarketController.setArea(ekOrOLFromConnect.get(2));
			EKrutFinalMarketController.ekOrOl(ekOrOLFromConnect);
			if(detailsOnUserArray.get(5).equals("1")) {
				EKrutFinalMarketController.setIsAMember(true);
			}else {
				EKrutFinalMarketController.setIsAMember(false);
			}
			ekfmc.start(primaryStage);	
		}
	}

    /**
     * Press pick up button to pick up order
     * @param event
     * @throws Exception
     */
    @FXML
	void pressPickUpOrder(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		PickUpOrderLocalController puolc = new PickUpOrderLocalController();
		PickUpOrderLocalController.getMachineFromMainController(ekOrOLFromConnect.get(1));
		PickUpOrderLocalController.ekOrOl(ekOrOLFromConnect);
		PickUpOrderLocalController.getUserData(detailsOnUserArray);
		puolc.start(primaryStage);
	}


    /**
     * Showing next window for choosing reports
     * @param event
     * @throws Exception
     */
 	@FXML
 	void pressWatchReports(ActionEvent event) throws Exception {
 		Stage primaryStage = new Stage();
 		((Node) event.getSource()).getScene().getWindow().hide(); 
 		primaryStage.initStyle(StageStyle.UNDECORATED);
 		CeoReportViewController crvc = new CeoReportViewController();
 		CeoReportViewController.getUserData(detailsOnUserArray);
 		CeoReportViewController.ekOrOl(ekOrOLFromConnect);
 		crvc.start(primaryStage);
 	}
 	
 	/**
 	 * Retrieve data from the server -EK or OL
 	 * @param arrFromServer ArrayList
 	 */
 	public static void ekOrOl(ArrayList<String> arrFromServer) {
		ekOrOLFromConnect = arrFromServer;
	}
 	
 	/**
 	 * Retrieve the details of the current user from the server.
 	 * @param arrFromServer ArrayList
 	 */
 	public static void detailsOnUser(ArrayList<String> arrFromServer) {
		detailsOnUserArray = arrFromServer;
	}
 	
 	/**
 	 * Used to retrieve product data from the server.
 	 * @param arrFromServer ArrayList
 	 */
 	public static void getDataProductsFromDBMyFile(ArrayList<MyFile> arrFromServer) {
		arrFromServerRetProductsMyFile = arrFromServer;
	}
 	
 	/**
 	 * Retrieve data from the server
 	 * @param arrFromServer ArrayList
 	 */
 	public static void getUserData(ArrayList<String> arrFromServer) {
		arrFromServerRet = arrFromServer;
	}

 	/**
 	 * Gets data from the server to determine the current user's information and updates the UI accordingly
 	 */
 	@Override
	public void initialize(URL location, ResourceBundle resources) {

		String result = "";
		int ind = 1;
		ArrayList<String> res = new ArrayList<>();
		ArrayList<String> msg1 = new ArrayList<>();
		msg1.add("getUserDataCeo");
		try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		welcomeBackLable.setText("Welcome back " + arrFromServerRet.get(1));
		GetMessages checkMsg;
		try {
			checkMsg = new GetMessages("Ceo");
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

		ArrayList<String> msg2 = new ArrayList<>();
		msg2.add("getUpdateInfoForMA");
		msg2.add(arrFromServerRet.get(3));
		try {
			ClientUI.chat.accept(msg2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(ekOrOLFromConnect.get(0).equals("EK")) {
			watchReportsBtn.setDisable(true);
			DeliveryBTN1.setDisable(true);
    	}
		else
			pickUpOrderBtn.setDisable(true);
				
		if(detailsOnUserArray.get(4).equals("Not A Customer"))
			newOrderAndPickUpBTNAnchorPane.setVisible(false);
		else if(detailsOnUserArray.get(4).equals("NotApproved"))
		{
			newOrderBtn.setDisable(true);
			pickUpOrderBtn.setDisable(true);
			DeliveryBTN1.setDisable(true);
		}		
	}
}

