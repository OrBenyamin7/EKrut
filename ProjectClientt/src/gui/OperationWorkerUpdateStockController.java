package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.CustomerToApprove;
import logic.ItemToUpdate;
import logic.MessageToApprove;

/**
 * Class for operation worker window of updating stock
 * @author Michal and Or
 *
 */
public class OperationWorkerUpdateStockController implements Initializable{

	@FXML
    private Button backBtn;

    @FXML
    private TableColumn<ItemToUpdate, String> currentAmountColumn;

    @FXML
    private ImageView ekrutLogoImage;

    @FXML
    private Button exitBtn;

    @FXML
    private Label feedbackLabel;

    @FXML
    private TableColumn<ItemToUpdate, String> itemColumn;

    @FXML
    private ImageView machineNameImg;

    @FXML
    private Label machineNameLable;

    @FXML
    private ImageView managmentAreaImg;

    @FXML
    private Label managmentAreaLable;

    @FXML
    private TableColumn<ItemToUpdate, String> numberColumn;

    @FXML
    private Label specificAreaLable;

    @FXML
    private Label specificMachineNameLable;

    @FXML
    private Button saveBtn;

    @FXML
    private Label updateLabel;

    @FXML
    private TableView<ItemToUpdate> updateStockTable;
    
    private double xoffset;
	private double yoffset;
	
	private static ArrayList<String> areaAndMachineName;
	private static ArrayList<String> stockOfMachine;
	private static ArrayList<String> updateAboutStock;
	
	private static ArrayList<String> ekOrOLFromConnect;
	private static ArrayList<String> detailsOnUserArray;

	/**
	 * start window to provide movement window
	 * @param primaryStage
	 * @throws Exception
	 */
    public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/OperationWorkerUpdateStock.fxml"));

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
		primaryStage.setScene(scene);
		primaryStage.show();
	}
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		
    	specificAreaLable.setText(areaAndMachineName.get(0));
    	specificMachineNameLable.setText(areaAndMachineName.get(1));
    	
    	ArrayList<String> msg1 = new ArrayList<>();
    	msg1.add("getStockForOperationWorker");
    	msg1.add(areaAndMachineName.get(0)); //area
		msg1.add(areaAndMachineName.get(1)); //machine number
    	try {
			ClientUI.chat.accept(msg1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	    	
    	ObservableList<ItemToUpdate> items = FXCollections.observableArrayList();
    	int j=1;
		
		for (int i = 0; i < stockOfMachine.size()-1; i+=2, j++) { // inserting customers data to table
			items.add(new ItemToUpdate(Integer.toString(j)
					,stockOfMachine.get(i),stockOfMachine.get(i+1)));
		}
    	
		
    	
		
		
    	numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));
		itemColumn.setCellValueFactory(new PropertyValueFactory<>("Item"));
		currentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("CurrentAmount"));
		
		
		updateStockTable.setItems(items);
		
		numberColumn.setStyle("-fx-alignment: CENTER;");
		itemColumn.setStyle("-fx-alignment: CENTER;");
		currentAmountColumn.setStyle("-fx-alignment: CENTER;");
		
	}
    

  	@FXML
  	/**
  	 * press back button from current window
  	 * @param event
  	 * @throws Exception
  	 */
  	void pressBackBtn(ActionEvent event) throws Exception {
  		Stage primaryStage = new Stage();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		primaryStage.initStyle(StageStyle.UNDECORATED);
		OperationWorkerUpdateStockReviewController owusrc = new OperationWorkerUpdateStockReviewController();
		OperationWorkerUpdateStockReviewController.getUserData(detailsOnUserArray);
		OperationWorkerUpdateStockReviewController.ekOrOl(ekOrOLFromConnect);
		owusrc.start(primaryStage);
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
     * press save button to save all the changes made by the operation worker in the changes in stock
     * @param event
     */
    void pressSaveBtn(ActionEvent event) 
    {
    	ArrayList<String> msg1 = new ArrayList<>();
    	String newStock = "";
    	
    	
    	if(checkValid())
    	{
    		for(int i=0;i<updateStockTable.getItems().size();i++)
        	{
        		newStock += updateStockTable.getItems().get(i).getItem() + "," + 
        				updateStockTable.getItems().get(i).getCurrentAmount().getText().trim();
        		if(i +1 !=updateStockTable.getItems().size())
        			newStock += ",";
        	}
    		msg1.add("updateStockByOperationWorker");
    		msg1.add(areaAndMachineName.get(0)); //area
    		msg1.add(areaAndMachineName.get(1)); //machineNumber
    		msg1.add(newStock);
    		try 
    		{
    			ClientUI.chat.accept(msg1);
    		} 
    		catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		if(updateAboutStock.get(0).equals("Success!"))
    		{
    			
    			feedbackLabel.setTextFill(Color.GREEN);
    			feedbackLabel.setText("saved Successfully!");
    				
    		}
    		else
    		{
    			feedbackLabel.setText("save failed!");
    		}
    	}	
    }
    
    /**
     * private function that check the valid of inserting
     * @return true & false
     */
    private boolean checkValid()
    {
    	//String input = amountAddedTextField.getText().trim();
		feedbackLabel.setTextFill(Color.RED);
    	for(int i=0;i < updateStockTable.getItems().size();i++)
    	{
    		String amount = updateStockTable.getItems().get(i).getCurrentAmount().getText().trim();
    		
    		for(int j=0;j<amount.length();j++)
    		{
    			if(amount.charAt(j)<'0' || amount.charAt(j)>'9')
        		{
        			feedbackLabel.setText("Inorder to update stock you must\n"
        					+ "enter valid amount(only digits and positive number)");
        			return false;
        		}
    			
    			if(Integer.parseInt(amount)<Integer.parseInt(stockOfMachine.get(stockOfMachine.size()-1)))
    			{
    				feedbackLabel.setText("Inorder to update stock you must\n"
        					+ "enter amount bigger than minimum level");
        			return false;
    			}
    				
    		}
    	}
    	return true;
    }

    
    /**
     * function that get the area and machines name from db
     * @param data
     */
    public static void getAreaAndMachineName(ArrayList<String> data)
    {
    	areaAndMachineName = data;
    }
    
    /**
     * function that get the stock of specific machine
     * @param data
     */
    public static void getStock(ArrayList<String> data)
    {
    	stockOfMachine = data;
    }
    
    /**
     * function that get update from DB abou the changes made on the stock
     * @param data
     */
    public static void getUpdateFromDBAboutStock(ArrayList<String> data)
    {
    	updateAboutStock = data;
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
	public static void detailsOnUser(ArrayList<String> arrFromServer) {
		detailsOnUserArray = arrFromServer;
	}
    
    

}

