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
 * Controller for area manager report view, by choosing type of report (inventory, customers and orders), year, month and machine name
 * @author michal
 *
 */
public class AreaManagerReportViewController implements Initializable {
	@FXML
	private Button backBtn;

	@FXML
	private ImageView calendarImg;

	@FXML
	private ImageView ekrutLogoImage;

	@FXML
	private Button exitBtn;

	@FXML
	private ImageView machineNumberImg;

	@FXML
	private Label machineNumberLable;

	@FXML
	private ImageView managmentAreaImage;

	@FXML
	private Label managmentAreaLable;

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
	private Label specificAreaLable;

	@FXML
	private ImageView typeImg;

	@FXML
	private Label typeLable;

	@FXML
	private ComboBox<String> yearComboBox;
	
	@FXML
	private Label errorReportDataLable;

	private double xoffset;
	private double yoffset;
	
	private static ArrayList<String> arrForUserData;
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> arrReportData;
	private static ArrayList<String> arrCustomersReportData;
	private static ArrayList<String> arrInventoryReportData;
	private static ArrayList<String> ekOrOLFromConnect;

	private ObservableList<String> machineNameList; // for numberMachineComboBox
	private ObservableList<String> reportTypeList; // for reportTypeComboBox
	private ObservableList<String> yearList; // for yearComboBox
	private ObservableList<String> monthList; // for monthComboBox

	private String selectedMachineName;
	private String selectedMonth;
	private String selectedYear;
	private String selectedReportType;

	/**
	 * Start window to provide movement window
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/AreaManagerReportView.fxml"));

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

	/**
	 * Used to set the options for the machineNameComboBox, reportTypeComboBox, yearComboBox and monthComboBox.
		It also sets the text for the specificAreaLabel
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<String> msg1 = new ArrayList<>();
		msg1.add("getMachineName");
		try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		machineNameList = FXCollections.observableArrayList();

		for (int i = 0; i < arrFromServerRet.size() - 1; i++) { // copying to machineNumberList
			machineNameList.add(arrFromServerRet.get(i));
		}
		nameMachineComboBox.setItems(machineNameList); // show machines number for specific area
		reportTypeList = FXCollections.observableArrayList("Orders", "Inventory", "Customers");
		reportTypeComboBox.setItems(reportTypeList);
		yearList = FXCollections.observableArrayList("2022", "2021", "2020", "2019", "2018", "2017", "2016");
		yearComboBox.setItems(yearList);
		monthList = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
		monthComboBox.setItems(monthList);
		specificAreaLable.setText(arrFromServerRet.get(arrFromServerRet.size() - 1)); // show the area label
	}

	/**
	 * Retrieve the machine data from the server
	 * @param massageFromServer
	 */
	public static void getMachineData(ArrayList<String> massageFromServer) {
		arrFromServerRet = massageFromServer;
	}
	
	/**
	 * Get selected month in combo box
	 * @param event
	 */
	@FXML
	void chooseMonth(ActionEvent event) {
		selectedMonth = monthComboBox.getSelectionModel().getSelectedItem();
	}

	/**
	 * Get selected machine number in combo box
	 * @param event
	 */
	@FXML
	void chooseMachine(ActionEvent event) {
		selectedMachineName = nameMachineComboBox.getSelectionModel().getSelectedItem();
	}

	/**
	 * Get selected report type in combo box
	 * @param event
	 */
	@FXML
	void chooseTypeReport(ActionEvent event) {
		selectedReportType = reportTypeComboBox.getSelectionModel().getSelectedItem();
		
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
		selectedYear = yearComboBox.getSelectionModel().getSelectedItem();
	}
	
	
	/**
	 *  Press back button from area manager page
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void pressBackBtn(ActionEvent event) throws Exception {
		ArrayList<String> msg = new ArrayList<>();
		msg.add("getUserDataAreaManager");
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ClientUI.chat.accept(msg);
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		AreaManagerHomePageController amhpc = new AreaManagerHomePageController();
		AreaManagerHomePageController.detailsOnUser(arrForUserData);
		AreaManagerHomePageController.ekOrOl(ekOrOLFromConnect);
		amhpc.start(primaryStage);
	}

	/**
	 * Press exit button from area manager page
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
	 * Show next window for orders report - customers, inventory and orders
	 * @param event
	 * @throws Exception
	 */
	@FXML
	void pressShowReport(ActionEvent event) throws Exception {
		if(checkValidReportData()) {
			ArrayList<String> msg = new ArrayList<>();
			switch(selectedReportType) {
				case "Orders":
					msg.add("getOrderReportDetails");
					break;
				case "Customers":
					msg.add("getCustomersReportDetails");
					break;
				case "Inventory":
					msg.add("getInventoryReportDetails");
					break;
			}
			if(selectedReportType.equals("Inventory"))
				msg.add(selectedMachineName);
			msg.add(specificAreaLable.getText());
			msg.add(selectedMonth);
			msg.add(selectedYear);
			ClientUI.chat.accept(msg);
			if(checkingErrorMsg()) //showing report view if the user put current details
			{ 
				Stage primaryStage = new Stage();
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				primaryStage.initStyle(StageStyle.UNDECORATED);
				switch(selectedReportType) {
				case "Orders":
					OrderReportViewController orvc = new OrderReportViewController();
					OrderReportViewController.getOrderReportDetails(arrReportData);
					OrderReportViewController.getUserData(arrForUserData);
					OrderReportViewController.ekOrOl(ekOrOLFromConnect);
					orvc.start(primaryStage);
					break;
				case "Customers":
					ClientHistogramReportViewController chrvc = new ClientHistogramReportViewController();
					ClientHistogramReportViewController.getCustomersReportDetails(arrCustomersReportData);
					ClientHistogramReportViewController.getUserData(arrForUserData);
					ClientHistogramReportViewController.ekOrOl(ekOrOLFromConnect);
					chrvc.start(primaryStage);
					break;
				case "Inventory":
					InventoryReportViewController irvc = new InventoryReportViewController();
					InventoryReportViewController.getInventoryReportDetails(arrInventoryReportData);
					InventoryReportViewController.getUserData(arrForUserData);
					InventoryReportViewController.ekOrOl(ekOrOLFromConnect);
					irvc.start(primaryStage);
					break;
				}

			}
		}
	}	
	
	/**
	 *  Used to check if the report data is valid. If the data is invalid, it sets the text for the errorReportDataLabel and returns false.
	 * @return
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
	 * @return
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
			errorReportDataLable.setText("Inorder to view report you must choose year\n month, machine name and report type");
			return false;
		}
		return true;
	}
	
	/**
	 * Used to retrieve the order report data from the server.
	 * @param massageFromServer
	 */
	public static void getOrderReportData(ArrayList<String> massageFromServer) {
		arrReportData = massageFromServer;	
	}

	/**
	 * Used to retrieve the customers report data from the server.
	 * @param massageFromServer
	 */
	public static void getCustomersReportData(ArrayList<String> massageFromServer) {
		arrCustomersReportData = massageFromServer;
	}
	
	/**
	 * Used to retrieve the inventory report data from the server.
	 * @param massageFromServer
	 */
	public static void getInventoryReportData(ArrayList<String> massageFromServer) {
		arrInventoryReportData = massageFromServer;
	}
	
	/**
	 * Used to retrieve the user data from the server.
	 * @param arrFromServer
	 */
	public static void getUserData(ArrayList<String> arrFromServer) {
		arrForUserData = arrFromServer;
	}
	
	/**
	 * function that save arrayList from server for ek or ol 
	 * @param arrFromServer
	 */
	public static void ekOrOl(ArrayList<String> arrFromServer) {
		ekOrOLFromConnect = arrFromServer;
	}
}
