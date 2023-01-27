package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The OrderPickedController class is used to handle the behavior of the OrderPicked.fxml
 * file and its corresponding GUI. This class handles the button presses and sets the 
 * behavior of the window.
 */
public class OrderPickedController implements Initializable{

    /**
     * backToHomePageBTN button is used to return to the previous screen
     */
    @FXML
    private Button backToHomePageBTN;

    /**
     * exitBTN button is used to exit the application
     */
    @FXML
    private Button exitBTN;

    /**
     * textAreaOrder is used to show the Area of the order
     */
    @FXML
    private TextArea textAreaOrder;
    
    /*
     * xoffset,yoffset to handle drag of the window.
     */
    private double xoffset;
	private double yoffset;
	/**
	 * A static ArrayList of String to store the order details
	 */
	private static ArrayList<String> order;
	private static ArrayList<String> arrFromServerRet3;
	private static ArrayList<String> ekOrOLFromConnect;
	/**
     * Static method to retrieve data from the server for ekOrOl.
     * @param ekOrOLFromConnect1
     */
	public static void ekOrOl(ArrayList<String> ekOrOLFromConnect1){
     	ekOrOLFromConnect = ekOrOLFromConnect1;
     }
 
    /*
     * Static method to retrieve data from the server for the user.
     * @param arrFromServer an ArrayList of strings containing data from the server
     */
	 public static void getUserData(ArrayList<String> arrFromServer){
	    	arrFromServerRet3 = arrFromServer;
	    }
	
	    /**
	     * Handles the functionality when the "Return" button is pressed. 
	     * It returns to the previous screen ServiceRepresentativeHomePage.
	     *
	     * @param event The event that triggers the method, in this case the "Return" button press.
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
	/*
	* start method is to load the FXML file, to move the window with the mouse and to show the data in the table view.
	* @param primaryStage the primary stage for this application
	* @throws Exception
	*/
public void start(Stage primaryStage) throws Exception {
    	
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/OrderPickedScreen.fxml"));
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
 * This class is the controller for the 'OrderPicked' scene. It is responsible for handling the events triggered by the
 * buttons in the scene, and updating the text area with the order details.
 * @param order1
*/
	public static void getOrderFromPickUpConroller(ArrayList<String> order1) {
		order = order1;
	}

	/**
	* Initializes the controller class by getting the order from the PickUpController and displaying it in the text area.
	*
	* @param location The location used to resolve relative paths for the root object, or
	* {@code null} if the location is not known.
	* @param resources The resources used to localize the root object, or {@code null} if the root
	* object was not localized.
	*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		String title ="Product\tAmount\n";
		String orders = "";
		String[] arrOfMachines = order.get(0).split(",");
		for(int i = 0;i<arrOfMachines.length;i+=2) {
			if(arrOfMachines[i].equals("Bisli")) {
				orders = orders + arrOfMachines[i] + "\t\t      " + arrOfMachines[i+1] + "\n";
			}
			else {
				orders = orders + arrOfMachines[i] + "\t      " + arrOfMachines[i+1] + "\n";
			}
		}
		textAreaOrder.setText(title+orders);
	}
}
