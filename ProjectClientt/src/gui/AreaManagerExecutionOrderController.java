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
 * Controller for area manager execution order
 * @author Michal and Or
 *
 */
public class AreaManagerExecutionOrderController implements Initializable{

    @FXML
    private Button backBtn;

    @FXML
    private ImageView ekrutLogoImage;

    @FXML
    private TextField executionMsgTextField;

    @FXML
    private Button exitBtn;
    
    @FXML
    private Label feedbackLabel;

    @FXML
    private ComboBox<String> operationWorkerComboBox;

    @FXML
    private ImageView operationWorkerImg;

    @FXML
    private Label operationWorkerLabel;

    @FXML
    private Label executionLabel;

    @FXML
    private Button sendBtn;
    
    private double xoffset;
	private double yoffset;
	
	private String selectedworker;
	
	private static ArrayList<String> arrExecutionOrder;
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> ekOrOLFromConnect;
	private static ArrayList<String> arrForUserData;
	
	private ObservableList<String> operationWorkerList; // for numberMachineComboBox


	
	/**
	 * start window to provide movement window
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/AreaManagerExecutionOrder.fxml"));

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
		msg1.add("getOperationWorkersName");
		try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		operationWorkerList = FXCollections.observableArrayList();

		for (int i = 0; i < arrFromServerRet.size(); i++) { // copying to operationWorkerList
			operationWorkerList.add(arrFromServerRet.get(i));
		}
		operationWorkerComboBox.setItems(operationWorkerList); // show machines number for specific area
		
	}

    @FXML
    /**
     * function that save choosen worker
     * @param event
     */
    void chooseWorker(ActionEvent event) {
    	selectedworker = operationWorkerComboBox.getSelectionModel().getSelectedItem();// get selected worker
    }

	@FXML
	/**
	 * press back button from area manager page
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
     * function that work when pressing send button
     * @param event
     */
    void pressSendButton(ActionEvent event) {
    	
    	if(checkValidInsertData())
    	{
    		ArrayList<String> msg1 = new ArrayList<>();
			msg1.add("sendExecutionOrder");
			msg1.add(selectedworker);
			msg1.add(executionMsgTextField.getText().trim());			
			try {
				ClientUI.chat.accept(msg1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    		if(arrExecutionOrder.get(0).equals("Success!")) {
    			feedbackLabel.setTextFill(Color.GREEN);
    			feedbackLabel.setText("Sent Successfully!");			
			}
			else {
				feedbackLabel.setTextFill(Color.RED);
				feedbackLabel.setText("Sent Failed!");	
			}
    	}		

    }
    
    
    /**
     * private function for checking valid input
     * @return
     */
    private boolean checkValidInsertData() {
    	if ((operationWorkerComboBox==null)||(executionMsgTextField.getText().trim().isEmpty())){
    		feedbackLabel.setText("Inorder to send execution order you must\n choose operation worker and insert message");
    		feedbackLabel.setTextFill(Color.RED);
    		return false;
    	}
    	return true;	
    }
    
    /**
     * function that get arrayList from server
     * @param massageFromServer
     */
    public static void getOperationWorkersName(ArrayList<String> massageFromServer) {
		arrFromServerRet = massageFromServer;
	}
    
    /**
     * function that get arrayList from server
     * @param massageFromServer
     */
    public static void getInfoForSendingExecutionOrder(ArrayList<String> massageFromServer) {
		arrExecutionOrder = massageFromServer;
		
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

