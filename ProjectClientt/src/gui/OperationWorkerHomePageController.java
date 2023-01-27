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
 * Controller for operation worker home page, options for: updating stock, update status message and buying/pick up (if he is a customer)
 * @author michal
 *
 */
public class OperationWorkerHomePageController implements Initializable{

    @FXML
    private ImageView ekrutLogoImage;

    @FXML
    private Button exitBtn;

    @FXML
    private Label lastUpdatesLable;

    @FXML
    private TextArea lastUpdatesTextArea;

    @FXML
    private Button logOutbtn;

    @FXML
    private Button statusMessageBtn;

    @FXML
    private ImageView statusMessageImg;

    @FXML
    private Button updateStockBtn;

    @FXML
    private ImageView updateStockImg;

    @FXML
    private Label welcomeBackLable;
    
    @FXML
    private AnchorPane newOrderAndPickUpBTNAnchorPane;

    @FXML
    private Button newOrderBtn;

    @FXML
    private ImageView newOrderImg;

    @FXML
    private Button pickUpOrderBtn;
    
    @FXML
    private Button DeliveryBTN1;


    @FXML
    private ImageView pickUpOrderImg;
    
    private double xoffset;
	private double yoffset;
	
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> ekOrOLFromConnect;
	private static ArrayList<String> detailsOnUserArray;
	private static ArrayList<MyFile> arrFromServerRetProductsMyFile;
	private ArrayList<String> messagesToShow = new ArrayList<>();

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
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/OperationWorkerHomePage.fxml"));

		root.setOnMousePressed(event1 -> {
			xoffset = event1.getSceneX();
			yoffset = event1.getSceneY();
		});

		root.setOnMouseDragged(event1 -> {
			primaryStage.setX(event1.getScreenX() - xoffset);
			primaryStage.setY(event1.getScreenY() - yoffset);
		});
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
 	 * Retrieve data from the server -EK or OL
 	 * @param arrFromServer ArrayList
 	 */
	public static void ekOrOl(ArrayList<String> arrFromServer) {
		ekOrOLFromConnect = arrFromServer;
	}
	
	/**
	 * Retrieves user data from the server, and displays any updates or messages. It also sets the availability of certain buttons based on user permissions and status.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		
		GetMessages checkMsg;
		try {
			checkMsg = new GetMessages(detailsOnUserArray.get(7));
			ArrayList<String> msg = checkMsg.getMsg();
			System.out.println(msg);
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
		
		
		
		String result = "";
		int ind = 1;
		ArrayList<String> msg1 = new ArrayList<>();
		String[] message;
		msg1.add("getUserDataOperationWorker");
		try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		welcomeBackLable.setText("Welcome back " + arrFromServerRet.get(arrFromServerRet.size()-1));
		for(int i=0;i<arrFromServerRet.size()-1;i++)
		{
			message = arrFromServerRet.get(i).split(",");
			messagesToShow.add(message[0]); //add to arrayList only message without status
		}
		
		//checking if there is any message
		if(messagesToShow.size()==0)
		{
			lastUpdatesTextArea.setText("No Update!");
		}
		
		else
		{
			for(int i=0;i<messagesToShow.size();i++)
			{
				result += (i+1) + ". " + messagesToShow.get(i);
				result += "\n\n";
			}
			
			lastUpdatesTextArea.setWrapText(true);
			lastUpdatesTextArea.setText(result);
		}
		
		
		if(ekOrOLFromConnect.get(0).equals("EK")) 
		{
			statusMessageBtn.setDisable(true);
			updateStockBtn.setDisable(true);
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
	
	/**
 	 * Retrieve data from the server
 	 * @param arrFromServer ArrayList
 	 */
	public static void getUserData(ArrayList<String> arrFromServer) {
		arrFromServerRet = arrFromServer;
	}
	
	/**
 	 * Stores the user details received from the user
 	 * @param arrFromServer ArrayList
 	 */
	public static void detailsOnUser(ArrayList<String> arrFromServer) {
		detailsOnUserArray = arrFromServer;
	}
	
	/**
	 * Used to store the product data received from the server in the form of MyFile objects
	 * @param arrFromServer ArrayList
	 */
	public static void getDataProductsFromDBMyFile(ArrayList<MyFile> arrFromServer) {
		arrFromServerRetProductsMyFile = arrFromServer;
	}

	/**
	 * Used to handle the event when the 'Status Message' button is pressed. It opens a new window for the Operation Worker to view and update status messages
	 * @param event ActionEvent
	 * @throws Exception
	 */
	@FXML
	void pressStatusMessage(ActionEvent event) throws Exception 
	{
		Stage primaryStage = new Stage();
		((Node) event.getSource()).getScene().getWindow().hide(); 
		primaryStage.initStyle(StageStyle.UNDECORATED);
		OperationWorkerStatusMessageController owsmc = new OperationWorkerStatusMessageController();
		OperationWorkerStatusMessageController.getMessages(messagesToShow);
		OperationWorkerStatusMessageController.getUserData(detailsOnUserArray);
		OperationWorkerStatusMessageController.ekOrOl(ekOrOLFromConnect);
		owsmc.start(primaryStage);
	}

	/**
	 * Used to handle the event when the 'Update Stock' button is pressed. And opens a new window for the Operation Worker to update stock
	 * @param event ActionEvent
	 * @throws Exception
	 */
	@FXML
	void pressUpdateStock(ActionEvent event) throws Exception 
	{
		Stage primaryStage = new Stage();
		((Node) event.getSource()).getScene().getWindow().hide(); 
		primaryStage.initStyle(StageStyle.UNDECORATED);
		OperationWorkerUpdateStockReviewController owusrc = new OperationWorkerUpdateStockReviewController();
		OperationWorkerUpdateStockReviewController.getUserData(detailsOnUserArray);
		OperationWorkerUpdateStockReviewController.ekOrOl(ekOrOLFromConnect);
		owusrc.start(primaryStage);
	}

	/**
	 * Press exit button from operation worker home page
	 * @param event ActionEvent
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
	 * Press logOut button from operation worker home page
	 * @param event ActionEvent
	 * @throws Exception
	 */ 	@FXML
 	void pressLogOut(ActionEvent event) throws Exception {
 		ArrayList<String> msg = new ArrayList<>();
 		msg.add("SignOut");
 		((Node) event.getSource()).getScene().getWindow().hide(); 
 		ClientUI.chat.accept(msg);
 		Stage primaryStage = new Stage();
 		primaryStage.initStyle(StageStyle.UNDECORATED);
 		LoginScreensController LSC = new LoginScreensController();
 		LSC.start(primaryStage);
 	}
 	
 	
	 /**
	* Press NewOrder button to buy in case of OL or EK (if he is a customer)
	* @param event ActionEvent
	* @throws Exception
	*/
 	@FXML
	void pressNewOrder(ActionEvent event) throws Exception {
		if(ekOrOLFromConnect.get(0).equals("OL")) {
			Stage primaryStage = new Stage();
			((Node)event.getSource()).getScene().getWindow().hide(); 
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
			((Node) event.getSource()).getScene().getWindow().hide(); 
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
     * @param event ActionEvent
     * @throws Exception
     */
 	@FXML
	void pressPickUpOrder(ActionEvent event) throws Exception {
		((Node)event.getSource()).getScene().getWindow().hide(); 
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		PickUpOrderLocalController puolc = new PickUpOrderLocalController();
		PickUpOrderLocalController.getMachineFromMainController(ekOrOLFromConnect.get(1));
		PickUpOrderLocalController.ekOrOl(ekOrOLFromConnect);
		PickUpOrderLocalController.getUserData(detailsOnUserArray);
		puolc.start(primaryStage);
	}

}
