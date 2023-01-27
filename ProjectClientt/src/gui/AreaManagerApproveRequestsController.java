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

/**
 * Controller for area manager
 * @author Michal and Or
 *
 */
public class AreaManagerApproveRequestsController implements Initializable{

    @FXML
    private Button backBtn;

    @FXML
    private ImageView ekrutLogoImage;

    @FXML
    private Label executionLabel;
    
    @FXML
    private Label feedbackLabel;

    @FXML
    private Button exitBtn;

    @FXML
    private TableColumn<CustomerToApprove, String> number;

    @FXML
    private TableColumn<CustomerToApprove, String> userName;
    
    @FXML
    private TableColumn<CustomerToApprove, String> status;
    
    @FXML
    private TableView<CustomerToApprove> table;
    
    //private ObservableList<CustomerToApprove> customers;
    
    private ObservableList<String> dataForComboBox;
    
    private static ArrayList<String> arrCustomersData;
    private static ArrayList<String> arrCustomersStatusUpdate;
    private static ArrayList<String> ekOrOLFromConnect;
	private static ArrayList<String> arrForUserData;
    
    @FXML
    private Button saveBtn;

    private double xoffset;
	private double yoffset;
	
	
	/**
	 * press back button from area manager page
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
	 * press exit button from area manager page
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
	 * start window to provide movement window
	 * @param primaryStage
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/AreaManagerApproveRequests.fxml"));

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
		ArrayList<String> msg1 = new ArrayList<>();
		msg1.add("getNotApproveCustomers");
		try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		dataForComboBox = FXCollections.observableArrayList("Approve","Not Aprrove");
		
		
		
		
		ObservableList<CustomerToApprove> customers = FXCollections.observableArrayList();
		
		for (int i = 0; i < arrCustomersData.size(); i++) { // inserting customers data to table
			customers.add(new CustomerToApprove(Integer.toString(i+1)
					,arrCustomersData.get(i),dataForComboBox));
		}
		
				
		
		//make sure the property value factory should be exactly same as the e.g getStudentId from your model class
		number.setCellValueFactory(new PropertyValueFactory<>("Number"));
		userName.setCellValueFactory(new PropertyValueFactory<>("UserName"));
		status.setCellValueFactory(new PropertyValueFactory<>("Status"));
		
		number.setStyle("-fx-alignment: CENTER;");
		userName.setStyle("-fx-alignment: CENTER;");
		status.setStyle("-fx-alignment: CENTER;");
		
		
        //add your data to the table here.
        table.setItems(customers);
	}
	

    
    @FXML
    /**
     * function for pressing save button
     * @param event
     */
    void pressSaveBtn(ActionEvent event) 
    {
    	
    	ArrayList<String> msg1 = new ArrayList<>();
		msg1.add("updateCustomerStatus");
		
		for(CustomerToApprove customer : table.getItems())
		{
			if((customer.getStatus()).getSelectionModel().getSelectedItem().equals("Approve"))
				msg1.add(customer.getUserName());

		}

		if(msg1.size()>1)
		{
			try {
				ClientUI.chat.accept(msg1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	if(arrCustomersStatusUpdate.get(0).equals("Success!")) {
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
     *  static function that get arrayList of data from server
     * @param massageFromServer
     */
    public static void getNotApproveCustomersData(ArrayList<String> massageFromServer) {
		arrCustomersData = massageFromServer;	
	}
    
    /**
     * static function that get arrayList of data from server
     * @param massageFromServer
     */
    public static void getInfoForApproveCustomersStatus(ArrayList<String> massageFromServer) {
    	arrCustomersStatusUpdate = massageFromServer;
		
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

