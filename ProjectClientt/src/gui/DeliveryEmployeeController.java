package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import logic.CalcEstimatedDeliveryTime;
import logic.Delivery;


/**
 * The DeliveryEmployeeController class is a JavaFX controller class that is used to handle the GUI events 
 * of a delivery management system for a company. The class is annotated with the {@code @FXML} annotation, 
 * which indicates that the class is controlled by an FXML file, and the class uses the {@code javafx.fxml.FXML} 
 * annotation to map the GUI elements in the FXML file to the fields of this class.
 *
 */
public class DeliveryEmployeeController  implements Initializable {

    /**
     * orderNumberLabel; TextField is used to enter the orderNumber of the delivery
     */
    @FXML
    private TextField orderNumberLabel;
    
	
    /**
     * EnterArea TextField is used to enter the area of the delivery
     */
    @FXML
    private TextField EnterArea;
    
    /**
     * MessageUserPassLabel is used to show the message if the user entered wrong details
     */
    @FXML
    private Label MessageUserPassLabel;
    /**
     * NameOfUserLabel is used to show the name of the user
     */
    @FXML
    private Label NameOfUserLabel;
    /**
     * exitBTN button is used to exit the application
     */
    @FXML
    private Button exitBTN;
    /**
     * ReturnBTN button is used to return to the previous screen
     */
    @FXML
    private Button ReturnBTN;
    /**
     * reloadTableDataBTN button is used to reload the data in the table
     */
    @FXML
    private Button reloadTableDataBTN;
    /**
     * idLabel is used to show the id of the order
     */
    @FXML
    private TextField idLabel;
    /**
     * CloseOrderBTN button is used to close an order
     */
    @FXML
    private Button CloseOrderBTN;
    /**
     * deliveryTable TableView is used to display the delivery data
     */
    @FXML
    private TableView<Delivery> deliveryTable;
    /**
     * requestCol TableColumn is used to show the requests of the delivery
     */
    @FXML
    private TableColumn<Delivery, String> requestCol;
    
    /**
     * orderNumCol TableColumn is used to show the order number of the delivery
     */
    @FXML
    private TableColumn<Delivery, String> orderNumCol;
    /**
     * ClientDeliveryStatusCol TableColumn is used to show the delivery status of the client
     */
    @FXML
    private TableColumn<Delivery, String> ClientDeliveryStatusCol;
    /**
     * addressCol TableColumn is used to show the address of the delivery
     */
    @FXML
    private TableColumn<Delivery, String> addressCol;
    /**
     * DeliveryDetailsCol TableColumn is used to show the delivery details of the order
     */
    @FXML
    private TableColumn<Delivery, String> DeliveryDetailsCol;
    /**
     * DaysLeftForDeliveryCol TableColumn is used to show the days left for the delivery
     */
    @FXML
    private TableColumn<Delivery, String> DaysLeftForDeliveryCol;
    /**
     * ClientNotifiedCol TableColumn is used to show whether the client is notified or not
     */
    @FXML
    private TableColumn<Delivery, String> ClientNotifiedCol;
    /*
     * xoffset,yoffset to handle drag of the window.
     */
    private double xoffset;
	private double yoffset;
    /**
     * arrFromServerRet ArrayList is used to store the data received from the server
     */
	private static ArrayList<String> arrFromServerRet;
   	private static ArrayList<String> ekOrOLFromConnect;
   	private static ArrayList<String> arrtest;
    /**
     * deliveryFromDb ArrayList is used to store the delivery data from the database
     */
   	private static ArrayList<Delivery> deliveryFromDb = new ArrayList<>();
    /**
     * list ObservableList is used to display the delivery data in the table
     */
   	static ObservableList<Delivery> list = FXCollections.observableArrayList();
   	private boolean isCalled = false;
   	private static boolean refreshed = false;
    private static String workerLocation;
    private String orderNumber;
    private boolean requestApproved = false;
    @FXML
    private Button approveBTN;

    /**
     * ADDTOTIME is used to add the time to the delivery details
     */
	public static final double ADDTOTIME = 12.00;
	static Period period;
	private static LocalDateTime deliveryDetails;
    private static String estimatedDeliveryDateValue1;
    private static String daysLeftForDeliveryValue;
    
    /**
     * rootPane, pane AnchorPane is used as the root pane and the main container for the GUI elements
     */
    @FXML
    private AnchorPane rootPane, pane;


    /**
     * Handles the functionality when the "Exit" button is pressed. 
     * It closes the application.
     *
     * @param event The event that triggers the method, in this case the "Exit" button press.
     */
    @FXML
    void exitBTNPress(ActionEvent event) throws IOException {
    	ArrayList<String> msg = new ArrayList<>();
		msg.add("quit");
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		ClientUI.chat.accept(msg);
		System.exit(1); 
    }
    
    /**
     * Handles the functionality when the "Return" button is pressed. 
     * It returns to the previous screen.
     *
     * @param event The event that triggers the method, in this case the "Return" button press.
     */

    @FXML
    void pressReturnBTN(ActionEvent event) throws Exception { 
    	Stage primaryStage;
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		DeliveryEmployeeHomePageController dehpc = new DeliveryEmployeeHomePageController();
		DeliveryEmployeeHomePageController.getUserData(arrFromServerRet);
		DeliveryEmployeeHomePageController.ekOrOl(ekOrOLFromConnect);
		list.clear();
		dehpc.start(primaryStage);
    }
    
    
    
  
	/*
	* start method is to load the FXML file, to move the window with the mouse and to show the data in the table view.
	* @param primaryStage the primary stage for this application
	* @throws Exception
	*/
    public void start(Stage primaryStage) throws Exception {
    	
		rootPane = FXMLLoader.load(getClass().getResource("/gui/DeliveryEmployee.fxml"));
		//event handler for when the mouse is pressed AND dragged to move the window
		rootPane.setOnMousePressed(event1 -> {
            xoffset = event1.getSceneX();
            yoffset = event1.getSceneY();
        });

        // event handler for when the mouse is pressed AND dragged to move the window
		rootPane.setOnMouseDragged(event1 -> {
            primaryStage.setX(event1.getScreenX()-xoffset);
            primaryStage.setY(event1.getScreenY()-yoffset);
        });
        
		Scene scene = new Scene(rootPane);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
    
    /*
     * gets user data static method
     */
    public static void getUserData(ArrayList<String> arrFromServer){
 		arrFromServerRet = arrFromServer;
 	}
    
    
    
    /*
     * sets OL or EK connection option static method
     */
    public static void ekOrOl(ArrayList<String> ekOrOLFromConnect1){
     	ekOrOLFromConnect = ekOrOLFromConnect1;
     }
 	 
    /**
     * This method receives new deliveries data from the server and updates the table with the new data.
     * @param newDeliveriesList ArrayList of delivery which contains new deliveries data 
     */
 	 public static void setNewDeliverysList(ArrayList<String> arrFromServer){ 
 		
 		arrtest = arrFromServer; //"updateReservationStatusToCompleted",idNumberToClose
     	deliveryFromDb.clear();
     	
         	String deliverys[] = ((String) arrFromServer.get(0)).split(" ");
     	
         	for(int i=0;i<deliverys.length;i+=11) {

         		deliveryFromDb.add(new Delivery(deliverys[i],deliverys[i+1],deliverys[i+2]+" "+deliverys[i+3]+" "+deliverys[i+4], deliverys[i+10], deliverys[i+5], deliverys[i+6], deliverys[i+8], deliverys[i+9], deliverys[i+7]));	
     	                                       // number,       Status,       Address(city           Street             number),  requestapp,       days4Delivery, clientNotified, deliveryReceived, del-details,  area(location)  
         	}
  	}
 	 



     /**
      * Handles the functionality when the "Close Order" button is pressed. 
      * It closes an order.
      *
      * @param event The event that triggers the method, in this case the "Close Order" button press.
      */
 	  @FXML
 	  void pressCloseOrderBTN(ActionEvent event) throws IOException {
 		boolean idFound = false;
 		MessageUserPassLabel.setText("");
     	String idNumberToClose = idLabel.getText();
     	ArrayList<String> msg = new ArrayList<>();
     	if(idNumberToClose.equals("")) {
     		MessageUserPassLabel.setText("No Id Inserted");
     	}
     	else {
     		for(Delivery dev : deliveryFromDb) {
     			
     			if(dev.getOrdernumber().equals(idNumberToClose)) {
     				if(dev.getDeliveryReceived().equals("Recived")) {
     					msg.add("updateReservationStatusClose"); 
         				msg.add(idNumberToClose);
         				ClientUI.chat.accept(msg);
         				dev.setClientDelStatus("Completed");
        				list.setAll(deliveryFromDb);
         				idFound = true;
         				break;
     				}
     				else {
     					MessageUserPassLabel.setText("Customer did not recieve Delivery yet");
         				idFound = true;
         				break;
     				}
     			}
     		}
     		if(!idFound) {
     			MessageUserPassLabel.setText("Order number is Not Found");
     		}
     	}
 	  }

 	 /**
 	 * Initializes the controller class and sets the location of the delivery worker, retrieves delivery data from the database,
 	 * and populates the table with the data.
 	 *
 	 * @param url The location used to resolve relative paths for the root object, or
 	 * {@code null} if the location is not known.
 	 * @param rb The resources used to localize the root object, or {@code null} if the root
 	 * object was not localized.
 	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		workerLocation = arrFromServerRet.get(6); //sets area location of delivery employee 
		EnterArea.setText(workerLocation);
		ArrayList<String> msg1 = new ArrayList<>();
		msg1.add("getdelToWorkerFromDbForTable");
		msg1.add(workerLocation);

		try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		list.addAll(deliveryFromDb);
    	
		orderNumCol.setCellValueFactory(new PropertyValueFactory<Delivery, String>("Ordernumber"));
		ClientDeliveryStatusCol.setCellValueFactory(new PropertyValueFactory<Delivery, String>("ClientDelStatus"));
		addressCol.setCellValueFactory(new PropertyValueFactory<Delivery, String>("address"));
		DeliveryDetailsCol.setCellValueFactory(new PropertyValueFactory<Delivery, String>("DeliveryDetails"));
		DaysLeftForDeliveryCol.setCellValueFactory(new PropertyValueFactory<Delivery, String>("DaysLeftForDelivery"));
		ClientNotifiedCol.setCellValueFactory(new PropertyValueFactory<Delivery, String>("ClientNotified"));
		requestCol.setCellValueFactory(new PropertyValueFactory<Delivery, String>("approveRequest"));
	    deliveryTable.setItems(list);
	}
	
	
	
	
    /**
     * Handles the functionality when the "Reload Table Data" button is pressed. 
     * It reloads the data in the table.
     *
     * @param event The event that triggers the method, in this case the "Reload Table Data" button press.
     */
    @FXML
    void pressReloadTableDataBTN(ActionEvent event) throws Exception { 
    	Stage primaryStage;
    	((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		primaryStage = new Stage();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		DeliveryEmployeeController dec = new DeliveryEmployeeController();
		DeliveryEmployeeController.getUserData(arrFromServerRet);
		DeliveryEmployeeController.getDeliveryData(deliveryFromDb);
		DeliveryEmployeeController.ekOrOl(ekOrOLFromConnect);
		list.clear();
		dec.start(primaryStage);
    }

    
    /**
     * Static method that is used to get the delivery data from the server.
     * @param DeliveryFromDb ArrayList of delivery to get the data from
     */
    public static void getDeliveryData(ArrayList<Delivery> DeliveryFromDb){
    	deliveryFromDb = DeliveryFromDb;
 	}
    
    
    
    
    /**
     * This method approves the delivery status, calculates the estimated delivery details and notifies the client.
     * @param deliveryLine Delivery object representing the delivery line to approve
     * @param hoursToAdd double value representing the number of hours to add to the current time for estimated delivery
     * @param approved boolean value representing if the delivery status is approved or not
     */
 	public static void approveStatusCalcNotifyDelLine(){ 
    	//boolean idFound = false;
 		//approve set in DB approve column
 		ArrayList<String> estimatedDelDetails = new ArrayList<>();
 		ArrayList<String> msg = new ArrayList<>();
 		for(Delivery dev : deliveryFromDb) {
 				if(dev.getApproveRequest().equals("NotApproved") ) {
 					
 					msg.add("updateApproveStatusCalcNotifyDelLine");
 					msg.add(dev.getOrdernumber());
 					estimatedDelDetails =  CalcEstimatedDelDetails(dev.getArea());
 					msg.add(estimatedDelDetails.get(0));//date
 					msg.add(estimatedDelDetails.get(1));//time
 					try {
						ClientUI.chat.accept(msg); //"updateApproveStatusCalcNotifyDelLine", ordernum, estimatedDelDetails.get(0), estimatedDelDetails.get(1)
					} catch (IOException e) {
						e.printStackTrace();
					}
 					dev.setApproveRequest("Approved");
 					dev.setClientDelStatus("Handled");
 					dev.setClientNotified("YES");
 					dev.setDeliveryDetails(estimatedDelDetails.get(0));
 					dev.setDaysLeftForDelivery(estimatedDelDetails.get(1));
 					list.setAll(deliveryFromDb);
 					break;
 				}
 			}
 		}
 	
 	  
    @FXML
    void pressApproveBTN(ActionEvent event) throws IOException {
    	boolean idFound = false;
    	MessageUserPassLabel.setText("");
     	String orderNumber = orderNumberLabel.getText();
     	ArrayList<String> msg = new ArrayList<>();
     	if(orderNumber.equals("")) {
     		MessageUserPassLabel.setText("No order Number Inserted");
     	}else {
     		for(Delivery dev : deliveryFromDb) {
     			if(dev.getOrdernumber().equals(orderNumber)) {
     				if(dev.getDeliveryReceived().equals("NotRecived"))
     				{
         				idFound = true;
         				approveStatusCalcNotifyDelLine();
         				break;
     				}
     				else {
     					MessageUserPassLabel.setText("delivery alredy excepted by client");
     					break;
     				}

     			}

     		}
     	}
     	if(!idFound)
     		MessageUserPassLabel.setText("order Number is not found");
    }
 	
 	
 	
    
 	/**
 	 * This method calculates the estimated delivery details by adding a specific amount of time to the current time.
 	 * @param hoursToAdd double value representing the number of hours to add to the current time
 	 * @return LocalDateTime object representing the estimated delivery date and time
 	 */
 	public static ArrayList<String> CalcEstimatedDelDetails(String area) {
 		
 			ArrayList<String> estimatedDelDetails = new ArrayList<>();
    		//calculate estimated delivery details
 			CalcEstimatedDeliveryTime deliveryCalculator = new CalcEstimatedDeliveryTime();
 			deliveryDetails = deliveryCalculator.calcEstimatedDeliveryTime(area);
    		
    		if(deliveryDetails.getHour() >= 20 || deliveryDetails.getHour() < 8) {
    			//if delivery time id between 20:00 to 08:00 adding 12 hours for delivery time
    			deliveryDetails = deliveryDetails.plusHours((long)ADDTOTIME);
    		}
    			
    			DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy-->HH:mm");
    			String deliveryDetailsString = deliveryDetails.format(customFormat);
    			estimatedDeliveryDateValue1 = deliveryDetailsString;
    			estimatedDelDetails.add(estimatedDeliveryDateValue1);

    			period = Period.between( LocalDate.now(),deliveryDetails.toLocalDate());  //days left for delivery
    			daysLeftForDeliveryValue = Integer.toString(period.getDays());
    			estimatedDelDetails.add(daysLeftForDeliveryValue);
   
    		return estimatedDelDetails;
 	}
}


