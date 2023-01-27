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
 * Controller for CEO report view, by choosing type of report (inventory, customers and orders), year, month and machine name
 * @author michal
 *
 */
public class CeoReportViewController implements Initializable{

    @FXML
    private ComboBox<String> areaComboBox;

    @FXML
    private ImageView areaImg;

    @FXML
    private Label areaLabel;

    @FXML
    private Button backBtn;

    @FXML
    private ImageView calendarImg;

    @FXML
    private ImageView ekrutLogoImage;

    @FXML
    private Label errorReportDataLable;

    @FXML
    private Button exitBtn;

    @FXML
    private ImageView machineNumberImg;

    @FXML
    private Label machineNumberLable;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private ComboBox<String> nameMachineComboBox;

    @FXML
    private Label reportLable;

    @FXML
    private ComboBox<String> reportTypeComboBox;

    @FXML
    private Button showReportBtn;

    @FXML
    private ImageView typeImg;

    @FXML
    private Label typeLable;

    @FXML
    private ComboBox<String> yearComboBox;
    
	private String selectedMachineName;
	private String selectedMonth;
	private String selectedYear;
	private String selectedReportType;
	private String selectedLocation;
	
	private ObservableList<String> machineNameList; // for numberMachineComboBox
	private ObservableList<String> reportTypeList; // for reportTypeComboBox
	private ObservableList<String> yearList; // for yearComboBox
	private ObservableList<String> monthList; // for monthComboBox
	private ObservableList<String> locationList; // for areaComboBox

	private static ArrayList<String> arrMachinesNames;
	private static ArrayList<String> arrReportData;
	private static ArrayList<String> arrCustomersReportData;
	private static ArrayList<String> arrInventoryReportData;
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> ekOrOLFromConnect;


	
    private double xoffset;
	private double yoffset;
    
	/**
	 * Start window to provide movement window
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
 		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/CeoReportView.fxml"));

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
	 * Used to set the options for the area combo box - north, south and uae
	 */
 	@Override
	public void initialize(URL location, ResourceBundle resources) {
 		locationList = FXCollections.observableArrayList("North", "South", "UAE");
 		areaComboBox.setItems(locationList);
	}

 	/**
 	 * Handle the action of selecting a location from the location combo box. It also updates the report type, year, and month comboboxes with their respective options.
 	 * @param event ActionEvent
 	 */
    @FXML
    void chooseLocation(ActionEvent event) {
		selectedLocation = areaComboBox.getSelectionModel().getSelectedItem();// get selected location
		ArrayList<String> msg1 = new ArrayList<>();
		msg1.add("getMachineNameForSelectedAreaCeo");
		msg1.add(selectedLocation);
		try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		machineNameList = FXCollections.observableArrayList();

		for (int i = 0; i < arrMachinesNames.size(); i++) { // copying to machineNumberList
			machineNameList.add(arrMachinesNames.get(i));
		}
		nameMachineComboBox.setItems(machineNameList); // show machines number for specific area
		reportTypeList = FXCollections.observableArrayList("Orders", "Inventory", "Customers");
		reportTypeComboBox.setItems(reportTypeList);
		yearList = FXCollections.observableArrayList("2022", "2021", "2020", "2019", "2018", "2017", "2016");
		yearComboBox.setItems(yearList);
		monthList = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
		monthComboBox.setItems(monthList);
    }

    /**
	 * Get selected machine number in combo box
	 * @param event ActionEvent
	 */
    @FXML
	void chooseMachine(ActionEvent event) {
		selectedMachineName = nameMachineComboBox.getSelectionModel().getSelectedItem();// get selected machine
																							// number in c.b
	}

    /**
	 * Get selected month in combo box
	 * @param event ActionEvent
	 */
    @FXML
	void chooseMonth(ActionEvent event) {
		selectedMonth = monthComboBox.getSelectionModel().getSelectedItem();// get selected month in c.b
	}

    /**
	 * Get selected report type in combo box
	 * @param event ActionEvent
	 */
    @FXML
	void chooseTypeReport(ActionEvent event) {
		selectedReportType = reportTypeComboBox.getSelectionModel().getSelectedItem();// get selected report type
																						// in c.b
		
		if((selectedReportType.equals("Orders")) || (selectedReportType.equals("Customers")))
		{
			nameMachineComboBox.setDisable(true);
		}
		else
			nameMachineComboBox.setDisable(false);
	}

    /**
	 * Get selected year in combo box
	 */
    @FXML
	void chooseYear(ActionEvent event) {
		selectedYear = yearComboBox.getSelectionModel().getSelectedItem();// get selected year in c.b
	}

    /**
	 *  Press back button from area manager page
	 * @param event ActionEvent
	 * @throws Exception
	 */
    @FXML
 	void pressBackBtn(ActionEvent event) throws Exception {
 		ArrayList<String> msg = new ArrayList<>();
 		msg.add("getUserDataCeo");
 		((Node) event.getSource()).getScene().getWindow().hide(); 
 		ClientUI.chat.accept(msg);
 		Stage primaryStage = new Stage();
 		primaryStage.initStyle(StageStyle.UNDECORATED);
 		CeoHomePageController chpc = new CeoHomePageController();
 		CeoHomePageController.detailsOnUser(arrFromServerRet);
		CeoHomePageController.ekOrOl(ekOrOLFromConnect);
 		chpc.start(primaryStage);
 	}

 	/**
	 * Press exit button from area manager page
	 * @param event ActionEvent
	 * @throws IOException
	 */ 
 	@FXML
 	void pressExitBtn(ActionEvent event) throws IOException {
 		ArrayList<String> msg = new ArrayList<>();
 		msg.add("quit");
 		((Node) event.getSource()).getScene().getWindow().hide(); 
 		ClientUI.chat.accept(msg);
 		System.exit(1);
 	}

 	/**
	 * Show next window for orders report - customers, inventory and orders
	 * @param event ActionEvent
	 * @throws Exception
	 */
 	@FXML
 	void pressShowReport(ActionEvent event) throws Exception {
 		if(checkValidReportData()) {
 			ArrayList<String> msg = new ArrayList<>();
 			switch(selectedReportType) {
 				case "Orders":
 					msg.add("getOrderReportDetailsForCeo");
 					break;
 				case "Customers":
 					msg.add("getCustomersReportDetailsForCeo");
 					break;
 				case "Inventory":
 					msg.add("getInventoryReportDetailsForCeo");
 					break;
 			}
 			if(selectedReportType.equals("Inventory"))
 				msg.add(selectedMachineName);
 			msg.add(selectedLocation);
 			msg.add(selectedMonth);
 			msg.add(selectedYear);
 			ClientUI.chat.accept(msg);
 			if(checkingErrorMsg())
 			{ //showing report view if the user put current details
 				Stage primaryStage = new Stage();
 				((Node) event.getSource()).getScene().getWindow().hide(); 
 				primaryStage.initStyle(StageStyle.UNDECORATED);
 				switch(selectedReportType) {
 				case "Orders":
 					OrderReportViewController orvc = new OrderReportViewController();
 					OrderReportViewController.getOrderReportDetails(arrReportData);
 					OrderReportViewController.getUserData(arrFromServerRet);
 					OrderReportViewController.ekOrOl(ekOrOLFromConnect);
 					orvc.start(primaryStage);
 					break;
 				case "Customers":
 					ClientHistogramReportViewController chrvc = new ClientHistogramReportViewController();
 					ClientHistogramReportViewController.getCustomersReportDetails(arrCustomersReportData);
 					ClientHistogramReportViewController.getUserData(arrFromServerRet);
 					ClientHistogramReportViewController.ekOrOl(ekOrOLFromConnect);
 					chrvc.start(primaryStage);
 					break;
 				case "Inventory":
 					InventoryReportViewController irvc = new InventoryReportViewController();
 					InventoryReportViewController.getInventoryReportDetails(arrInventoryReportData);
 					InventoryReportViewController.getUserData(arrFromServerRet);
 					InventoryReportViewController.ekOrOl(ekOrOLFromConnect);
 					irvc.start(primaryStage);
 					break;
 				}

 			}
 		}
 	}	
 	
 	/**
	 *  Used to check if the report data is valid. If the data is invalid, it sets the text for the errorReportDataLabel and returns false.
	 * @return true is there is no error or false if there is an error
	 */
 	private boolean checkingErrorMsg() {
 		int flag=1;
 		switch(selectedReportType) {
 		case "Orders":
 			if((arrReportData.size()==0)||(arrReportData.get(0).equals("Error")))
 				flag=0;		
 			break;
 		case "Customers":
 			if((arrCustomersReportData.size()==0)||(arrCustomersReportData.get(0).equals("Error")))
 				flag=0;		
 			break;
 		case "Inventory":
 			if((arrInventoryReportData.size()==0)||(arrInventoryReportData.get(0).equals("Error")))
 				flag=0;		
 			break;
 		}
 		if (flag==0) {
 			errorReportDataLable.setText("No such report");
 			return false;
 		}
 		return true;	
 	}

 	/**
	 * Checks if the user has selected a month, year, machine name, and report type. If any of these selections are missing, it sets the text for the errorReportDataLabel and returns false.
	 * @return true if data is valid, else false
	 */
 	boolean checkValidReportData() {
 		
 		int flagError=0;
 		
 		if((selectedReportType!=null)&&(selectedReportType.equals("Orders")) || 
 				(selectedReportType!=null)&&(selectedReportType.equals("Customers")))
 		{
 			if ((selectedMonth==null)||(selectedYear==null)||(selectedReportType==null))
 				flagError = 1;
 		}
 		else
 		{
 			if ((selectedMachineName==null)||(selectedMonth==null)||
 					(selectedYear==null)||(selectedReportType==null))
 				flagError = 1;
 		}
 		
 		if(flagError==1)
 		{
 			errorReportDataLable.setText("Inorder to view report you must choose area\n year, month, machine name and report type");
 			return false;
 		}
 		return true;
 	}
    
 	/**
	 * Used to retrieve the machine names from the server.
	 * @param massageFromServer ArrayList
	 */
    public static void getMachinesNames(ArrayList<String> massageFromServer) {
		arrMachinesNames = massageFromServer;
	}
    
    /**
	 * Used to retrieve the order report data from the server.
	 * @param massageFromServer ArrayList
	 */
    public static void getOrderReportData(ArrayList<String> massageFromServer) {
		arrReportData = massageFromServer;	
	}

    /**
	 * Used to retrieve the customers report data from the server.
	 * @param massageFromServer ArrayList
	 */
	public static void getCustomersReportData(ArrayList<String> massageFromServer) {
		arrCustomersReportData = massageFromServer;
	}
	
	/**
	 * Used to retrieve the inventory report data from the server.
	 * @param massageFromServer ArrayList
	 */
	public static void getInventoryReportData(ArrayList<String> massageFromServer) {
		arrInventoryReportData = massageFromServer;
	}
	
	/**
	 * Used to retrieve the user data from the server.
	 * @param arrFromServer ArrayList
	 */
	public static void getUserData(ArrayList<String> arrFromServer) {
		arrFromServerRet = arrFromServer;
	}
	
	/**
 	 * Retrieve data from the server -EK or OL
 	 * @param arrFromServer ArrayList
 	 */
 	public static void ekOrOl(ArrayList<String> arrFromServer) {
		ekOrOLFromConnect = arrFromServer;
	}

}
