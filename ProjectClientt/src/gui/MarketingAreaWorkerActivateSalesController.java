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
import javafx.scene.control.Alert;
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

MarketingAreaWorkerActivateSalesController is a controller for the Marketing Area Worker Activate Sales GUI.

It handles the interactions between the GUI elements and the business logic.
*/

public class MarketingAreaWorkerActivateSalesController implements Initializable {

	@FXML
	private TextField idLabel;
    @FXML
    private Button activeBTN;

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

    
    private double xoffset;
   	private double yoffset;
   	private static ArrayList<String> arrFromServerRet;
   	private static ArrayList<String> ekOrOLFromConnect;
   	private static ArrayList<Sale> salesFromDb = new ArrayList<>();
   	static ObservableList<Sale> list = FXCollections.observableArrayList();
   	private static boolean isCalled = false;
    private static String workerLocation;

        
  
    /**
     * Handles the press of the Activate button.
     * Sends a request to the server to activate the sale with the entered ID.
     *
     * @param event the press of the Activate button
     * @throws IOException
     */
    @FXML
    void pressActiveBTN(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        boolean idFound = false;
        erroeLabel.setText("");
        String idNumberToActivate = idLabel.getText();
        ArrayList<String> msg = new ArrayList<>();
        if(idNumberToActivate.equals("")) {
            erroeLabel.setText("No Id Inserted");
        }else {
            for(Sale sale : salesFromDb) {
                if(sale.getSaleNumber().equals(idNumberToActivate)) {
                    if(sale.getIsActive().equals("1")) {
                        msg.add("updateSaleOnMechines");
                        msg.add("SetActive");
                        msg.add(workerLocation);
                        msg.add(sale.getDiscountLevel());
                        ClientUI.chat.accept(msg);
                        idFound = true;
                        alert.setTitle("activate");
                        alert.setHeaderText("Sale activated");
                        alert.showAndWait();
                        break;
                    }
                    else {
                        erroeLabel.setText("Sale is not Active");
                        idFound = true;
                        break;
                    }

                }
            }
            if(!idFound) {
                erroeLabel.setText("id Not Found");
            }
        }
    }

    /**
     * Handles the press of the Disactivate button.
     * Sends a request to the server to disactivate the sale with the entered ID.
     *
     * @param event the press of the Disactivate button
     * @throws IOException
     */
    @FXML
    void pressDisactiveBTN(ActionEvent event) throws IOException {
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
    	boolean idFound = false;
    	erroeLabel.setText("");
    	String idNumberToActivate = idLabel.getText();
    	ArrayList<String> msg = new ArrayList<>();
    	if(idNumberToActivate.equals("")) {
    		erroeLabel.setText("No Id Inserted");
    	}else {
    		for(Sale sale : salesFromDb) {
    			if(sale.getSaleNumber().equals(idNumberToActivate)) {
    				if(sale.getIsActive().equals("1")) {
    					msg.add("updateSaleOnMechines");
        				msg.add("SetDisActive");
        				msg.add(workerLocation);
        				ClientUI.chat.accept(msg);
        				idFound = true;
        				alert.setTitle("closed");
        				alert.setHeaderText("The sale is closed");
        				alert.showAndWait();
        				break;
    				}
    				else {
    	    			erroeLabel.setText("Sale is not Active");
        				idFound = true;
        				break;
    				}

    			}
    		}
    		if(!idFound) {
    			erroeLabel.setText("id Not Found");
    		}
    	}

    }
    
    /**
     * Handles the press of the Return button.
     * Hides the current window and opens the Marketing Area Worker home page.
     *
     * @param event the press of the Return button
     * @throws Exception
     */
    @FXML
    void pressReturnBTN(ActionEvent event) throws Exception {
        Stage primaryStage;
        ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
        primaryStage = new Stage();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        MarketingAreaWorkerHomePageController mawhpc = new MarketingAreaWorkerHomePageController();
        MarketingAreaWorkerHomePageController.getUserData(arrFromServerRet);
        MarketingAreaWorkerHomePageController.ekOrOl(ekOrOLFromConnect);
        list.clear();
        mawhpc.start(primaryStage);
    }
    
    /**
     * Sets the sales list from the server.
     *
     * @param arrFromServer an ArrayList containing the sales data
     */
    public static void setSalesList(ArrayList<String> arrFromServer){ 
        if(!isCalled) {
            String sales[] = ((String) arrFromServer.get(0)).split(" ");
            for(int i=0;i<sales.length;i+=5) {
                salesFromDb.add(new Sale(sales[i],sales[i+1],sales[i+2],sales[i+3],sales[i+4]));
            }
            isCalled = true;
        }
    }

    
    /**
   	Receives the user data from the previous controller.
   	@param arrFromServer an ArrayList containing the user data
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
	 * Initializes the controller class. Initializes the Name Of User label with the
	 * user's name.
	 *
	 * @param url the URL of the FXML file
	 * @param rb  the ResourceBundle of the FXML file
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		workerLocation=arrFromServerRet.get(6);
		System.out.println(workerLocation);
		ArrayList<String> msg = new ArrayList<>();
		msg.add("getSalesFromDbToMarketingWorker");
		msg.add(workerLocation);
		
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
     * Opens the Marketing Area Worker Activate Sales GUI.
     *
     * @param primaryStage the Stage to be shown
     * @throws Exception
     */
	public void start(Stage primaryStage) throws Exception {
    	
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/MarketingAreaWorkerActivateSales.fxml"));
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
