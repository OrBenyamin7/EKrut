package gui;
 
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
 
import client.ClientUI;
import common.MyFile;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
 
/**
 * The OrderTypeController class handles the user's choice of order type, machine, area, and delivery method.
 * It also contains fields for the various UI elements and variables used in the class.
 *
 */
public class OrderTypeController implements Initializable {
 
    /**
     * The ContinueBTN button is used to continue to the next screen.
     */
	@FXML
	private Button ContinueBTN;
 
	
    /**
     * The EnterCityName TextField is used to enter the CityName of the place for delivery.
     */
	@FXML
	private TextField EnterCityName;
 
	 /**
     * EnterHouseNumber TextField is used to enter the HouseNumber of the place for delivery
     */
	@FXML
	private TextField EnterHouseNumber;
 
	 /**
     * EnterReceiverPhoneNumber TextField is used to enter the ReceiverPhoneNumber of the order
     */
	@FXML
	private TextField EnterReceiverPhoneNumber;
 
	 /**
     * EnterStreetName TextField is used to enter the StreetName of the place for delivery
     */
	@FXML
	private TextField EnterStreetName;
 
    /**
     * ReturnBTN button is used to return to the previous screen
     */
	@FXML
	private Button ReturnBTN;
 
    /**
     * The chooseAreaDropDownList ComboBox is used to select the area for the order.
     */
	@FXML
	private ComboBox<String> chooseAreaDropDownList;
 
    /**
     * The anchorSupplyMethod AnchorPane is used to hold the delivery information fields.
     */
	
	@FXML
	private AnchorPane anchorSupplyMethod;
 
    /**
     * The chooseMachineNameDropDownList ComboBox is used to select the machine name.
     */
	@FXML
	private ComboBox<String> chooseMachineNameDropDownList;
 
    /**
     * The chooseOrderTypeDropDownList ComboBox is used to select the order type.
     */
	@FXML
	private ComboBox<String> chooseOrderTypeDropDownList;
	
    /**
     * The group ToggleGroup is used to group the delivery radio buttons.
     */
	@FXML
	private ToggleGroup group;
 
    /**
     * The radioDelivery RadioButton is used to select the delivery method.
     */
	@FXML
    private RadioButton radioDelivery;
 
    /**
     * The radioDelivery RadioButton is used to select the delivery method.
     */
    @FXML
    private RadioButton radioPickUp;
 
    /**
     * errorLabel Label is used to show the user that something is wrong with the action he tried to make
     */
	@FXML
	private ImageView pic;
 
    /**
     * errorLabel Label is used to show the user that something is wrong with the action he tried to make
     */
	@FXML
	private Label errorLabel;
 
	
    /*
     * xoffset,yoffset to handle drag of the window.
     */
	private double xoffset;
	private double yoffset;
	private boolean isdelivery = false;
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<MyFile> arrFromServerRetProducts;
	Map<String, ArrayList<String>> areaAndMachines = new HashMap<>();
	private boolean isAreaChosed = false;
	private boolean isMachineChosed = false;
	private static boolean isAMember;
	private String method="PickUp";
	private static ArrayList<String> arrFromServerRetUserData;
	private static ArrayList<String> ekOrOLFromConnect;
 
	 /**
     * This method sets the isAMember variable.
     *
     * @param type boolean value indicating whether the user is a member or not
     */
	public static void setIsAMember(boolean type) {
		isAMember = type;
	}
   
    /**
     * The pressradioDelivery method is triggered when the "Delivery" radio button is pressed.
     * It makes the "anchorSupplyMethod" element visible and sets the "method" variable to "Delivery" and "isdelivery" to true.
     * 
     * @param event the event that triggers the method
     */
	@FXML
    void pressradioDelivery(ActionEvent event) {
		anchorSupplyMethod.setVisible(true);
		method="Delivery";
		isdelivery=true;
    }
 
    /**
     * The pressradioPickUp method is triggered when the "PickUp" radio button is pressed.
     * It hides the "anchorSupplyMethod" element and sets the "method" variable to "PickUp" and "isdelivery" to false.
     * 
     * @param event the event that triggers the method
     */
    @FXML
    void pressradioPickUp(ActionEvent event) {
    	anchorSupplyMethod.setVisible(false);
    	method="PickUp";
    	isdelivery=false;
    }
    
    /**
     * The pressContinueBTN method is triggered when the "Continue" button is pressed.
     * It checks if the user has selected an area and machine, and if so, it retrieves data from the server and opens a new window.
     * 
     * @param event the event that triggers the method
     * @throws Exception 
     */
	@FXML
	void pressContinueBTN(ActionEvent event) throws Exception {
 
		if (isdelivery == false) {
			if (isAreaChosed && isMachineChosed) {
				String area = chooseAreaDropDownList.getSelectionModel().getSelectedItem().toString();
				String machineName = chooseMachineNameDropDownList.getSelectionModel().getSelectedItem().toString();
				try {
					
					if ((area == null || machineName == null)) {
						errorLabel.setText("All fileds have to be filled");
					}else {
						ArrayList<String> msg = new ArrayList<>();
						msg.add("getdataforcatalog");
						msg.add(area);
						msg.add(machineName);
						ClientUI.chat.accept(msg);
 
						((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
						Stage primaryStage = new Stage();
						primaryStage.initStyle(StageStyle.UNDECORATED);
						EKrutFinalMarketController ekfmc = new EKrutFinalMarketController();
						EKrutFinalMarketController.getProductsFromOrderType(arrFromServerRetProducts);
						EKrutFinalMarketController.setMacineName(machineName);
						EKrutFinalMarketController.setMethod(method);
						EKrutFinalMarketController.setArea(area);
						EKrutFinalMarketController.setIsAMember(isAMember);
						EKrutFinalMarketController.ekOrOl(ekOrOLFromConnect);
						EKrutFinalMarketController.getUserData(arrFromServerRetUserData);
						
						ekfmc.start(primaryStage);
 
					}
				}catch(Exception e){
					errorLabel.setText("All fileds have to be filled");
				}
 
 
 
			}
			else {
				errorLabel.setText("All fileds have to be filled");
			}
 
		} else {
			if (isAreaChosed && isMachineChosed) {
				String area = chooseAreaDropDownList.getSelectionModel().getSelectedItem().toString();
				String machineName = chooseMachineNameDropDownList.getSelectionModel().getSelectedItem().toString();
				String address = EnterCityName.getText() + " " + EnterStreetName.getText() + " "
						+ EnterHouseNumber.getText();
				try {
					
					if ((area == null || machineName == null )) {
						errorLabel.setText("All fileds have to be filled");
					}
					else if(EnterCityName.getText().trim().isEmpty()||EnterStreetName.getText().trim().isEmpty()||EnterHouseNumber.getText().trim().isEmpty()) {
						errorLabel.setText("All fileds have to be filled");
					}
					else {
						ArrayList<String> msg = new ArrayList<>();
						msg.add("getdataforcatalog");
						msg.add(area);
						msg.add(machineName);
						ClientUI.chat.accept(msg);
 
						((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
						Stage primaryStage = new Stage();
						primaryStage.initStyle(StageStyle.UNDECORATED);
						EKrutFinalMarketController ekfmc = new EKrutFinalMarketController();
						EKrutFinalMarketController.getProductsFromOrderType(arrFromServerRetProducts);
						EKrutFinalMarketController.setMacineName(machineName);
						EKrutFinalMarketController.getUserData(arrFromServerRetUserData);
						EKrutFinalMarketController.setAddress(address);
						EKrutFinalMarketController.setMethod(method);
						EKrutFinalMarketController.setArea(area);
						
						EKrutFinalMarketController.ekOrOl(ekOrOLFromConnect);
						EKrutFinalMarketController.setIsAMember(isAMember);
						ekfmc.start(primaryStage);
 
 
					}
				}catch(Exception e) {
					errorLabel.setText("All fileds have to be filled");
				}
 
			}
			else {
				errorLabel.setText("All fileds have to be filled");
			}
 
		}
	}

	/**
     * Static method to retrieve data from the server for ekOrOl.
     * @param ekOrOLFromConnect1
     */
	public static void ekOrOl(ArrayList<String> ekOrOLFromConnect1){
     	ekOrOLFromConnect = ekOrOLFromConnect1;
     }
 
    /**
     * Handles the functionality when the "Return" button is pressed. 
     * It returns to the previous screen if ServiceRepresentative to his HomePage.
     * if MarketingManager to his HomePage.
     *
     * @param event The event that triggers the method, in this case the "Return" button press.
     */
	@FXML
	void pressReturnBTN(ActionEvent event) throws Exception {
 
		Stage primaryStage = new Stage();
		String role = arrFromServerRetUserData.get(2);
		switch(role) {
		case "Customer":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			CustomerMainScreenController cmc = new CustomerMainScreenController();
			CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
			CustomerMainScreenController.getUserData(arrFromServerRetUserData);
			cmc.start(primaryStage);	
			break;
		case "AreaManager":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			AreaManagerHomePageController amgpc = new AreaManagerHomePageController();
			AreaManagerHomePageController.detailsOnUser(arrFromServerRetUserData);
			AreaManagerHomePageController.ekOrOl(ekOrOLFromConnect);

			amgpc.start(primaryStage);
			break;
		case "ServiceRepresentative":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			ServiceRepresentativeHomePageController srhpc = new ServiceRepresentativeHomePageController();
			ServiceRepresentativeHomePageController.getUserData(arrFromServerRetUserData);
			ServiceRepresentativeHomePageController.ekOrOl(ekOrOLFromConnect);
			srhpc.start(primaryStage);
			break;
		case "User":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			CustomerMainScreenController cmcu = new CustomerMainScreenController();
			CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
			CustomerMainScreenController.getUserData(arrFromServerRetUserData);
			cmcu.start(primaryStage);	
			break;
		case "MarketingManager":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			MarketingManagerHomePageController mmhpc = new MarketingManagerHomePageController();
			MarketingManagerHomePageController.getUserData(arrFromServerRetUserData);
			MarketingManagerHomePageController.ekOrOl(ekOrOLFromConnect);
			mmhpc.start(primaryStage);
			break;

		case "OperationWorker":
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			primaryStage.initStyle(StageStyle.UNDECORATED);
			OperationWorkerHomePageController owhpc = new OperationWorkerHomePageController();
			OperationWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
			OperationWorkerHomePageController.detailsOnUser(arrFromServerRetUserData);
			owhpc.start(primaryStage);
			break;
			
			case "MarketingAreaWorker":
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				primaryStage.initStyle(StageStyle.UNDECORATED);
				MarketingAreaWorkerHomePageController mawhpc = new MarketingAreaWorkerHomePageController();
				MarketingAreaWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
				MarketingAreaWorkerHomePageController.getUserData(arrFromServerRetUserData);
				
				mawhpc.start(primaryStage);
				break;
			case "DeliveryEmployee":
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				primaryStage.initStyle(StageStyle.UNDECORATED);
				DeliveryEmployeeHomePageController dehpc = new DeliveryEmployeeHomePageController();//new conroller
				DeliveryEmployeeHomePageController.ekOrOl(ekOrOLFromConnect);//transfer Ek Or Ol TO delivery employe
				DeliveryEmployeeHomePageController.getUserData(arrFromServerRetUserData);//Details about user
				dehpc.start(primaryStage);
				break;
			case "Ceo":
				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
				primaryStage.initStyle(StageStyle.UNDECORATED);
				CeoHomePageController chpc = new CeoHomePageController();
				CeoHomePageController.detailsOnUser(arrFromServerRetUserData);
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
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		ClientUI.chat.accept(msg);
		System.exit(1);
 
	}
 
	/*
	* start method is to load the FXML file, to move the window with the mouse and to show the data in the table view.
	* @param primaryStage the primary stage for this application
	* @throws Exception
	*/
	public void start(Stage primaryStage) throws Exception {
 
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/OrderType.fxml"));
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
	 * This method is triggered when the user chooses a machine name.
	 * It sets the "isMachineChosed" variable to true and populates the machine name dropdown list with the available machines for the chosen area.
	 * It also displays an error message if the user has not chosen an area first.
	 * 
	 * @param event the event that triggers the method
	 * @throws IOException
	 */
	@FXML
	void pressMachineNameDropDownList(MouseEvent event) throws IOException {
		if (isAreaChosed) {
			errorLabel.setText("");
			String Area = chooseAreaDropDownList.getSelectionModel().getSelectedItem().toString();
			ObservableList<String> chooseMachineNameFromDbArray = FXCollections.observableArrayList("");
			chooseMachineNameFromDbArray = FXCollections.observableArrayList(areaAndMachines.get(Area));
			chooseMachineNameDropDownList.setItems(chooseMachineNameFromDbArray);
			isMachineChosed = true;
		} else {
			errorLabel.setText("You Have To Choose Area First");
		}
 
	}
 
	/**
	 * This method is triggered when the user chooses an area.
	 * It sets the "isAreaChosed" variable to true.
	 * 
	 * @param event the event that triggers the method
	 */
	@FXML
	void pressAreaChoosed(MouseEvent event) {
		isAreaChosed = true;
	}
 
	/**
	* Static method to retrieve data about Products From DB from the server for the user.
	* @param arrFromServer an ArrayList of strings containing data from the server
	*/
	public static void getDataProductsFromDB(ArrayList<MyFile> arrFromServer) {
		arrFromServerRetProducts = arrFromServer;
	}
    /**
     * Static method to retrieve data from the server for the user.
     * @param arrFromServer an ArrayList of strings containing data from the server
     */
	public static void getDataAboutUser(ArrayList<String> arrFromServer) {
		arrFromServerRetUserData = arrFromServer;
	}
 
	/**
	* Static method to retrieve data about pickUP from the server for the user.
	* @param arrFromServer an ArrayList of strings containing data from the server
	*/
	public static void getDataFromDbPickUp(ArrayList<String> arrFromServer) {
		arrFromServerRet = arrFromServer;
	}
 
	 /**
	 * Initializes the controller class and sets drop down list f machines in that area that is set in this func
	 *
	 * @param url The location used to resolve relative paths for the root object, or
	 * {@code null} if the location is not known.
	 * @param rb The resources used to localize the root object, or {@code null} if the root
	 * object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		anchorSupplyMethod.setVisible(false);
		ArrayList<String> msg = new ArrayList<String>();
		msg.add("getAreaAndMachimes");
		try {
			ClientUI.chat.accept(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] arrOfMachines = arrFromServerRet.get(0).split(" ");
 
		for (int i = 0; i < arrOfMachines.length; i += 2) {
			if (areaAndMachines.containsKey(arrOfMachines[i])) {
				areaAndMachines.get(arrOfMachines[i]).add(arrOfMachines[i + 1]);
			} else {
				areaAndMachines.put(arrOfMachines[i], new ArrayList<String>());
				areaAndMachines.get(arrOfMachines[i]).add(arrOfMachines[i + 1]);
			}
		}
		ObservableList<String> chooseAreaDropDownArray = FXCollections.observableArrayList(areaAndMachines.keySet());
		chooseAreaDropDownList.setItems(chooseAreaDropDownArray);
 
	}
 
}