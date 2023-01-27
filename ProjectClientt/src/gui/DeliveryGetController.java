
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Delivery;
import logic.Sale;
import logic.UpdateDeliveryCustomer;

/*
 * The DeliveryGetController class is the controller for the delivery management GUI.
 * It displays a list of deliveries in a TableView and allows the user to interact with them.
 */
public class DeliveryGetController implements Initializable {
	
	/*
     * Text field for entering an order code to filter deliveries.
     */
	@FXML
	private TextField EnterOrderCode;
	 /*
     * Button for sighing out .
     */
	@FXML
	private Button SignOutbtn111;
	 /*
     * Button for marking a delivery as received.
     */
	@FXML
	private Button OrderreceivedBTN;
	
	 /*
     * Table column for displaying the order number of a delivery.
     */
	@FXML
	private TableColumn<UpdateDeliveryCustomer, String> ordernumCol;

	 /**
     * Table column for displaying the status of a delivery.
     */
	@FXML
	private TableColumn<UpdateDeliveryCustomer, String> statusCol;
	
	/**
     * Table column for displaying the delivery status if received.
     */
    @FXML
    private TableColumn<UpdateDeliveryCustomer, String> DeliveryReceivedcol;
    
    /**
     * Table view for displaying the list of deliveries.
     */
	@FXML
	private TableView<UpdateDeliveryCustomer> tableOrders;

	/**
     * Label for displaying error messages.
     */
	@FXML
	private Label errorLabel;
	
	 /*
     * Exit button to close the application.
     */
	@FXML
	private Button exitBTN;
	 /*
     * xoffset,yoffset to handle drag of the window.
     */
	private double xoffset;
	private double yoffset;
	 /*
     * ArrayList of strings to hold data retrieved from the server.
     */
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> arrFromServerRet3;
	 /*
     * Observable list to hold data to be displayed in the table view.
     */
	private static ObservableList<UpdateDeliveryCustomer> list = FXCollections.observableArrayList();
    /*
     * Boolean variable to check whether the data has been retrieved from the server.
     */
	private static boolean isCalled = false;
	 /*
     * ArrayList of UpdateDeliveryCustomer objects to hold data retrieved from the database.
     */
	private static ArrayList<UpdateDeliveryCustomer> deliveryFromDb = new ArrayList<>();

    /*
     * Static method to retrieve data from the server for pick-up orders.
     * @param arrFromServer an ArrayList of strings containing data from the server
     */
	public static void getDataFromDbPickUp(ArrayList<String> arrFromServer) {
		arrFromServerRet = arrFromServer;
	}
	
	/*
     * ArrayList of strings to hold data about connection type of user that retrieved from the server.
     */
	private static ArrayList<String> ekOrOLFromConnect;
	
	/**
     * Static method to retrieve data from the server for ekOrOl.
     * @param ekOrOLFromConnect1
     */
	public static void ekOrOl(ArrayList<String> ekOrOLFromConnect1) {
		ekOrOLFromConnect = ekOrOLFromConnect1;
	}

    /*
     * Static method to retrieve data from the server for the user.
     * @param arrFromServer an ArrayList of strings containing data from the server
     */
	public static void getUserData(ArrayList<String> arrFromServer) {
		arrFromServerRet3 = arrFromServer;
	}

    /*
     * Event handler for the "Sign out" button press.
     * Returns the user to the login page.
     * @param event the ActionEvent that triggered the handler
     * @throws Exception
     */
	@FXML
	void pressSignOut(ActionEvent event) throws Exception {

		Stage primaryStage = new Stage();
		String role = arrFromServerRet3.get(2);
		switch (role) {
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
		
		case "User":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			CustomerMainScreenController cmcu = new CustomerMainScreenController();
			CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
			CustomerMainScreenController.getUserData(arrFromServerRet3);
			cmcu.start(primaryStage);	
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

		case "ServiceRepresentative":
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			ServiceRepresentativeHomePageController srhpc = new ServiceRepresentativeHomePageController();
			ServiceRepresentativeHomePageController.getUserData(arrFromServerRet3);
			ServiceRepresentativeHomePageController.ekOrOl(ekOrOLFromConnect);
			srhpc.start(primaryStage);
			break;
		case "MarketingManager":
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			MarketingManagerHomePageController mmhpc = new MarketingManagerHomePageController();
			MarketingManagerHomePageController.getUserData(arrFromServerRet3);
			MarketingManagerHomePageController.ekOrOl(ekOrOLFromConnect);
			mmhpc.start(primaryStage);
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

	 /*
     * Event handler for the "Exit" button press.
     * Closes the application.
     * @param event the ActionEvent that triggered the handler
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
     * Event handler for the "Order received" button press.
     * Marks the selected delivery as received and updates the status in the database.
     * @param event the ActionEvent that triggered the handler
     */
	@FXML
	void pressOrderreceivedBTN(ActionEvent event) throws Exception {
		errorLabel.setText("");
		 boolean idFound = false;
		String orderCode = EnterOrderCode.getText();

		for(UpdateDeliveryCustomer dev : deliveryFromDb) {
			if(dev.getOrdernumber().equals(orderCode)) {
				ArrayList<String> msg = new ArrayList<>();
				msg.add("updateResDeliveryTaken");
				msg.add(orderCode);
				try {
					ClientUI.chat.accept(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dev.setDeliveryReceived("1");
				list.setAll(deliveryFromDb);
				idFound = true;
				break;	
			
			}
		}
		if(!idFound) {
			errorLabel.setText("id Not Found");
 		}
	}

	 /**
     * setDeliverysList method is to get the data from the database and to add it to the list that will be shown in the table view.
     * 
     */
	public static void setDeliverysList(ArrayList<String> arrFromServer) {
		deliveryFromDb.clear();
		if (!arrFromServer.get(0).equals("Error")) {
			for (int i = 0; i < arrFromServer.size(); i++) {
				String deliverys[] = ((String) arrFromServer.get(i)).split(",");
				deliveryFromDb.add(new UpdateDeliveryCustomer(deliverys[0], deliverys[1],deliverys[2]));

			}
		
		}else if(isCalled) {
			
		}
	

	}

	/*
	* start method is to load the FXML file, to move the window with the mouse and to show the data in the table view.
	* @param primaryStage the primary stage for this application
	* @throws Exception
	*/
	public void start(Stage primaryStage) throws Exception {

		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/DeliveryGet.fxml"));
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
     * Initializes the controller class.
     * Populates the table view with data from the server and sets up the table columns.
     * @param url the URL used to resolve relative paths for the root object
     * @param rb the ResourceBundle used to localize the root object
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<String> msg = new ArrayList<>();
		msg.add("getdelorders");
		try {
			ClientUI.chat.accept(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (arrFromServerRet.get(0).equals("Error")) {
			errorLabel.setText("NO Order Found for you");
		} else {
			if(!isCalled) {
				list.addAll(deliveryFromDb);
			}else {
				list.setAll(deliveryFromDb);
			}
			
			ordernumCol.setCellValueFactory(new PropertyValueFactory<UpdateDeliveryCustomer, String>("Ordernumber"));
			statusCol.setCellValueFactory(new PropertyValueFactory<UpdateDeliveryCustomer, String>("orderstatus"));
			DeliveryReceivedcol.setCellValueFactory(new PropertyValueFactory<UpdateDeliveryCustomer, String>("deliveryReceived"));
			tableOrders.setItems(list);
		}
		isCalled = true;
	}

}
