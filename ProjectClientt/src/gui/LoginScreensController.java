package gui;
 
import java.io.IOException;
import java.util.ArrayList;
 
import client.ChatClient;
import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
 
/**
 * The Controller class for the login screen. Handles the behavior of the login screen,
 * including handling button clicks, validating user input, and directing the user to the
 * appropriate screen based on their role.
 */

public class LoginScreensController {

	@FXML
    private Button exitBTN;
 
 
	@FXML
    private Label ErrorUserPass;
 
    @FXML
    private Button LogInbtn;
 
    @FXML
    private Button RegisterMamberbtn;
 
    @FXML
    private PasswordField password;
 
    @FXML
    private TextField username;
 
    private String userNameStr;
    private String passwordStr;
    private double xoffset;
	private double yoffset;
	private static ArrayList<String> arrFromServerRet;
	private static ArrayList<String> ekOrOLFromConnect;
	
	 /**
     * Called when the user clicks the login button. It retrieves the user name and password from the text fields
     * and sends them to the server for validation. If the validation is successful, the user is directed to the appropriate
     * screen based on their role (e.g. customer, area manager, service representative). If the validation fails,
     * an error message is displayed.
     *
     * @param event the event that triggered the button click
     * @throws Exception
     */
    @FXML
    void pressLogIn(ActionEvent event) throws Exception {
    	Stage primaryStage = new Stage();
    	ArrayList<String> msg = new ArrayList<>();
    	userNameStr = username.getText();
    	passwordStr = password.getText();
 
    	if(userNameStr.trim().isEmpty() || passwordStr.trim().isEmpty()) {
    		ErrorUserPass.setText("Invalid UserName Or Password !");
    	}
 
    	else {
    		msg.add("UserNameAndPasswordCheck");
    		msg.add(userNameStr);
    		msg.add(passwordStr);
    		
    		ClientUI.chat.accept(msg);

    		if(arrFromServerRet.get(0).equals("Error"))
			{
    			ErrorUserPass.setText("Username Or Password Incorrect");		
			}
    		else if(arrFromServerRet.get(3).equals("1")) {
	    		ErrorUserPass.setText("User Is Already Logged in");
	    	}
			else {
				switch(arrFromServerRet.get(2)) {
					case "Customer":
						((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
						primaryStage.initStyle(StageStyle.UNDECORATED);
						CustomerMainScreenController cmc = new CustomerMainScreenController();
						CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
						arrFromServerRet.add(userNameStr);
						CustomerMainScreenController.getUserData(arrFromServerRet);
						cmc.start(primaryStage);	
						break;
					case "AreaManager":
						((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
						primaryStage.initStyle(StageStyle.UNDECORATED);
						AreaManagerHomePageController amgpc = new AreaManagerHomePageController();
						arrFromServerRet.add(userNameStr);
						AreaManagerHomePageController.detailsOnUser(arrFromServerRet);
						AreaManagerHomePageController.ekOrOl(ekOrOLFromConnect);

						amgpc.start(primaryStage);
						break;
						
					case "ServiceRepresentative":
						((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
						primaryStage.initStyle(StageStyle.UNDECORATED);
						ServiceRepresentativeHomePageController srhpc = new ServiceRepresentativeHomePageController();
						arrFromServerRet.add(userNameStr);
						ServiceRepresentativeHomePageController.getUserData(arrFromServerRet);
						ServiceRepresentativeHomePageController.ekOrOl(ekOrOLFromConnect);
						
						srhpc.start(primaryStage);
						break;
					case "User":
						((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
						primaryStage.initStyle(StageStyle.UNDECORATED);
						CustomerMainScreenController cmcu = new CustomerMainScreenController();
						CustomerMainScreenController.ekOrOl(ekOrOLFromConnect);
						arrFromServerRet.add(userNameStr);
						CustomerMainScreenController.getUserData(arrFromServerRet);
						cmcu.start(primaryStage);	
						break;
					case "MarketingManager":
						((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
						primaryStage.initStyle(StageStyle.UNDECORATED);
						arrFromServerRet.add(userNameStr);
						MarketingManagerHomePageController mmhpc = new MarketingManagerHomePageController();
						MarketingManagerHomePageController.getUserData(arrFromServerRet);
						MarketingManagerHomePageController.ekOrOl(ekOrOLFromConnect);
						mmhpc.start(primaryStage);
						break;

					case "OperationWorker":
						((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
						primaryStage.initStyle(StageStyle.UNDECORATED);
						OperationWorkerHomePageController owhpc = new OperationWorkerHomePageController();
						arrFromServerRet.add(userNameStr);
						OperationWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
						OperationWorkerHomePageController.detailsOnUser(arrFromServerRet);
						owhpc.start(primaryStage);
						break;
						
						case "MarketingAreaWorker":
							((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
							primaryStage.initStyle(StageStyle.UNDECORATED);
							MarketingAreaWorkerHomePageController mawhpc = new MarketingAreaWorkerHomePageController();
							arrFromServerRet.add(userNameStr);
							MarketingAreaWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
							MarketingAreaWorkerHomePageController.getUserData(arrFromServerRet);
							
							mawhpc.start(primaryStage);
							break;
						case "DeliveryEmployee":
							((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
							primaryStage.initStyle(StageStyle.UNDECORATED);
							DeliveryEmployeeHomePageController dehpc = new DeliveryEmployeeHomePageController();//new conroller
							DeliveryEmployeeHomePageController.ekOrOl(ekOrOLFromConnect);//transfer Ek Or Ol TO delivery employe
							DeliveryEmployeeHomePageController.getUserData(arrFromServerRet);//Details about user
							dehpc.start(primaryStage);
							break;
						case "Ceo":
							((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
							primaryStage.initStyle(StageStyle.UNDECORATED);
							CeoHomePageController chpc = new CeoHomePageController();
							CeoHomePageController.detailsOnUser(arrFromServerRet);
							CeoHomePageController.ekOrOl(ekOrOLFromConnect);
								
							chpc.start(primaryStage);
							break;
 
 
 
				}
			}
    	}
    }
 
    /**
     * Event handler for the Exit button press. Closes the program.
     *
     * @param event the action event that triggered this handler
     */
    @FXML
    void exitBTNPress(ActionEvent event) throws IOException {
    	ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		ClientUI.chat.accept(msg);
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		System.exit(1);
    }
 
    
    /**
    * A method that receives an ArrayList of type String from the server and stores it in the arrFromServerRet instance variable.
    *
    * @param arrFromServer an ArrayList of type String received from the server
    */
    public static void subscriberDetails(ArrayList<String> arrFromServer){
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
     * Opens the Login Screen GUI.
     *
     * @param primaryStage the Stage to be shown
     * @throws Exception
     */
    public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/loginScreens.fxml"));
 
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
		primaryStage.setTitle("User Login");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
 
}