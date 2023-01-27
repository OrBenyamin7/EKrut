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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Class for area manager setting minimum level for specific machine
 * @author Michal and Or
 *
 */
public class SetMinimumLevelController implements Initializable{

    @FXML
    private Button backBtn;

    @FXML
    private ImageView ekrutLogoImage;

    @FXML
    private Label enterMinLevelLable;

    @FXML
    private Label errorDataLable;

    @FXML
    private Button exitBtn;

    @FXML
    private ImageView machineNameImg;


    @FXML
    private ImageView minLevelImage;

    @FXML
    private Label machineNameLable;

    @FXML
    private ImageView managmentAreaImage;

    @FXML
    private Label managmentAreaLable;

    @FXML
    private Label minLevelLable;

    @FXML
    private ComboBox<String> nameMachineComboBox;

    @FXML
    private Label specificAreaLable;

    @FXML
    private TextField specificMinLevelText;

    @FXML
    private Button updateReportBtn;
    
	private double xoffset;
	private double yoffset;
	private static ArrayList<String> arrFromServerRet; 
	private static ArrayList<String> arrMachineUpdateLevel;
	private static ArrayList<String> ekOrOLFromConnect;
	private static ArrayList<String> arrForUserData;
	
	
	private String selectedMachineName;
	private ObservableList<String> machineNumberList; // for numberMachineComboBox
	
	/**
	 * start window to provide movement window
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/SetMinimumLevelView.fxml"));

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
		ArrayList<String> msg1 = new ArrayList<>();
		msg1.add("getMachineNameForSetMinimum");
		try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		machineNumberList = FXCollections.observableArrayList();

		for (int i = 0; i < arrFromServerRet.size() - 1; i++) { // copying to machinenameList
			machineNumberList.add(arrFromServerRet.get(i));
		}
		nameMachineComboBox.setItems(machineNumberList); // show machines name for specific area
		specificAreaLable.setText(arrFromServerRet.get(arrFromServerRet.size() - 1)); // show the area label
	}
	
	@FXML
	/**
	 * function that get machine name from DB
	 * @param event
	 */
	void chooseNumberMachine(ActionEvent event) {
		selectedMachineName = nameMachineComboBox.getSelectionModel().getSelectedItem();// get selected machine
																							// name in c.b
	}
	
	/**
	 * function that get information on update made by the area manager
	 * @param massageFromServer
	 */
	public static void getMachineDataMinimumLevel(ArrayList<String> massageFromServer) {
		arrFromServerRet = massageFromServer;
	}
	
	@FXML
	/**
	 * press back button from current window
	 * @param event
	 * @throws Exception
	 */
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
     * function for press update button
     * @param event
     */
    void pressUpdate(ActionEvent event) {
		if (checkValidInsertData()){
			ArrayList<String> msg1 = new ArrayList<>();
			msg1.add("setMinimumLevelMachineDetails");
			msg1.add(selectedMachineName);
			msg1.add(specificMinLevelText.getText().trim());			
			try {
				ClientUI.chat.accept(msg1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(arrMachineUpdateLevel.get(0).equals("Success!")) {
				errorDataLable.setTextFill(Color.GREEN);
				errorDataLable.setText("\t\t\tUpdate Successful!");			
			}
			else {
				errorDataLable.setText("\t\t\tUpdate Failed!");	
			}
		}
    }
    
    /**
     * private function for checking valid input
     * @return
     */
    private boolean checkValidInsertData() {
    	if ((selectedMachineName==null)||(specificMinLevelText.getText().trim().isEmpty())){
			errorDataLable.setText("Inorder to set minimum level you must choose\n machine name and enter minimum level");
		}
    	else {
    		int intValue;
    		try {
    		    intValue = Integer.parseInt(specificMinLevelText.getText().trim());
    		    if (intValue>=0)
    		    	return true;
    		    else
    		    	errorDataLable.setText("\t\tEnter only positive integer number!");
    		} catch (NumberFormatException e) {
    			errorDataLable.setText("\t\tEnter only integer number!");
    		    return false;
    		}
    	}
    	return false;	
    }

    /**
     * function that get from DB information about update made by the area manager
     * @param massageFromServer
     */
	public static void getMachineUpdateMinimumLevelData(ArrayList<String> massageFromServer) {
		arrMachineUpdateLevel = massageFromServer;
		
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

