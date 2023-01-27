package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import client.ClientController;
import client.ClientUI;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * ConnectController class is a controller class for the Connect FXML file.
 * It handles the actions of the buttons and ComboBoxes in the Connect screen.
 *
 */
public class ConnectController implements Initializable {
	// Class variables *************************************************
	@FXML
	private Button connectEK_btn1;
	@FXML
    private TextField ip_server1;

	@FXML
	private Button connectOL_btn;

	@FXML
	private Button exit_btn;

	@FXML
	private AnchorPane anchorEKFAST;

	@FXML
	private AnchorPane anchorOL;

	@FXML
	private ToggleGroup group;

	@FXML
	private Button fastConnectbtn;

	@FXML
	private TextField ip_server;

	@FXML
	private TextField machineName;
	@FXML
	private Label ErrorLabel;

	@FXML
	private RadioButton radioEK;

	@FXML
	private RadioButton radioOL;

	@FXML
	private ComboBox<String> chooseAreaDropDownList;

	@FXML
	private ComboBox<String> chooseMachineNameDropDownList;

	private static ArrayList<String> arrFromServerRet;
	private double xoffset;

	private double yoffset;
	private boolean isAreaChosed = false;
	private boolean isMachineChosed = false;
	private boolean isConnected = false;
	// ******************************************************************

	/**
     * This method is used to handle the pressConnectEK button event
     * If the user did not choose machine name and area, an error message will be shown
     * Else, it will hide the primary window and open the login screen with the selected machine name and area.
     *
     * @param event the event of the pressConnectEK button.
     * @throws Exception when an error occurs while loading the FXML file.
     */
	@FXML
	void pressConnectEK(ActionEvent event) throws Exception {
		
		if (!(isAreaChosed && isMachineChosed)) {
			ErrorLabel.setText("You Must Enter Machine Name");
		}
		else {
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();
			primaryStage.initStyle(StageStyle.UNDECORATED);
			ClientUI.setMyClientchat(new ClientController(ip_server.getText(), 5555));
			LoginScreensController LSC = new LoginScreensController();
			ClientUI.getMyClientchat().openConnection();
			ArrayList<String> msg = new ArrayList<>();
			msg.add("EK");
			msg.add(chooseMachineNameDropDownList.getSelectionModel().getSelectedItem().toString());
			msg.add(chooseAreaDropDownList.getSelectionModel().getSelectedItem().toString());
			LoginScreensController.ekOrOl(msg);
			LSC.start(primaryStage);
		}
		
	}
	
	/**
	 * pressConnectOL method is called when the connect button is pressed in the OL (Online Login) tab.
	 * It hides the primary window and opens a new stage for the login screen.
	 * It also creates a new ClientController object and opens a connection with the server using the IP address entered in the text field.
	 * It sends a message to the server indicating that the user is trying to login using the OL method.
	 *
	 * @param event the event that triggers the method (pressing the connect button)
	 * @throws Exception if any error occurs while creating or showing the new stage
	 */
	@FXML
	void pressConnectOL(ActionEvent event) throws Exception {
		
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		ClientUI.setMyClientchat(new ClientController(ip_server1.getText(), 5555));
		LoginScreensController LSC = new LoginScreensController();
		ClientUI.getMyClientchat().openConnection();
		ArrayList<String> msg = new ArrayList<>();
		msg.add("OL");
		LoginScreensController.ekOrOl(msg);
		LSC.start(primaryStage);

	}
	
	/**
	 * pressradioEK method is called when the EK radio button is selected.
	 * It makes the anchor pane for EK visible and hides the anchor pane for OL.
	 *
	 * @param event the event that triggers the method (selecting the EK radio button)
	 */
	@FXML
	void pressradioEK(ActionEvent event) {

		anchorEKFAST.setVisible(true);
		anchorOL.setVisible(false);
	}
	
	/**
	 * pressradioOL method is called when the OL radio button is selected.
	 * It makes the anchor pane for OL visible and hides the anchor pane for EK.
	 *
	 * @param event the event that triggers the method (selecting the OL radio button)
	 */
	@FXML
	void pressradioOL(ActionEvent event) {
		anchorOL.setVisible(true);
		anchorEKFAST.setVisible(false);
	}
	
	/**
	 * subscriberDetails method is used to store the details of a subscriber received from the server.
	 *
	 * @param arrFromServer an array list of strings containing the details of the subscriber
	 */
	public static void subscriberDetails(ArrayList<String> arrFromServer) {
		arrFromServerRet = arrFromServer;
	}

	
	/**
	 * pressfastConnectbtn method is called when the fast connect button is pressed in the EK.
	 * It sends a message to the server indicating that the user is trying to login using the fast connect method.
	 * If the user is already logged in, it displays an error message. 
	 * Else, it opens a new stage for the customer main screen.
	 *
	 * @param event the event that triggers the method (pressing the fast connect button)
	 * @throws Exception if any error occurs while creating or showing the new stage
	 */
	@FXML
	void pressfastConnectbtn(ActionEvent event) throws Exception {
		ArrayList<String> msg = new ArrayList<>();
		if (!(isAreaChosed && isMachineChosed)) {
			ErrorLabel.setText("You Must Enter Machine Name");
		}

		else {
			if (!isConnected) {
				ClientUI.setMyClientchat(new ClientController(ip_server.getText(), 5555));
				ClientUI.getMyClientchat().openConnection();
				isConnected = true;
			}
			msg.add("Fastconnect");
			msg.add("memberfast");
			msg.add("123456");
			ClientUI.chat.accept(msg);
			
			arrFromServerRet.add("memberfast");
			
			if (arrFromServerRet.get(3).equals("1")) {
				ErrorLabel.setText("User Is Already Logged in");
			} else {
				((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
				Stage primaryStage = new Stage();
				primaryStage.initStyle(StageStyle.UNDECORATED);
				ArrayList<String> msg1 = new ArrayList<>();
				msg1.add("EK");
				msg1.add(chooseMachineNameDropDownList.getSelectionModel().getSelectedItem().toString());
				msg1.add(chooseAreaDropDownList.getSelectionModel().getSelectedItem().toString());
				CustomerMainScreenController CMSC = new CustomerMainScreenController();
				CustomerMainScreenController.ekOrOl(msg1);
				CustomerMainScreenController.getUserData(arrFromServerRet);
				
				CMSC.start(primaryStage);
			}

		}
	}
	
	/**
	pressMachineNameDropDownList is a method that is triggered when the user clicks on the "machine name" dropdown list.
	It populates the dropdown list with different machine names based on the area that the user has selected.
	@param event a MouseEvent object that contains information about the user's mouse interaction with the GUI element.
	*/
	@FXML
	void pressMachineNameDropDownList(MouseEvent event) {
		ObservableList<String> chooseMachineNameFromDbArray = FXCollections.observableArrayList("");
		if (isAreaChosed &&chooseAreaDropDownList.getSelectionModel().getSelectedItem()!=null) {
			String area = chooseAreaDropDownList.getSelectionModel().getSelectedItem().toString();
			switch (area) {
			case "UAE":
				chooseMachineNameFromDbArray = FXCollections.observableArrayList("Hilton", "Stadium", "Airport",
						"BurjKhalifa");
				chooseMachineNameDropDownList.setItems(chooseMachineNameFromDbArray);
				isMachineChosed=true;
				break;
			case "South":
				chooseMachineNameFromDbArray = FXCollections.observableArrayList("Malha", "Dizengoff", "Sarona");
				chooseMachineNameDropDownList.setItems(chooseMachineNameFromDbArray);
				isMachineChosed=true;
				break;

			case "North":
				chooseMachineNameFromDbArray = FXCollections.observableArrayList("Grand", "Park", "Winery");
				chooseMachineNameDropDownList.setItems(chooseMachineNameFromDbArray);
				isMachineChosed=true;
				break;
			}
		}
	}
	
	
	/**
	pressAreaChoosed is a method that is triggered when the user clicks on the "area" dropdown list.
	It sets the isAreaChosed flag to true, indicating that the user has selected an area.
	@param event a MouseEvent object that contains information about the user's mouse interaction with the GUI element.
	*/
	@FXML
	void pressAreaChoosed(MouseEvent  event) {
		isAreaChosed = true;
	}
	
	/**
     * Handles the event where the exit button is pressed.
     * Closes the current window.
     * @param event the ActionEvent that occurred
     */
	@FXML
	void prees_exit(ActionEvent event) throws IOException {

		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		System.exit(1);

	}

	/**
	 * open connect gui
	 * 
	 * @param event
	 */
	public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/connect.fxml"));

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
		primaryStage.setTitle("Connect");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
	
	/**
	 * The initialize method.
	 * It sets the visibility of the anchorOL and anchorEKFAST anchors to false, and populates the chooseAreaDropDownList
	 * with the options "UAE", "South", and "North".
	 *
	 * @param location the location of the FXML file
	 * @param resources the resources associated with the FXML file
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		anchorOL.setVisible(false);
		anchorEKFAST.setVisible(false);
		ObservableList<String> chooseAreaDropDownArray = FXCollections.observableArrayList("UAE", "South", "North");
		chooseAreaDropDownList.setItems(chooseAreaDropDownArray);

	}
}
