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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class for operation worker for choosing machine from amount of machines
 * to update stock to
 * @author orb19
 *
 */
public class OperationWorkerUpdateStockReviewController implements Initializable{

    @FXML
    private Button backBtn;

    @FXML
    private ImageView ekrutLogoImage;

    @FXML
    private Label errorLable;

    @FXML
    private Button exitBtn;

    @FXML
    private ComboBox<String> locationComboBox;

    @FXML
    private Label locationLable;

    @FXML
    private ImageView machineNameImg;

    @FXML
    private Label machineNameLable;

    @FXML
    private Button nextBtn;

    @FXML
    private ComboBox<String> nameMachineComboBox;

    @FXML
    private ImageView typeImg;

    @FXML
    private Label updateLabel;
    
    private double xoffset;
	private double yoffset;
	
	private String selectedMachineName;
	private String selectedArea;
	
	private ObservableList<String> locationList; // for locationComboBox
	private ObservableList<String> machineNameList; // for nameMachineComboBox
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> arrMachinesFromDB;
	
	private static ArrayList<String> ekOrOLFromConnect;
	private static ArrayList<String> detailsOnUserArray;



    
    
	/**
	 * start window to provide movement window
	 * @param primaryStage
	 * @throws Exception
	 */
 	public void start(Stage primaryStage) throws Exception {
 		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/OperationWorkerUpdateStockReview.fxml"));

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
 		Scene scene = new Scene(root);
 		primaryStage.setScene(scene);
 		primaryStage.show();
 	}
    
 	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		locationList = FXCollections.observableArrayList("North", "South", "UAE");
		locationComboBox.setItems(locationList);
		
		
	}
 	
 	/*
 	public static void getOrderReportData(ArrayList<String> massageFromServer) {
 		arrFromServerRet = massageFromServer;	
	}
 	*/
 	
 	
 	/**
 	 * function that get the machines data from db
 	 * @param massageFromServer
 	 */
 	public static void getMachinesData(ArrayList<String> massageFromServer) {
 		arrMachinesFromDB = massageFromServer;	
	}

 	@FXML
 	/**
 	 * function that get the machines name from DB
 	 * @param event
 	 */
	void chooseNumberMachine(ActionEvent event) {
 		selectedMachineName = nameMachineComboBox.getSelectionModel().getSelectedItem();// get selected machine
																							// name in c.b
	}

    @FXML
    /**
     * function for choosing location
     * @param event
     */
    void chooseLocation(ActionEvent event) {
    	selectedArea = locationComboBox.getSelectionModel().getSelectedItem();
    	ArrayList<String> msg1 = new ArrayList<>();
		msg1.add("getMachineNameForSelectedArea");
		msg1.add(selectedArea);
		try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		machineNameList = FXCollections.observableArrayList();

		for (int i = 0; i < arrMachinesFromDB.size(); i++) { // copying to machineNumberList
			machineNameList.add(arrMachinesFromDB.get(i));
		}
		nameMachineComboBox.setItems(machineNameList); // show machines number for specific area
		
    }
    
 	@FXML
 	/**
 	 * press back button from current window
 	 * @param event
 	 * @throws Exception
 	 */
 	void pressBackBtn(ActionEvent event) throws Exception {
 		ArrayList<String> msg = new ArrayList<>();
 		msg.add("getUserDataOperationWorker");
 		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
 		ClientUI.chat.accept(msg);
 		Stage primaryStage = new Stage();
 		primaryStage.initStyle(StageStyle.UNDECORATED);
 		OperationWorkerHomePageController owhpc = new OperationWorkerHomePageController();
 		OperationWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
		OperationWorkerHomePageController.detailsOnUser(detailsOnUserArray);
 		owhpc.start(primaryStage);
 	}
 	

 	@FXML
 	/**
 	 * press exit button from current window
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
     * pressing next to move the next window where updating stock
     * @param event
     * @throws Exception
     */
    void pressNextBtn(ActionEvent event) throws Exception 
    {
    	ArrayList<String> areaAndMachineName = new ArrayList<>();
		if ((selectedMachineName==null)||(selectedArea==null))	
			errorLable.setText("Inorder to view update stock\n you must choose area and machine name");
		else
		{
			areaAndMachineName.add(selectedArea);
			areaAndMachineName.add(selectedMachineName);
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			OperationWorkerUpdateStockController owusc = new OperationWorkerUpdateStockController();
			OperationWorkerUpdateStockController.getAreaAndMachineName(areaAndMachineName);
			OperationWorkerUpdateStockController.ekOrOl(ekOrOLFromConnect);
			OperationWorkerUpdateStockController.detailsOnUser(detailsOnUserArray);
			owusc.start(primaryStage);
		}
    }
    
    
    /**
 	 * Retrieve data from the server -EK or OL
 	 * @param arrFromServer ArrayList
 	 */
	public static void ekOrOl(ArrayList<String> arrFromServer) {
		ekOrOLFromConnect = arrFromServer;
	}
	
	
	/**
 	 * Stores the user details received from the user
 	 * @param arrFromServer ArrayList
 	 */
	public static void getUserData(ArrayList<String> arrFromServer) {
		detailsOnUserArray = arrFromServer;
	}

}

