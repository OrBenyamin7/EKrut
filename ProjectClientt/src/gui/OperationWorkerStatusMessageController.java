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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.CustomerToApprove;
import logic.MessageToApprove;

/**
 * Class for operation worker for approving status message window
 * @author Michal and Or
 *
 */
public class OperationWorkerStatusMessageController implements Initializable{

    @FXML
    private Button backBtn;

    @FXML
    private ImageView ekrutLogoImage;

    @FXML
    private Button exitBtn;

    @FXML
    private Label feedbackLabel;

    @FXML
    private TableColumn<MessageToApprove, String> message;

    @FXML
    private TableColumn<MessageToApprove, String>  number;

    @FXML
    private Button saveBtn;

    @FXML
    private TableColumn<MessageToApprove, String>  status;

    @FXML
    private TableView<MessageToApprove> table;

    @FXML
    private Label updateStatusLabel;
    
    private ObservableList<String> dataForComboBox;
    private static ArrayList<String> arrFromServerRet;
    private static ArrayList<String> arrMessageStatusUpdate;
    
    private static ArrayList<String> ekOrOLFromConnect;
	private static ArrayList<String> detailsOnUserArray;
    
    
    private double xoffset;
	private double yoffset;
    
	/**
	 * start window to provide movement window
	 * @param primaryStage
	 * @throws Exception
	 */
 	public void start(Stage primaryStage) throws Exception {
 		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/OperationWorkerStatusMessage.fxml"));

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
	public void initialize(URL location, ResourceBundle resources) 
	{
 		
		
		dataForComboBox = FXCollections.observableArrayList("in process","Done");
		
		
		for (int i = 0; i < arrFromServerRet.size(); i++)
			System.out.println(arrFromServerRet.get(i));
		
		
		ObservableList<MessageToApprove> messageToShow = FXCollections.observableArrayList();
		
		for (int i = 0; i < arrFromServerRet.size(); i++) { // inserting message data to table
			messageToShow.add(new MessageToApprove(Integer.toString(i+1)
					,arrFromServerRet.get(i),dataForComboBox));
		}
		
				
		
		
		
		number.setCellValueFactory(new PropertyValueFactory<>("Number"));
		message.setCellValueFactory(new PropertyValueFactory<>("Message"));
		status.setCellValueFactory(new PropertyValueFactory<>("Status"));
		
		number.setStyle("-fx-alignment: CENTER;");
		message.setStyle("-fx-alignment: CENTER;");
		status.setStyle("-fx-alignment: CENTER;");
		
        
        table.setItems(messageToShow);
        
	}
 	
 	/**
 	 * function that get arrayList with data contains information about the action made by the operation worker
 	 * @param arrFromServer
 	 */
 	public static void getMessages(ArrayList<String> arrFromServer) {
		arrFromServerRet = arrFromServer;
	}
 	

  	@FXML
  	/**
  	 * press back button from current window of approving status messages
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

    // press exit button from area manager page
 	@FXML
 	/**
 	 * press exit button from current window of approving status messages
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
     * pressing save button to save the changes made by the operation worker
     * @param event
     */
    void pressSaveBtn(ActionEvent event) 
    {
    	
    	ArrayList<String> msg1 = new ArrayList<>();
		msg1.add("updateOperationWorkerMessageStatus");
		
		for(MessageToApprove customer : table.getItems())
		{
			if((customer.getStatus()).getSelectionModel().getSelectedItem().equals("Done"))
				msg1.add(customer.getMessage());

		}
		if(msg1.size()>1)
		{
			try {
				ClientUI.chat.accept(msg1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	if(arrMessageStatusUpdate.get(0).equals("Success!")) {
	    		feedbackLabel.setTextFill(Color.GREEN);
	    		feedbackLabel.setText("updated Successfully!");
	    		return;
	    			
			}
			feedbackLabel.setText("update Failed!");	
		}
		feedbackLabel.setTextFill(Color.RED);
		feedbackLabel.setText("update Failed!");
    	
    }
    
    /**
     * function that get inforamtion about the changes in the DB made by the operation worker
     * @param massageFromServer
     */
    public static void getUpdateFromDBAboutMessage(ArrayList<String> massageFromServer) {
    	arrMessageStatusUpdate = massageFromServer;
		
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