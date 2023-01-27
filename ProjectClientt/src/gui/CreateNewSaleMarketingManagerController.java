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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A controller class for the Create New Sale page for Marketing Manager user. This class handles the
 * user interface logic for creating a new sale, including input validation and sending the necessary
 * information to the server to create the sale.
 */

public class CreateNewSaleMarketingManagerController implements Initializable {

    @FXML
    private Button ReturnBTN;

    @FXML
    private ComboBox<String> chooseAreaDropDownList;

    @FXML
    private Button createBtn;

    @FXML
    private TextField discriptionLabel;

    @FXML
    private Label erroeLabel;

    @FXML
    private Button exitBTN;

    @FXML
    private TextField saleLevel;
    
    private double xoffset;
   	private double yoffset;
   	private static ArrayList<String> arrFromServerRet;
   	private static ArrayList<String> ekOrOLFromConnect;
   	
    /**
     * Receives the user data from the previous controller.
     *
     * @param arrFromServer an ArrayList containing the user data
     */
    public static void getUserData(ArrayList<String> arrFromServer){
 		arrFromServerRet = arrFromServer;
 	}
    /**
     * Receives the EK or OL status from the previous controller.
     *
     * @param ekOrOLFromConnect1 an ArrayList containing the EK or OL status
     */
 	 public static void ekOrOl(ArrayList<String> ekOrOLFromConnect1){
     	ekOrOLFromConnect = ekOrOLFromConnect1;
     }

 	/**
      * Event handler for the Create button press. Validates the input fields and sends a message to the
      * server to create a new sale with the specified information.
      *
      * @param event the action event that triggered this handler
      * @throws IOException if an error occurs while sending the message to the server
      */
    @FXML
    void pressCreateBTN(ActionEvent event) throws IOException {
    	erroeLabel.setText("");
    	try {
    		String area = chooseAreaDropDownList.getSelectionModel().getSelectedItem().toString();
        	String discountLevel = saleLevel.getText();
        	String discription = discriptionLabel.getText();
        	ArrayList<String> msg = new ArrayList<>();
        	int checkSpaces = discription.indexOf(" ");
        	if(checkSpaces != -1 || discription == "") {
        		erroeLabel.setText("No Spaces Allowed!");
        	}else if(area == null || discountLevel == "") {
        		erroeLabel.setText("All fileds have to be filled");
        	}else if(Integer.parseInt(discountLevel)>=100 || Integer.parseInt(discountLevel)<=0 ) {
        		erroeLabel.setText("Discount is out of range...");
        	}else {
        		msg.add("AddNewSaleFromMarketingManager");
        		msg.add(discountLevel);
        		msg.add(area);
        		msg.add(discription);
        		ClientUI.chat.accept(msg);
        	}
    	}catch(NullPointerException e) {
    		erroeLabel.setText("You Have To Choose Area First!");
    	}
    	
    }
    /**
     * Event handler for the Return button press. Returns the user to the Marketing Manager Home page.
     *
     * @param event the action event that triggered this handler
     * @throws Exception if an error occurs while loading the next page
     */
    @FXML
    void pressReturnBTN(ActionEvent event) throws Exception {
    	Stage primaryStage;
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		MarketingManagerHomePageController mmhpc = new MarketingManagerHomePageController();
		MarketingManagerHomePageController.getUserData(arrFromServerRet);
		MarketingManagerHomePageController.ekOrOl(ekOrOLFromConnect);
		mmhpc.start(primaryStage);

    }
    
    /**
     * Event handler for the Exit button press. Closes the program.
     *
     * @param event the action event that triggered this handler
     */
    @FXML
    void pressexitBTN(ActionEvent event) throws IOException {
    	ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		ClientUI.chat.accept(msg);
		System.exit(1);

    }

    /**
     * Initializes the controller. Populates the area dropdown list with the available areas.*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> chooseAreaDropDownArray = FXCollections.observableArrayList("North","South","UAE");
		chooseAreaDropDownList.setItems(chooseAreaDropDownArray);

		
	}
	
	/**
     * Opens the Create New Sale Marketing Manager GUI.
     *
     * @param primaryStage the Stage to be shown
     * @throws Exception
     */
	public void start(Stage primaryStage) throws Exception {
    	
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/AddNewSale.fxml"));
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

}
