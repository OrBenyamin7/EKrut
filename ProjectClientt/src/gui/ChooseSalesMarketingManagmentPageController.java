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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.Sale;

/**
* Controller class for the ChooseSalesMarketingManagmentPage.fxml file.
* Handles events and updates the GUI elements in the associated user interface.
*/

public class ChooseSalesMarketingManagmentPageController implements Initializable {

	
	// GUI element fields, annotated with @FXML to indicate they are used in the associated FXML file
	@FXML
	private TextField idLabel;
    @FXML
    private Button activeBTN;
    @FXML
    private Button deleteBTN;
    @FXML
    private Button disactiveBTN;
    @FXML
    private Label erroeLabel;	
    @FXML
    private Button ReturnBTN;
    @FXML
    private Button exitBTN;
    @FXML
    private TableView<Sale> salesTable;
    private double xoffset;
   	private double yoffset;
   	private static ArrayList<String> arrFromServerRet;
   	private static ArrayList<String> ekOrOLFromConnect;
   	private static ArrayList<Sale> salesFromDb = new ArrayList<>();
   	static ObservableList<Sale> list = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Sale, String> areaCol;
    @FXML
    private TableColumn<Sale, String> disCol;
    @FXML
    private TableColumn<Sale, String> discountCol;
    @FXML
    private TableColumn<Sale, String> isActieCol;
    @FXML
    private TableColumn<Sale, String> saleIdCol;
    
   
    
    /**
     * Sets the static field arrFromServerRet to the given ArrayList of strings.
     * @param arrFromServer the ArrayList to set arrFromServerRet
     */
    
    public static void getUserData(ArrayList<String> arrFromServer){
 		arrFromServerRet = arrFromServer;
 	}
    /**
     * Sets the static field ekOrOLFromConnect to the given ArrayList of strings.
     * @param ekOrOLFromConnect1 the ArrayList to set ekOrOLFromConnect to
     */

 	 public static void ekOrOl(ArrayList<String> ekOrOLFromConnect1){
     	ekOrOLFromConnect = ekOrOLFromConnect1;
     }
 	 
 	 
     /**
      * Sets the static field salesFromDb to the given ArrayList of Sale objects.
      * @param sales the ArrayList to set salesFromDb to
      */	
     public static void setSalesList(ArrayList<String> arrFromServer){    	
     	salesFromDb.clear();
     	arrFromServer.remove(0);
        	String sales[] = ((String) arrFromServer.get(0)).split(" ");
        	for(int i=0;i<sales.length;i+=5) {
        		salesFromDb.add(new Sale(sales[i],sales[i+1],sales[i+2],sales[i+3],sales[i+4]));
        		
        	}
     	
  	}
     
     /**
      * Initializes the controller class and its associated GUI elements.
      * This method is automatically called when the associated FXML file is loaded.
      * @param location the location of the FXML file
      * @param resources the resource bundle for the FXML file
      */
 	@Override
 	public void initialize(URL arg0, ResourceBundle arg1) {
 		ArrayList<String> msg = new ArrayList<>();
 		msg.add("getSalesFromDbToMarketingManager");
 		
 		try {
 			ClientUI.chat.accept(msg);
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		
 		list.addAll(salesFromDb);
 		areaCol.setCellValueFactory(new PropertyValueFactory<Sale, String>("area"));
 		disCol.setCellValueFactory(new PropertyValueFactory<Sale, String>("discription"));
 		discountCol.setCellValueFactory(new PropertyValueFactory<Sale, String>("discountLevel"));
 		isActieCol.setCellValueFactory(new PropertyValueFactory<Sale, String>("isActive"));
 		saleIdCol.setCellValueFactory(new PropertyValueFactory<Sale, String>("saleNumber"));
 		salesTable.setItems(list);
 		
 	}


 	/**
     * Handles the event where the active button is pressed.
     * Sends a message to the server to set the sale with the specified ID to active, and updates the table view.
     * @param event the ActionEvent that occurred
     * @throws IOException if an I/O error occurs
     */
    @FXML
    void pressActiveBTN(ActionEvent event) throws IOException {
    	boolean idFound = false;
    	erroeLabel.setText("");
    	String idNumberToActivate = idLabel.getText();
    	ArrayList<String> msg = new ArrayList<>();
    	if(idNumberToActivate.equals("")) {
    		erroeLabel.setText("No Id Inserted");
    	}else {
    		for(Sale sale : salesFromDb) {
    			if(sale.getSaleNumber().equals(idNumberToActivate)) {
    				msg.add("ChangeStatusOfOrDeleteSale");
    				msg.add("SetActive");
    				msg.add(idNumberToActivate);
    				sale.setIsActive("1");
    				list.setAll(salesFromDb);
    				ClientUI.chat.accept(msg);
    				idFound = true;
    				break;
    			}
    		}
    		if(!idFound) {
    			erroeLabel.setText("id Not Found");
    		}
    	}
    }

    /**
     * Handles the event where the delete button is pressed.
     * Sends a message to the server to delete the sale with the specified ID, and updates the table view.
     * @param event the ActionEvent that occurred
     * @throws IOException if an I/O error occurs
     */
    @FXML
    void pressDeleteBTN(ActionEvent event) throws IOException {
    	boolean idFound = false;
    	erroeLabel.setText("");
    	String idNumberToDelete = idLabel.getText();
    	if(idNumberToDelete.equals("")) {
    		erroeLabel.setText("No Id Inserted");
    	}else {
    		ArrayList<String> msg = new ArrayList<>();
        	for(Sale sale : salesFromDb) {
        		if(sale.getSaleNumber().equals(idNumberToDelete)) {
        			msg.add("ChangeStatusOfOrDeleteSale");
        			msg.add("Delete");
        			msg.add(idNumberToDelete);
        			salesFromDb.remove(sale);
        			list.setAll(salesFromDb);
        			ClientUI.chat.accept(msg);
        			idFound = true;
        			break;
        		}
        	}
        	if(!idFound) {
        		erroeLabel.setText("id Not Found");
        	}

    	}
    	
    }

    /**
     * Handles the event where the disactive button is pressed.
     * Sends a message to the server to set the sale with the specified ID to 0, and updates the table view.
     * @param event the ActionEvent that occurred
     * @throws IOException if an I/O error occurs
     */
    @FXML
    void pressDisactiveBTN(ActionEvent event) throws IOException {
    	boolean idFound = false;
    	erroeLabel.setText("");
    	String idNumberToActivate = idLabel.getText();
    	ArrayList<String> msg = new ArrayList<>();
    	if(idNumberToActivate.equals("")) {
    		erroeLabel.setText("No Id Inserted");
    	}else {
    		for(Sale sale : salesFromDb) {
    			if(sale.getSaleNumber().equals(idNumberToActivate)) {
    				msg.add("ChangeStatusOfOrDeleteSale");
    				msg.add("SetDisActive");
    				msg.add(idNumberToActivate);
    				sale.setIsActive("0");
    				list.setAll(salesFromDb);
    				ClientUI.chat.accept(msg);
    				idFound = true;
    				break;
    			}
    		}
    		if(!idFound) {
    			erroeLabel.setText("id Not Found");
    		}
    	}

    }
    /**
     * Handles the event where the return button is pressed.
     * Opens the SalesAndMarketingManagmentPage.fxml file in a new window.
     * @param event the ActionEvent that occurred
     * @throws IOException if an I/O error */
    
    @FXML
    void pressReturnBTN(ActionEvent event) throws Exception {
    	Stage primaryStage;
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		MarketingManagerHomePageController mmhpc = new MarketingManagerHomePageController();
		MarketingManagerHomePageController.getUserData(arrFromServerRet);
		MarketingManagerHomePageController.ekOrOl(ekOrOLFromConnect);
		list.clear();
		mmhpc.start(primaryStage);
    }

    /**
     * Handles the event where the exit button is pressed.
     * Closes the current window.
     * @param event the ActionEvent that occurred
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
     * The start method for the application. This method sets up and displays the initial user interface, and
     * adds event handlers for mouse press and drag events on the root element of the user interface. When the mouse
     * is pressed and dragged, the stage is moved to a new location based on the distance that the mouse was dragged.
     *
     * @param primaryStage the primary stage for the application
     * @throws Exception if an error occurs while loading the FXML file
     */
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file using an FXMLLoader
        AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/ChoseSalesMarketingManager.fxml"));

        // Set up event handlers for mouse press and drag events on the root element
        root.setOnMousePressed(event -> {
            xoffset = event.getSceneX();
            yoffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xoffset);
            primaryStage.setY(event.getScreenY() - yoffset);
        });

        // Create a new scene and set the root node as the root element of the FXML file
        Scene scene = new Scene(root);

        // Set the scene for the stage and show it
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
