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
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.GetMessages;

/**
 * function for area manager home page controller
 * @author Michal and Or
 *
 */
public class AreaManagerHomePageController implements Initializable {

	@FXML
	private ImageView approveRequestImg;

	@FXML
	private Button approveRequestsBtn;

	@FXML
	private ImageView ekrutLogoImage;

	@FXML
	private ImageView executionOrderImg;

	@FXML
	private Button exitBtn;

	@FXML
	private Label lastUpdatesLable;

	@FXML
	private TextArea lastUpdatesTextArea;

	@FXML
	private Button logOutbtn;

	@FXML
	private ImageView managmentAreaImage;

	@FXML
	private Label managmentAreaLable;

	@FXML
	private ImageView minLevelImage;

	@FXML
	private ImageView refreshImage;

	@FXML
	private Button sendMsgToOperationBtn;

	@FXML
	private Button setMinimummachinelevelBtn;

	@FXML
	private Label specificAreaLable;

	@FXML
	private ImageView watchReportImage;

	@FXML
	private Button watchReportsBtn;

	@FXML
	private Label welcomeBackLable;
	
	@FXML
    private AnchorPane newOrderAndPickUpBTNAnchorPane;

    @FXML
    private Button newOrderBtn;
    
    @FXML
    private Button pickUpOrderBtn;
    
    @FXML
    private Button DeliveryBTN1;

    
    
    
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
		DeliveryGetController.getUserData(detailsOnUserArray);
		dgc.start(primaryStage);

    }
    
    

	private double xoffset;
	private double yoffset;
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> arrInventoryUpdate;
	private static ArrayList<String> ekOrOLFromConnect;
	private static ArrayList<String> detailsOnUserArray;
	private static ArrayList<MyFile> arrFromServerRetProductsMyFile;
	
	@FXML
	/**
	 * press exit button from area manager page
	 * @param event
	 * @throws IOException
	 */
	void pressExitBtn(ActionEvent event) throws IOException {
		ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ClientUI.chat.accept(msg);
		System.exit(1);
	}

	@FXML
	/**
	 * press logOut button from area manager page
	 * @param event
	 * @throws Exception
	 */
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
	 * start window to provide movement window
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/AreaManagerHomePage.fxml"));

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
	 * function that save arrayList from server for user data
	 * @param arrFromServer
	 */
	public static void getUserData(ArrayList<String> arrFromServer) {
		arrFromServerRet = arrFromServer;
	}

	/**
	 * function that save arrayList from server for inventory data
	 * @param arrFromServer
	 */
	public static void getInventoryUpdateData(ArrayList<String> arrFromServer) {
		arrInventoryUpdate = arrFromServer;
	}
	
	/**
	 * function that save arrayList from server for ek or ol 
	 * @param arrFromServer
	 */
	public static void ekOrOl(ArrayList<String> arrFromServer) {
		ekOrOLFromConnect = arrFromServer;
	}
	
	
	public static void getDataProductsFromDBMyFile(ArrayList<MyFile> arrFromServer) {
		arrFromServerRetProductsMyFile = arrFromServer;
	}
	
	/**
	 * function that save arrayList from server for details on current user
	 * @param arrFromServer
	 */
	public static void detailsOnUser(ArrayList<String> arrFromServer) {
		detailsOnUserArray = arrFromServer;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String result = "";
		int ind = 1;
		ArrayList<String> res = new ArrayList<>();
		ArrayList<String> msg1 = new ArrayList<>();
		GetMessages checkMsg;
		try {
			checkMsg = new GetMessages(detailsOnUserArray.get(7));
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
		msg1.add("getUserDataAreaManager");
		try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		welcomeBackLable.setText("Welcome back " + arrFromServerRet.get(1));
		specificAreaLable.setText(arrFromServerRet.get(3));
		
		

		ArrayList<String> msg2 = new ArrayList<>();
		msg2.add("getUpdateInfoForMA");
		msg2.add(arrFromServerRet.get(3));
		try {
			ClientUI.chat.accept(msg2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (arrInventoryUpdate.get(0).equals("No Update")) {
			lastUpdatesTextArea.setText("No Update");
		} else {
			for (int i = 0; i < arrInventoryUpdate.size(); i++) {
				String arr[] = arrInventoryUpdate.get(i).split(" ");
				result += ind + ". Machine Name: " + arr[0] + "\n";
				for (int j = 1; j < arr.length; j++) {
					result += arr[j];
					if (j != arr.length - 1)
						result += ",";
				}
				if (arr.length>2)
					result += " are below minimum level.\n\n";
				else
					result += " is below minimum level.\n\n";
				
				ind++;
			}
			lastUpdatesTextArea.setWrapText(true);
			lastUpdatesTextArea.setText(result);
		}
		
		if(ekOrOLFromConnect.get(0).equals("EK")) {
			watchReportsBtn.setDisable(true);
			approveRequestsBtn.setDisable(true);
			sendMsgToOperationBtn.setDisable(true);
			setMinimummachinelevelBtn.setDisable(true);
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

	@FXML
	/**
	 * show next window for choosing reports
	 * @param event
	 * @throws Exception
	 */
	void pressWatchReports(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		primaryStage.initStyle(StageStyle.UNDECORATED);
		AreaManagerReportViewController amrvc = new AreaManagerReportViewController();
		AreaManagerReportViewController.getUserData(detailsOnUserArray);
		AreaManagerReportViewController.ekOrOl(ekOrOLFromConnect);
		amrvc.start(primaryStage);
	}

	@FXML
	/**
	 * show next window for setting minimum level
	 * @param event
	 * @throws Exception
	 */
	void pressSetMinimummachinelevel(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		primaryStage.initStyle(StageStyle.UNDECORATED);
		SetMinimumLevelController smlc = new SetMinimumLevelController();
		SetMinimumLevelController.getUserData(detailsOnUserArray);
		SetMinimumLevelController.ekOrOl(ekOrOLFromConnect);
		smlc.start(primaryStage);
	}
	
	@FXML
	/**
	 * show next window for send execution order to operation worker
	 * @param event
	 * @throws Exception
	 */
	void pressExecutionOrder(ActionEvent event) throws Exception{
		Stage primaryStage = new Stage();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		primaryStage.initStyle(StageStyle.UNDECORATED);
		AreaManagerExecutionOrderController ameoc = new AreaManagerExecutionOrderController();
		AreaManagerExecutionOrderController.getUserData(detailsOnUserArray);
		AreaManagerExecutionOrderController.ekOrOl(ekOrOLFromConnect);
		ameoc.start(primaryStage);
	}
	
	
	@FXML
	/**
	 * pressing approve request button
	 * @param event
	 * @throws Exception
	 */
	void pressApproveRequests(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		primaryStage.initStyle(StageStyle.UNDECORATED);
		AreaManagerApproveRequestsController amarc = new AreaManagerApproveRequestsController();
		AreaManagerApproveRequestsController.getUserData(detailsOnUserArray);
		AreaManagerApproveRequestsController.ekOrOl(ekOrOLFromConnect);
		amarc.start(primaryStage);
		
	}
	
	
	
	@FXML
	/**
	 * pressing new order button
	 * @param event
	 * @throws Exception
	 */
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

	@FXML
	/**
	 * pressing pick up order button
	 * @param event
	 * @throws Exception
	 */
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
	
	



}
